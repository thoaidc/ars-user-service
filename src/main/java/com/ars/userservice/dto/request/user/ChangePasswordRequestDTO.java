package com.ars.userservice.dto.request.user;

import com.dct.model.constants.BaseExceptionConstants;
import com.dct.model.constants.BaseRegexConstants;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ChangePasswordRequestDTO {
    @NotNull(message = BaseExceptionConstants.ID_NOT_NULL)
    @Min(value = 1, message = BaseExceptionConstants.ID_INVALID)
    private Integer id;

    @NotBlank(message = BaseExceptionConstants.PASSWORD_NOT_BLANK)
    @Size(min = 8, message = BaseExceptionConstants.PASSWORD_MIN_LENGTH)
    @Size(max = 20, message = BaseExceptionConstants.PASSWORD_MAX_LENGTH)
    @Pattern(regexp = BaseRegexConstants.PASSWORD_PATTERN, message = BaseExceptionConstants.PASSWORD_INVALID)
    private String oldPassword;

    @NotBlank(message = BaseExceptionConstants.PASSWORD_NOT_BLANK)
    @Size(min = 8, message = BaseExceptionConstants.PASSWORD_MIN_LENGTH)
    @Size(max = 20, message = BaseExceptionConstants.PASSWORD_MAX_LENGTH)
    @Pattern(regexp = BaseRegexConstants.PASSWORD_PATTERN, message = BaseExceptionConstants.PASSWORD_INVALID)
    private String newPassword;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
