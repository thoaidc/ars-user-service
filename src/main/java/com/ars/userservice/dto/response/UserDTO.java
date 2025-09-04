package com.ars.userservice.dto.response;

import com.ars.userservice.dto.mapping.IRoleDTO;

import java.util.ArrayList;
import java.util.List;

public class UserDTO {
    private Integer id;
    private String fullname;
    private String username;
    private String email;
    private String phone;
    private String address;
    private String status;
    private boolean isAdmin;
    private List<IRoleDTO> roles = new ArrayList<>();

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
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
