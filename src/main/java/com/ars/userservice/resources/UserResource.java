package com.ars.userservice.resources;

import com.ars.userservice.dto.request.user.ChangeEmailRequestDTO;
import com.ars.userservice.dto.request.user.ChangePasswordRequestDTO;
import com.ars.userservice.dto.request.user.CreateUserRequestDTO;
import com.ars.userservice.dto.request.user.RecoverPasswordRequestDTO;
import com.ars.userservice.dto.request.user.UpdateUserRequestDTO;
import com.ars.userservice.service.UserService;

import com.dct.config.aop.annotation.CheckAuthorize;
import com.dct.model.constants.BaseRoleConstants;
import com.dct.model.dto.request.BaseRequestDTO;
import com.dct.model.dto.response.BaseResponseDTO;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@CheckAuthorize(authorities = BaseRoleConstants.User.USER)
public class UserResource {
    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public BaseResponseDTO getUsersWithPaging(@ModelAttribute BaseRequestDTO request) {
        return userService.getUsersWithPaging(request);
    }

    @GetMapping("/{userId}")
    public BaseResponseDTO getUserDetail(@PathVariable Integer userId) {
        return userService.getUserDetail(userId);
    }

    @PostMapping
    @CheckAuthorize(authorities = BaseRoleConstants.User.CREATE)
    public BaseResponseDTO createUser(@Valid @RequestBody CreateUserRequestDTO requestDTO) {
        return userService.createUser(requestDTO);
    }

    @PutMapping
    @CheckAuthorize(authorities = BaseRoleConstants.User.UPDATE)
    public BaseResponseDTO updateUser(@Valid @RequestBody UpdateUserRequestDTO requestDTO) {
        return userService.updateUser(requestDTO);
    }

    @DeleteMapping("/{userId}")
    @CheckAuthorize(authorities = BaseRoleConstants.User.DELETE)
    public BaseResponseDTO deleteUser(@PathVariable Integer userId) {
        return userService.deleteUser(userId);
    }

    @PutMapping("/password")
    @CheckAuthorize(authorities = BaseRoleConstants.User.UPDATE)
    public BaseResponseDTO changePassword(@Valid @RequestBody ChangePasswordRequestDTO requestDTO) {
        return userService.changePassword(requestDTO);
    }

    @PutMapping("/email")
    @CheckAuthorize(authorities = BaseRoleConstants.User.UPDATE)
    public BaseResponseDTO changeEmail(@Valid @RequestBody ChangeEmailRequestDTO requestDTO) {
        return userService.changeEmail(requestDTO);
    }

    @PostMapping("/recover")
    public BaseResponseDTO recoverPassword(@Valid @RequestBody RecoverPasswordRequestDTO requestDTO) {
        return userService.recoverPassword(requestDTO);
    }
}
