package com.ars.userservice.service;

import com.ars.userservice.dto.request.user.LoginRequestDTO;
import com.ars.userservice.dto.request.user.RegisterRequestDTO;
import com.ars.userservice.dto.request.user.RegisterShopRequestDTO;
import com.ars.userservice.entity.Users;
import com.dct.model.dto.response.BaseResponseDTO;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    BaseResponseDTO checkAuthenticationStatus();
    BaseResponseDTO register(RegisterRequestDTO requestDTO);
    BaseResponseDTO registerShop(RegisterShopRequestDTO requestDTO);
    BaseResponseDTO authenticate(LoginRequestDTO requestDTO);
    BaseResponseDTO authenticate(Users user);
    BaseResponseDTO refreshToken(HttpServletRequest request);
    Cookie resetCookie();
}
