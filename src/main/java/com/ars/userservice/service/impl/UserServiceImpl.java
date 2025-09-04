package com.ars.userservice.service.impl;

import com.ars.userservice.dto.request.user.ChangeEmailRequestDTO;
import com.ars.userservice.dto.request.user.ChangePasswordRequestDTO;
import com.ars.userservice.dto.request.user.ChangeUserStatusRequestDTO;
import com.ars.userservice.dto.request.user.CreateUserRequestDTO;
import com.ars.userservice.dto.request.user.RecoverPasswordRequestDTO;
import com.ars.userservice.dto.request.user.UpdateUserRequestDTO;
import com.ars.userservice.service.UserService;
import com.dct.model.dto.response.BaseResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public BaseResponseDTO createUser(CreateUserRequestDTO requestDTO) {
        return null;
    }

    @Override
    public BaseResponseDTO updateUser(UpdateUserRequestDTO requestDTO) {
        return null;
    }

    @Override
    public BaseResponseDTO deleteUser(Integer userId) {
        return null;
    }

    @Override
    public BaseResponseDTO changeEmail(ChangeEmailRequestDTO requestDTO) {
        return null;
    }

    @Override
    public BaseResponseDTO changeStatus(ChangeUserStatusRequestDTO requestDTO) {
        return null;
    }

    @Override
    public BaseResponseDTO changePassword(ChangePasswordRequestDTO requestDTO) {
        return null;
    }

    @Override
    public BaseResponseDTO recoverPassword(RecoverPasswordRequestDTO requestDTO) {
        return null;
    }
}
