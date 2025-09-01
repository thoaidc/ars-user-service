package com.ars.userservice.service;

import com.ars.userservice.dto.request.ChangeEmailRequestDTO;
import com.ars.userservice.dto.request.ChangePasswordRequestDTO;
import com.ars.userservice.dto.request.ChangeUserStatusRequestDTO;
import com.ars.userservice.dto.request.CreateUserRequestDTO;
import com.ars.userservice.dto.request.RecoverPasswordRequestDTO;
import com.ars.userservice.dto.request.UpdateUserRequestDTO;
import com.dct.model.dto.response.BaseResponseDTO;

public interface UserService {
    BaseResponseDTO createUser(CreateUserRequestDTO requestDTO);
    BaseResponseDTO updateUser(UpdateUserRequestDTO requestDTO);
    BaseResponseDTO deleteUser(Integer userId);
    BaseResponseDTO changeEmail(ChangeEmailRequestDTO requestDTO);
    BaseResponseDTO changeStatus(ChangeUserStatusRequestDTO requestDTO);
    BaseResponseDTO changePassword(ChangePasswordRequestDTO requestDTO);
    BaseResponseDTO recoverPassword(RecoverPasswordRequestDTO requestDTO);
}
