package com.ars.userservice.resources;

import com.ars.userservice.dto.request.ChangeEmailRequestDTO;
import com.ars.userservice.dto.request.ChangePasswordRequestDTO;
import com.ars.userservice.dto.request.CreateUserRequestDTO;
import com.ars.userservice.dto.request.RecoverPasswordRequestDTO;
import com.ars.userservice.dto.request.UpdateUserRequestDTO;
import com.ars.userservice.service.UserService;
import com.dct.model.dto.response.BaseResponseDTO;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserResource {
    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public BaseResponseDTO createUser(@Valid @RequestBody CreateUserRequestDTO requestDTO) {
        return userService.createUser(requestDTO);
    }

    @PutMapping
    public BaseResponseDTO updateUser(@Valid @RequestBody UpdateUserRequestDTO requestDTO) {
        return userService.updateUser(requestDTO);
    }

    @DeleteMapping("/{userId}")
    public BaseResponseDTO deleteUser(@PathVariable Integer userId) {
        return userService.deleteUser(userId);
    }

    @PutMapping("/password")
    public BaseResponseDTO changePassword(@Valid @RequestBody ChangePasswordRequestDTO requestDTO) {
        return userService.changePassword(requestDTO);
    }

    @PutMapping("/email")
    public BaseResponseDTO changeEmail(@Valid @RequestBody ChangeEmailRequestDTO requestDTO) {
        return userService.changeEmail(requestDTO);
    }

    @PostMapping("/recover")
    public BaseResponseDTO recoverPassword(@Valid @RequestBody RecoverPasswordRequestDTO requestDTO) {
        return userService.recoverPassword(requestDTO);
    }
}
