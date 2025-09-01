package com.ars.userservice.resources;

import com.ars.userservice.dto.request.LoginRequestDTO;
import com.ars.userservice.dto.request.RegisterRequestDTO;
import com.ars.userservice.service.AuthService;
import com.dct.model.dto.response.BaseResponseDTO;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/p/v1/users")
public class AuthResource {
    private final AuthService authService;

    public AuthResource(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public BaseResponseDTO register(@Valid @RequestBody RegisterRequestDTO requestDTO) {
        return authService.register(requestDTO);
    }

    @PostMapping("/shop/register")
    public BaseResponseDTO registerShop(@Valid @RequestBody RegisterRequestDTO requestDTO) {
        return authService.registerShop(requestDTO);
    }

    @PostMapping("/authenticate")
    public BaseResponseDTO authenticate(@Valid @RequestBody LoginRequestDTO requestDTO) {
        return authService.authenticate(requestDTO);
    }

    @PostMapping("/logout")
    public BaseResponseDTO logout() {
        return authService.logout();
    }
}
