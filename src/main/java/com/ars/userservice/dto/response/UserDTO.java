package com.ars.userservice.dto.response;

import com.ars.userservice.dto.mapping.IRoleDTO;
import com.dct.model.constants.BaseDatetimeConstants;
import com.dct.model.constants.BaseUserConstants;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class UserDTO {
    private Integer id;
    private String fullname;
    private String username;
    private String email;
    private String phone;
    private String address;
    private String status;
    private boolean isAdmin;
    private String createdBy;

    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = BaseDatetimeConstants.Formatter.DD_MM_YYYY_HH_MM_SS_DASH,
        timezone = BaseDatetimeConstants.ZoneID.DEFAULT
    )
    private Instant createdDate;
    private String lastModifiedBy;

    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = BaseDatetimeConstants.Formatter.DD_MM_YYYY_HH_MM_SS_DASH,
        timezone = BaseDatetimeConstants.ZoneID.DEFAULT
    )
    private Instant lastModifiedDate;
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

    public void setStatus(Byte status) {
        this.status = BaseUserConstants.Status.toString(status);
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
