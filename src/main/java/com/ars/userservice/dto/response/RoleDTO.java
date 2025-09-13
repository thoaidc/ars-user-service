package com.ars.userservice.dto.response;

import com.dct.model.dto.response.AuditingDTO;
import java.util.Set;

public class RoleDTO extends AuditingDTO {
    private String name;
    private String code;
    Set<Integer> authorities;

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

    public Set<Integer> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Integer> authorities) {
        this.authorities = authorities;
    }
}
