package com.ars.userservice.resources;

import com.ars.userservice.dto.request.user.LoginRequestDTO;
import com.ars.userservice.dto.request.user.RegisterRequestDTO;
import com.ars.userservice.dto.response.AuthenticationResponseDTO;
import com.ars.userservice.service.AuthService;
import com.dct.model.dto.response.BaseResponseDTO;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthResource {
    private final AuthService authService;

    public AuthResource(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/v1/users/status")
    public BaseResponseDTO checkUserAuthenticationStatus() {
        return authService.checkAuthenticationStatus();
    }

    @PostMapping("/p/v1/users/register")
    public BaseResponseDTO register(@Valid @RequestBody RegisterRequestDTO requestDTO, @RequestParam boolean isShop) {
        return authService.register(requestDTO, isShop);
    }

    @PostMapping("/p/v1/users/authenticate")
    public BaseResponseDTO authenticate(@Valid @RequestBody LoginRequestDTO requestDTO, HttpServletResponse response) {
        BaseResponseDTO responseDTO = authService.authenticate(requestDTO);
        AuthenticationResponseDTO authenticationResponse = (AuthenticationResponseDTO) responseDTO.getResult();
        Cookie secureCookie = authenticationResponse.getCookie();
        response.addCookie(secureCookie);
        authenticationResponse.setCookie(null);
        responseDTO.setResult(authenticationResponse);
        return responseDTO;
    }

    @PostMapping("/p/v1/users/refresh-token")
    public BaseResponseDTO refreshToken(HttpServletRequest request) {
        return authService.refreshToken(request);
    }

    @PostMapping("/v1/users/logout")
    public BaseResponseDTO logout(HttpServletResponse response) {
        Cookie cookie = authService.resetCookie();
        response.addCookie(cookie);
        return BaseResponseDTO.builder().ok();
    }
}
