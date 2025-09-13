package com.ars.userservice.service.impl;

import com.ars.userservice.dto.mapping.IAuthorityDTO;
import com.ars.userservice.dto.mapping.IRoleDTO;
import com.ars.userservice.dto.request.role.CreateRoleRequestDTO;
import com.ars.userservice.dto.request.role.UpdateRoleRequestDTO;
import com.ars.userservice.dto.response.RoleDTO;
import com.ars.userservice.entity.Authority;
import com.ars.userservice.entity.RoleAuthority;
import com.ars.userservice.entity.Roles;
import com.ars.userservice.repository.AuthorityRepository;
import com.ars.userservice.repository.RoleAuthorityRepository;
import com.ars.userservice.repository.RoleRepository;
import com.ars.userservice.service.RoleService;

import com.dct.model.common.BaseCommon;
import com.dct.model.common.MessageTranslationUtils;
import com.dct.model.constants.BaseExceptionConstants;
import com.dct.model.dto.auth.AuthorityTreeNode;
import com.dct.model.dto.request.BaseRequestDTO;
import com.dct.model.dto.response.BaseResponseDTO;
import com.dct.model.exception.BaseBadRequestException;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    private static final String ENTITY_NAME = "com.ars.userservice.service.impl.RoleServiceImpl";
    private final RoleAuthorityRepository roleAuthorityRepository;
    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;
    private final MessageTranslationUtils messageTranslationUtils;

    public RoleServiceImpl(RoleAuthorityRepository roleAuthorityRepository,
                           AuthorityRepository authorityRepository,
                           RoleRepository roleRepository,
                           MessageTranslationUtils messageTranslationUtils) {
        this.roleAuthorityRepository = roleAuthorityRepository;
        this.authorityRepository = authorityRepository;
        this.roleRepository = roleRepository;
        this.messageTranslationUtils = messageTranslationUtils;
    }

    @Override
    public BaseResponseDTO getRolesWithPaging(BaseRequestDTO request) {
        Page<IRoleDTO> rolesWithPaged = roleRepository.findAllWithPaging(request.getKeywordSearch(), request.getPageable());
        return BaseResponseDTO.builder().total(rolesWithPaged.getTotalElements()).ok(rolesWithPaged.getContent());
    }

    @Override
    public BaseResponseDTO getRoleDetail(Integer roleId) {
        Optional<Roles> roleOptional = roleRepository.findById(roleId);

        if (roleOptional.isEmpty()) {
            throw new BaseBadRequestException(ENTITY_NAME, BaseExceptionConstants.DATA_NOT_FOUND);
        }

        RoleDTO roleDTO = new RoleDTO();
        BeanUtils.copyProperties(roleOptional.get(), roleDTO);
        roleDTO.setAuthorities(authorityRepository.findAllByRoleId(roleId));
        return BaseResponseDTO.builder().ok(roleDTO);
    }

    @Override
    public BaseResponseDTO getAuthoritiesTree() {
        List<IAuthorityDTO> authorities = authorityRepository.findAllByOrderByCodeAsc();
        Map<String, AuthorityTreeNode> authorityTreeMap = new HashMap<>();
        List<AuthorityTreeNode> roots = new ArrayList<>();

        for (IAuthorityDTO permission : authorities) {
            AuthorityTreeNode node = AuthorityTreeNode.builder()
                    .id(permission.getId())
                    .name(messageTranslationUtils.getMessageI18n(permission.getName()))
                    .code(permission.getCode())
                    .parentId(permission.getParentId())
                    .build();

            if (Objects.isNull(permission.getParentCode())) {
                authorityTreeMap.put(node.getCode(), node);
                roots.add(node);
            } else {
                AuthorityTreeNode parentNode = authorityTreeMap.get(permission.getParentCode());

                if (Objects.nonNull(parentNode)) {
                    parentNode.appendChildren(node);
                    authorityTreeMap.put(node.getCode(), node);
                }
            }
        }

        List<AuthorityTreeNode> results = roots.stream()
                .map(node -> authorityTreeMap.get(node.getCode()))
                .collect(Collectors.toList());

        return BaseResponseDTO.builder().ok(results);
    }

    @Override
    public List<IRoleDTO> getUserRoles(Integer userId) {
        if (Objects.isNull(userId) || userId <= 0) {
            throw new BaseBadRequestException(ENTITY_NAME, BaseExceptionConstants.INVALID_REQUEST_DATA);
        }

        return roleRepository.findAllByUserId(userId);
    }

    @Override
    @Transactional
    public BaseResponseDTO createNewRole(CreateRoleRequestDTO request) {
        boolean isRoleExisted = roleRepository.existsByCodeOrName(request.getCode(), request.getName());

        if (isRoleExisted) {
            throw new BaseBadRequestException(ENTITY_NAME, BaseExceptionConstants.ROLE_EXISTED);
        }

        Roles role = Roles.builder()
                .name(request.getName())
                .code(request.getCode())
                .normalizedName(BaseCommon.normalizeName(request.getName()))
                .build();
        roleRepository.save(role);
        List<IAuthorityDTO> authorityDTOS = authorityRepository.findAllByIds(request.getAuthorityIds());
        List<RoleAuthority> roleAuthorities = new ArrayList<>();

        if (authorityDTOS.isEmpty() || authorityDTOS.size() != request.getAuthorityIds().size()) {
            throw new BaseBadRequestException(ENTITY_NAME, BaseExceptionConstants.ROLE_AUTHORITY_INVALID);
        }

        authorityDTOS.forEach(authority -> {
            RoleAuthority roleAuthority = RoleAuthority.builder()
                    .roleId(role.getId())
                    .authorityId(authority.getId())
                    .build();
            roleAuthorities.add(roleAuthority);
        });
        roleAuthorityRepository.saveAll(roleAuthorities);
        return BaseResponseDTO.builder().ok();
    }

    @Override
    @Transactional
    public BaseResponseDTO updateRole(UpdateRoleRequestDTO request) {
        Optional<Roles> roleOptional = roleRepository.findById(request.getId());

        if (roleOptional.isEmpty()) {
            throw new BaseBadRequestException(ENTITY_NAME, BaseExceptionConstants.DATA_NOT_FOUND);
        }

        Roles role = roleOptional.get();

        // If the updated role content already exists
        if (roleRepository.existsByCodeOrNameAndIdNot(request.getCode(), request.getName(), role.getId())) {
            throw new BaseBadRequestException(ENTITY_NAME, BaseExceptionConstants.ROLE_EXISTED);
        }

        List<Authority> authoritiesForUpdate = authorityRepository.findAllById(request.getAuthorityIds());

        if (authoritiesForUpdate.isEmpty() || authoritiesForUpdate.size() != request.getAuthorityIds().size()) {
            throw new BaseBadRequestException(ENTITY_NAME, BaseExceptionConstants.ROLE_AUTHORITY_INVALID);
        }

        role.setName(request.getName());
        role.setCode(request.getCode());
        role.setAuthorities(authoritiesForUpdate);
        roleRepository.save(role);
        return BaseResponseDTO.builder().ok(role);
    }

    @Override
    @Transactional
    public BaseResponseDTO deleteRole(Integer roleId) {
        if (Objects.isNull(roleId) || roleId <= 0) {
            throw new BaseBadRequestException(ENTITY_NAME, BaseExceptionConstants.INVALID_REQUEST_DATA);
        }

        roleRepository.deleteById(roleId);
        return BaseResponseDTO.builder().ok();
    }
}
