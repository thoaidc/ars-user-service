package com.ars.userservice.dto.mapping;

@SuppressWarnings("unused")
public interface IUserDTO {
    Integer getId();
    String getUsername();
    String getFullname();
    String getEmail();
    String getPhone();
    boolean getIsAdmin();
    byte getStatus();
    String getCreatedBy();
    String getCreatedDate();
    String getLastModifiedBy();
    String getLastModifiedDate();
}
