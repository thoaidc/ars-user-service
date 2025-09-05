package com.ars.userservice.dto.request.user;

import com.dct.config.exception.BaseExceptionHandler;
import com.dct.model.constants.BaseExceptionConstants;
import com.dct.model.constants.BaseRegexConstants;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to map with register requests in a manual authenticate flow <p>
 * The @{@link Valid} annotation is used along with @{@link ResponseBody} to validate input data format <p>
 * Annotations like @{@link Pattern}, @{@link NotBlank} will be automatically handled by Spring <p>
 * {@link MethodArgumentNotValidException} will be thrown with the predefined message key
 * if any of the validated fields contain invalid data <p>
 * This exception is configured to be handled by {@link BaseExceptionHandler}.handleMethodArgumentNotValid()
 *
 * @author thoaidc
 */
public class CreateUserRequestDTO {
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

    @NotBlank(message = BaseExceptionConstants.PASSWORD_NOT_BLANK)
    @Size(min = 8, message = BaseExceptionConstants.PASSWORD_MIN_LENGTH)
    @Size(max = 20, message = BaseExceptionConstants.PASSWORD_MAX_LENGTH)
    @Pattern(regexp = BaseRegexConstants.PASSWORD_PATTERN, message = BaseExceptionConstants.PASSWORD_INVALID)
    private String password;

    private String phone;
    private String address;
    private boolean isAdmin;

    @Size(min = 1, message = BaseExceptionConstants.ROLE_AUTHORITIES_NOT_EMPTY)
    private List<Integer> roleIds = new ArrayList<>();

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public List<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Integer> roleIds) {
        this.roleIds = roleIds;
    }
}
