package com.ars.userservice.dto.request.user;

import com.dct.model.constants.BaseExceptionConstants;
import com.dct.model.constants.BaseRegexConstants;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

public class UpdateUserRequestDTO {
    @NotNull(message = BaseExceptionConstants.ID_NOT_NULL)
    @Min(value = 1, message = BaseExceptionConstants.ID_INVALID)
    private Integer id;

    @Size(max = 100, message = BaseExceptionConstants.NAME_MAX_LENGTH)
    private String fullname;

    @NotBlank(message = BaseExceptionConstants.USERNAME_NOT_BLANK)
    @Size(min = 2, message = BaseExceptionConstants.USERNAME_MIN_LENGTH)
    @Size(max = 45, message = BaseExceptionConstants.USERNAME_MAX_LENGTH)
    @Pattern(regexp = BaseRegexConstants.USERNAME_PATTERN, message = BaseExceptionConstants.USERNAME_INVALID)
    private String username;

    @NotBlank(message = BaseExceptionConstants.EMAIL_NOT_BLANK)
    @Size(min = 6, message = BaseExceptionConstants.EMAIL_MIN_LENGTH)
    @Size(max = 100, message = BaseExceptionConstants.EMAIL_MAX_LENGTH)
    @Pattern(regexp = BaseRegexConstants.EMAIL_PATTERN, message = BaseExceptionConstants.EMAIL_INVALID)
    private String email;
    private String phone;
    private String address;

    @Size(min = 1, message = BaseExceptionConstants.ROLE_AUTHORITIES_NOT_EMPTY)
    private List<Integer> roleIds = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Integer> roleIds) {
        this.roleIds = roleIds;
    }
}
