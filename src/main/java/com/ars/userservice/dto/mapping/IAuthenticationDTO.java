package com.ars.userservice.dto.mapping;

public interface IAuthenticationDTO {
    Integer getId();
    String getUsername();
    String getPassword();
    String getEmail();
    byte getStatus();
}
