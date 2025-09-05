package com.ars.userservice.dto.request.user;

import com.dct.model.constants.BaseExceptionConstants;
import com.dct.model.constants.BaseRegexConstants;
import com.dct.model.constants.BaseUserConstants;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Optional;

public class ChangeUserStatusRequestDTO {
    @NotNull(message = BaseExceptionConstants.ID_NOT_NULL)
    @Min(value = 1, message = BaseExceptionConstants.ID_INVALID)
    private Integer id;

    @NotBlank(message = BaseExceptionConstants.STATUS_NOT_BLANK)
    @Pattern(regexp = BaseRegexConstants.USER_STATUS_PATTERN, message = BaseExceptionConstants.STATUS_INVALID)
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public byte getStatus() {
        return Optional.ofNullable(BaseUserConstants.Status.fromString(status)).orElse(BaseUserConstants.Status.INACTIVE);
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
