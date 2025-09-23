package com.ars.userservice.service;

import com.ars.userservice.dto.mapping.OAuth2UserDTO;
import com.ars.userservice.dto.request.user.ChangeEmailRequestDTO;
import com.ars.userservice.dto.request.user.ChangePasswordRequestDTO;
import com.ars.userservice.dto.request.user.ChangeUserStatusRequestDTO;
import com.ars.userservice.dto.request.user.CreateUserRequestDTO;
import com.ars.userservice.dto.request.user.RecoverPasswordRequestDTO;
import com.ars.userservice.dto.request.user.UpdateUserRequestDTO;
import com.dct.model.dto.request.BaseRequestDTO;
import com.dct.model.dto.response.BaseResponseDTO;

public interface UserService {
    BaseResponseDTO getUsersWithPaging(BaseRequestDTO request);
    BaseResponseDTO getUserDetail(Integer userId);
    BaseResponseDTO createUser(CreateUserRequestDTO requestDTO);
    BaseResponseDTO updateUser(UpdateUserRequestDTO requestDTO);
    BaseResponseDTO createOrUpdateUser(OAuth2UserDTO userDTO);
    BaseResponseDTO deleteUser(Integer userId);
    BaseResponseDTO changeEmail(ChangeEmailRequestDTO requestDTO);
    BaseResponseDTO changeStatus(ChangeUserStatusRequestDTO requestDTO);
    BaseResponseDTO changePassword(ChangePasswordRequestDTO requestDTO);
    BaseResponseDTO recoverPassword(RecoverPasswordRequestDTO requestDTO);
}
