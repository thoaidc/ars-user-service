package com.ars.userservice.service;

import com.ars.userservice.dto.request.user.LoginRequestDTO;
import com.ars.userservice.dto.request.user.RegisterRequestDTO;
import com.dct.model.dto.response.BaseResponseDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    BaseResponseDTO register(RegisterRequestDTO requestDTO, boolean isShop);
    BaseResponseDTO authenticate(LoginRequestDTO requestDTO);
    BaseResponseDTO refreshToken(HttpServletRequest request);
    BaseResponseDTO logout();
}
