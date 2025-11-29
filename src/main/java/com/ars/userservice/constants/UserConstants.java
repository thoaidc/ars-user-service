package com.ars.userservice.constants;

public interface UserConstants {
    int DEFAULT_PASSWORD_LENGTH = 8;
    String REFRESH_TOKEN_API = "/api/p/v1/users/refresh-token";

    interface Type {
        String USER = "USER";
        String ADMIN = "ADMIN";
        String SHOP = "SHOP";
    }
}
