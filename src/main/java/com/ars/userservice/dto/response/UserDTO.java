package com.ars.userservice.dto.response;

import com.ars.userservice.dto.mapping.IRoleDTO;
import com.dct.model.constants.BaseUserConstants;
import com.dct.model.dto.response.AuditingDTO;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class UserDTO extends AuditingDTO {
    private String fullname;
    private String username;
    private String email;
    private String phone;
    private String address;
    private String status;
    private String type;
    private List<IRoleDTO> roles = new ArrayList<>();

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

    public String getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = BaseUserConstants.Status.toString(status);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<IRoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(List<IRoleDTO> roles) {
        this.roles = roles;
    }
}
