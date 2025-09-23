package com.ars.userservice.dto.mapping;

@SuppressWarnings("unused")
public interface IAuthorityDTO {
    Integer getId();
    Integer getParentId();
    String getParentCode();
    String getName();
    String getCode();
    String getDescription();
}
