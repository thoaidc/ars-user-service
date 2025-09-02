package com.ars.userservice.resources;

import com.ars.userservice.dto.request.LoginRequestDTO;
import com.ars.userservice.dto.request.RegisterRequestDTO;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthResource {
    private final AuthService authService;

    public AuthResource(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/p/v1/users/register")
    public BaseResponseDTO register(@Valid @RequestBody RegisterRequestDTO requestDTO) {
        return authService.register(requestDTO);
    }

    @PostMapping("/p/v1/users/shop/register")
    public BaseResponseDTO registerShop(@Valid @RequestBody RegisterRequestDTO requestDTO) {
        return authService.registerShop(requestDTO);
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

    @PostMapping("/v1/users/refresh-token")
    public BaseResponseDTO refreshToken(HttpServletRequest request) {
        return authService.refreshToken(request);
    }

    @PostMapping("/v1/users/logout")
    public BaseResponseDTO logout() {
        return authService.logout();
    }
}
