package com.ars.userservice.dto.response;

import com.dct.model.dto.response.AuditingDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.servlet.http.Cookie;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
public class AuthenticationResponseDTO extends AuditingDTO {
    private String fullname;
    private String email;
    private String phone;
    private String address;
    private String username;
    private String type;
    private String status;
    private String accessToken;
    @JsonIgnore
    private Cookie cookie;
    private Set<String> authorities = new HashSet<>();

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
