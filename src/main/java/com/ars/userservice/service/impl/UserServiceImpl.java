package com.ars.userservice.service.impl;

import com.ars.userservice.constants.UserConstants;
import com.ars.userservice.dto.mapping.IRoleDTO;
import com.ars.userservice.dto.mapping.IUserDTO;
import com.ars.userservice.dto.mapping.OAuth2UserDTO;
import com.ars.userservice.dto.request.user.ChangeEmailRequestDTO;
import com.ars.userservice.dto.request.user.ChangePasswordRequestDTO;
import com.ars.userservice.dto.request.user.ChangeUserStatusRequestDTO;
import com.ars.userservice.dto.request.user.CreateUserRequestDTO;
import com.ars.userservice.dto.request.user.RecoverPasswordRequestDTO;
import com.ars.userservice.dto.request.user.UpdateUserRequestDTO;
import com.ars.userservice.dto.response.UserDTO;
import com.ars.userservice.entity.Roles;
import com.ars.userservice.entity.UserRole;
import com.ars.userservice.entity.Users;
import com.ars.userservice.repository.RoleRepository;
import com.ars.userservice.repository.UserRepository;
import com.ars.userservice.repository.UserRoleRepository;
import com.ars.userservice.service.RoleService;
import com.ars.userservice.service.UserService;

import com.dct.model.common.CredentialGenerator;
import com.dct.model.constants.BaseExceptionConstants;
import com.dct.model.constants.BaseRegexConstants;
import com.dct.model.constants.BaseUserConstants;
import com.dct.model.dto.request.BaseRequestDTO;
import com.dct.model.dto.response.BaseResponseDTO;
import com.dct.model.event.UserShopCompletionEvent;
import com.dct.model.event.UserShopFailureEvent;
import com.dct.model.exception.BaseBadRequestException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private static final String ENTITY_NAME = "com.ars.userservice.service.impl.UserServiceImpl";
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           RoleService roleService,
                           RoleRepository roleRepository,
                           UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public BaseResponseDTO getUsersWithPaging(BaseRequestDTO request) {
        Page<IUserDTO> usersWithPaged = userRepository.findAllWithPaging(
            BaseUserConstants.Status.fromString(request.getStatusSearch(BaseRegexConstants.USER_STATUS_PATTERN)),
            request.getKeywordSearch(),
            request.getFromDateSearch(),
            request.getToDateSearch(),
            request.getPageable()
        );
        List<IUserDTO> users = usersWithPaged.getContent();
        return BaseResponseDTO.builder().total(usersWithPaged.getTotalElements()).ok(users);
    }

    @Override
    public BaseResponseDTO getUserDetail(Integer userId) {
        Optional<Users> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new BaseBadRequestException(ENTITY_NAME, BaseExceptionConstants.ACCOUNT_NOT_FOUND);
        }

        Users user = userOptional.get();
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        userDTO.setRoles(roleService.getUserRoles(userId));
        return BaseResponseDTO.builder().ok(userDTO);
    }

    @Override
    public BaseResponseDTO getShopOwnerInfos(Set<Integer> ownerIds) {
        return BaseResponseDTO.builder().ok(userRepository.getShopOwnerInfos(ownerIds));
    }

    @Override
    @Transactional
    public BaseResponseDTO createUser(CreateUserRequestDTO requestDTO) {
        if (userRepository.existsByUsernameOrEmail(requestDTO.getUsername(), requestDTO.getEmail())) {
            throw new BaseBadRequestException(ENTITY_NAME, BaseExceptionConstants.ACCOUNT_EXISTED);
        }

        List<IRoleDTO> roles = roleRepository.findAllByIds(requestDTO.getRoleIds());

        if (roles.isEmpty() || roles.size() != requestDTO.getRoleIds().size()) {
            throw new BaseBadRequestException(ENTITY_NAME, BaseExceptionConstants.ROLE_AUTHORITY_INVALID);
        }

        Users user = new Users();
        BeanUtils.copyProperties(requestDTO, user);
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        user.setStatus(BaseUserConstants.Status.ACTIVE);
        userRepository.save(user);
        int userId = user.getId();
        List<UserRole> userRoles = roles.stream().map(role -> new UserRole(userId, role.getId())).toList();
        userRoleRepository.saveAll(userRoles);
        return BaseResponseDTO.builder().ok(user);
    }

    @Override
    @Transactional
    public BaseResponseDTO updateUser(UpdateUserRequestDTO requestDTO) {
        boolean isUserExisted = userRepository.existsByUsernameOrEmailAndIdNot(
            requestDTO.getUsername(),
            requestDTO.getEmail(),
            requestDTO.getId()
        );

        if (isUserExisted) {
            throw new BaseBadRequestException(ENTITY_NAME, BaseExceptionConstants.ACCOUNT_EXISTED);
        }

        Optional<Users> userOptional = userRepository.findById(requestDTO.getId());

        if (userOptional.isEmpty()) {
            throw new BaseBadRequestException(ENTITY_NAME, BaseExceptionConstants.ACCOUNT_NOT_FOUND);
        }

        List<Roles> userRolesForUpdate = roleRepository.findAllById(requestDTO.getRoleIds());

        if (userRolesForUpdate.isEmpty() || userRolesForUpdate.size() != requestDTO.getRoleIds().size()) {
            throw new BaseBadRequestException(ENTITY_NAME, BaseExceptionConstants.ROLE_AUTHORITY_INVALID);
        }

        Users user = userOptional.get();
        BeanUtils.copyProperties(requestDTO, user);
        user.setRoles(userRolesForUpdate);
        userRepository.save(user);
        return BaseResponseDTO.builder().ok(user);
    }

    @Override
    @Transactional
    public BaseResponseDTO createOrUpdateUser(OAuth2UserDTO userDTO) {
        Optional<Users> userOptional = userRepository.findByEmail(userDTO.getEmail());

        if (userOptional.isEmpty()) {
            String rawPassword = CredentialGenerator.generatePassword(UserConstants.DEFAULT_PASSWORD_LENGTH);
            Users newUser = Users.builder()
                    .username(userDTO.getEmail())
                    .fullname(userDTO.getName())
                    .email(userDTO.getEmail())
                    .status(BaseUserConstants.Status.ACTIVE)
                    .password(passwordEncoder.encode(rawPassword))
                    .isAdmin(Boolean.FALSE)
                    .type("USER")
                    .build();
            userRepository.save(newUser);
            return BaseResponseDTO.builder().ok(newUser);
        }

        return BaseResponseDTO.builder().ok(userOptional.get());
    }

    @Override
    @Transactional
    public BaseResponseDTO deleteUser(Integer userId) {
        if (Objects.isNull(userId) || userId <= 0) {
            throw new BaseBadRequestException(ENTITY_NAME, BaseExceptionConstants.INVALID_REQUEST_DATA);
        }

        userRepository.updateUserStatusById(userId, BaseUserConstants.Status.DELETED);
        return BaseResponseDTO.builder().ok();
    }

    @Override
    @Transactional
    public BaseResponseDTO changeEmail(ChangeEmailRequestDTO requestDTO) {
        return null;
    }

    @Override
    @Transactional
    public BaseResponseDTO changeStatus(ChangeUserStatusRequestDTO requestDTO) {
        userRepository.updateUserStatusById(requestDTO.getId(), requestDTO.getStatus());
        return BaseResponseDTO.builder().ok();
    }

    @Override
    @Transactional
    public BaseResponseDTO changePassword(ChangePasswordRequestDTO requestDTO) {
        Optional<Users> userOptional = userRepository.findById(requestDTO.getId());

        if (userOptional.isEmpty()) {
            throw new BaseBadRequestException(ENTITY_NAME, BaseExceptionConstants.ACCOUNT_NOT_FOUND);
        }

        Users user = userOptional.get();
        String oldPassword = user.getPassword();
        String oldPasswordConfirm = requestDTO.getOldPassword();
        String newPassword = requestDTO.getNewPassword();

        if (!passwordEncoder.matches(oldPasswordConfirm, oldPassword)) {
            throw new BaseBadRequestException(ENTITY_NAME, BaseExceptionConstants.OLD_PASSWORD_INVALID);
        }

        if (Objects.equals(oldPasswordConfirm, newPassword)) {
            throw new BaseBadRequestException(ENTITY_NAME, BaseExceptionConstants.NEW_PASSWORD_DUPLICATED);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return BaseResponseDTO.builder().ok();
    }

    @Override
    @Transactional
    public BaseResponseDTO recoverPassword(RecoverPasswordRequestDTO requestDTO) {
        return null;
    }

    @Override
    @Transactional
    public void updateRegisterUserWithShopCompletion(UserShopCompletionEvent event) {
        log.info("[REGISTER_USER_WITH_SHOP_COMPLETION] - Register successfully with information: {}", event);
        userRepository.updateUserStatusById(event.getUserId(), BaseUserConstants.Status.ACTIVE);
    }

    @Override
    @Transactional
    public void rollbackRegisterUserWithShopFailure(UserShopFailureEvent event) {
        log.info("[ROLLBACK_REGISTER_USER_WITH_SHOP_FAILURE] - Deleting user for rollback action. Error: {}.", event);
        userRepository.deleteById(event.getUserId());
    }
}
