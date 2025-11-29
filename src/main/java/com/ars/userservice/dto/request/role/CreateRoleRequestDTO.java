package com.ars.userservice.dto.request.role;

import com.dct.model.constants.BaseExceptionConstants;
import com.dct.model.dto.request.BaseRequestDTO;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

public class CreateRoleRequestDTO extends BaseRequestDTO {
    @NotBlank(message = BaseExceptionConstants.NAME_NOT_BLANK)
    private String name;

    @NotBlank(message = BaseExceptionConstants.CODE_NOT_BLANK)
    private String code;
    private List<Integer> authorityIds = new ArrayList<>();

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
