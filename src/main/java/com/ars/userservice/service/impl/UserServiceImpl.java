package com.ars.userservice.service.impl;

import com.ars.userservice.dto.mapping.IUserDTO;
import com.ars.userservice.dto.request.user.ChangeEmailRequestDTO;
import com.ars.userservice.dto.request.user.ChangePasswordRequestDTO;
import com.ars.userservice.dto.request.user.ChangeUserStatusRequestDTO;
import com.ars.userservice.dto.request.user.CreateUserRequestDTO;
import com.ars.userservice.dto.request.user.RecoverPasswordRequestDTO;
import com.ars.userservice.dto.request.user.UpdateUserRequestDTO;
import com.ars.userservice.dto.response.UserDTO;
import com.ars.userservice.entity.Users;
import com.ars.userservice.repository.RoleRepository;
import com.ars.userservice.repository.UserRepository;
import com.ars.userservice.repository.UserRoleRepository;
import com.ars.userservice.service.RoleService;
import com.ars.userservice.service.UserService;

import com.dct.model.constants.BaseExceptionConstants;
import com.dct.model.constants.BaseRegexConstants;
import com.dct.model.constants.BaseUserConstants;
import com.dct.model.dto.request.BaseRequestDTO;
import com.dct.model.dto.response.BaseResponseDTO;
import com.dct.model.exception.BaseBadRequestException;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private static final String ENTITY_NAME = "com.ars.userservice.service.impl.UserServiceImpl";
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
            throw new BaseBadRequestException(ENTITY_NAME, BaseExceptionConstants.ACCOUNT_NOT_EXISTED);
        }

        Users user = userOptional.get();
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        userDTO.setRoles(roleService.getUserRoles(userId));
        return BaseResponseDTO.builder().ok(userDTO);
    }

    @Override
    @Transactional
    public BaseResponseDTO createUser(CreateUserRequestDTO requestDTO) {
        return null;
    }

    @Override
    @Transactional
    public BaseResponseDTO updateUser(UpdateUserRequestDTO requestDTO) {
        return null;
    }

    @Override
    @Transactional
    public BaseResponseDTO deleteUser(Integer userId) {
        return null;
    }

    @Override
    @Transactional
    public BaseResponseDTO changeEmail(ChangeEmailRequestDTO requestDTO) {
        return null;
    }

    @Override
    @Transactional
    public BaseResponseDTO changeStatus(ChangeUserStatusRequestDTO requestDTO) {
        return null;
    }

    @Override
    @Transactional
    public BaseResponseDTO changePassword(ChangePasswordRequestDTO requestDTO) {
        return null;
    }

    @Override
    @Transactional
    public BaseResponseDTO recoverPassword(RecoverPasswordRequestDTO requestDTO) {
        return null;
    }
}
