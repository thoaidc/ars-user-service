package com.ars.userservice.dto.request;

import com.dct.model.constants.BaseExceptionConstants;
import com.dct.model.constants.BaseRegexConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@SuppressWarnings("unused")
public class RegisterRequestDTO {
    @NotBlank(message = BaseExceptionConstants.USERNAME_NOT_BLANK)
    @Size(min = 2, message = BaseExceptionConstants.USERNAME_MIN_LENGTH)
    @Size(max = 50, message = BaseExceptionConstants.USERNAME_MAX_LENGTH)
    @Pattern(regexp = BaseRegexConstants.USERNAME_PATTERN, message = BaseExceptionConstants.USERNAME_INVALID)
    private String username;

    @NotBlank(message = BaseExceptionConstants.PASSWORD_NOT_BLANK)
    @Size(min = 6, message = BaseExceptionConstants.PASSWORD_MIN_LENGTH)
    @Size(max = 20, message = BaseExceptionConstants.PASSWORD_MAX_LENGTH)
    @Pattern(regexp = BaseRegexConstants.PASSWORD_PATTERN, message = BaseExceptionConstants.PASSWORD_INVALID)
    private String password;

    private String fullname;

    @NotBlank(message = BaseExceptionConstants.EMAIL_NOT_BLANK)
    @Size(min = 6, message = BaseExceptionConstants.EMAIL_MIN_LENGTH)
    @Size(max = 100, message = BaseExceptionConstants.EMAIL_MAX_LENGTH)
    @Pattern(regexp = BaseRegexConstants.EMAIL_PATTERN, message = BaseExceptionConstants.EMAIL_INVALID)
    private String email;

    @Size(min = 8, message = BaseExceptionConstants.PHONE_MIN_LENGTH)
    @Size(max = 10, message = BaseExceptionConstants.PHONE_MAX_LENGTH)
    @Pattern(regexp = BaseRegexConstants.PHONE_PATTERN, message = BaseExceptionConstants.PHONE_INVALID)
    private String phone;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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
}
