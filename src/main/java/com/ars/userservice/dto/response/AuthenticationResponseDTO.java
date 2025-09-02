package com.ars.userservice.dto.response;

import com.dct.model.constants.BaseDatetimeConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.servlet.http.Cookie;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
public class AuthenticationResponseDTO {
    private Integer userId;
    private String fullname;
    private String email;
    private String phone;
    private String address;
    private String username;
    private boolean isAdmin;
    private String status;
    private String accessToken;
    @JsonIgnore
    private Cookie cookie;
    private String createdBy;
    private String lastModifiedBy;
    private Set<String> authorities = new HashSet<>();

    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = BaseDatetimeConstants.Formatter.DD_MM_YYYY_HH_MM_SS_DASH,
        timezone = BaseDatetimeConstants.ZoneID.DEFAULT
    )
    private Instant createdDate;

    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = BaseDatetimeConstants.Formatter.DD_MM_YYYY_HH_MM_SS_DASH,
        timezone = BaseDatetimeConstants.ZoneID.DEFAULT
    )
    private Instant lastModifiedDate;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Cookie getCookie() {
        return cookie;
    }

    public void setCookie(Cookie cookie) {
        this.cookie = cookie;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
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
}
