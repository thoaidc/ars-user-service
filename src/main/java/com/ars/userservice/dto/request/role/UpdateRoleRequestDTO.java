package com.ars.userservice.dto.request.role;

import com.dct.model.constants.BaseExceptionConstants;
import com.dct.model.dto.request.BaseRequestDTO;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

public class UpdateRoleRequestDTO extends BaseRequestDTO {
    @NotNull(message = BaseExceptionConstants.ID_NOT_NULL)
    @Min(value = 1, message = BaseExceptionConstants.ID_INVALID)
    private Integer id;

    @NotBlank(message = BaseExceptionConstants.NAME_NOT_BLANK)
    @Size(max = 100, message = BaseExceptionConstants.NAME_MAX_LENGTH)
    private String name;

    @NotBlank(message = BaseExceptionConstants.CODE_NOT_BLANK)
    private String code;

    @Size(min = 1, message = BaseExceptionConstants.ROLE_AUTHORITIES_NOT_EMPTY)
    private List<Integer> authorityIds = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Integer> getAuthorityIds() {
        return authorityIds;
    }

    public void setAuthorityIds(List<Integer> authorityIds) {
        this.authorityIds = authorityIds;
    }
}
