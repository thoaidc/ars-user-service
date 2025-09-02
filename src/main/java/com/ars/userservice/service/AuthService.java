package com.ars.userservice.service;

import com.ars.userservice.dto.request.LoginRequestDTO;
import com.ars.userservice.dto.request.RegisterRequestDTO;
import com.dct.model.dto.response.BaseResponseDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    BaseResponseDTO register(RegisterRequestDTO requestDTO);
    BaseResponseDTO registerShop(RegisterRequestDTO requestDTO);
    BaseResponseDTO authenticate(LoginRequestDTO requestDTO);
    BaseResponseDTO refreshToken(HttpServletRequest request);
    BaseResponseDTO logout();
}
