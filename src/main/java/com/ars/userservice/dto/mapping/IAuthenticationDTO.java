package com.ars.userservice.dto.mapping;

import java.time.Instant;

@SuppressWarnings("unused")
public interface IAuthenticationDTO {
    Integer getId();
    String getUsername();
    String getPassword();
    String getFullname();
    String getEmail();
    String getPhone();
    String getType();
    byte getStatus();
    String getCreatedBy();
    String getLastModifiedBy();
    Instant getCreatedDate();
    Instant getLastModifiedDate();
}
