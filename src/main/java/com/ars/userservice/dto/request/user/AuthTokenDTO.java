package com.ars.userservice.dto.request.user;

import com.dct.model.dto.auth.BaseTokenDTO;

import java.util.HashSet;
import java.util.Set;

public class AuthTokenDTO extends BaseTokenDTO {
    private Integer shopId;
    private String shopName;

    public AuthTokenDTO(
        Integer shopId,
        String shopName,
        Integer userId,
        String username,
        Boolean isRememberMe,
        Set<String> authorities
    ) {
        super(userId, username, isRememberMe, authorities);
        this.shopId = shopId;
        this.shopName = shopName;
    }

    public static TokenBuilder tokenBuilder() {
        return new TokenBuilder();
    }

    public static class TokenBuilder {
        private String shopName;
        private Integer shopId;
        private Integer userId;
        private String username;
        private Boolean isRememberMe = false;
        private Set<String> authorities = new HashSet<>();

        public TokenBuilder username(String username) {
            this.username = username;
            return this;
        }

        public TokenBuilder userId(Integer userId) {
            this.userId = userId;
            return this;
        }

        public TokenBuilder authorities(Set<String> authorities) {
            this.authorities = authorities;
            return this;
        }

        public TokenBuilder rememberMe(boolean rememberMe) {
            this.isRememberMe = rememberMe;
            return this;
        }

        public TokenBuilder shopName(String shopName) {
            this.shopName = shopName;
            return this;
        }

        public TokenBuilder shopId(Integer shopId) {
            this.shopId = shopId;
            return this;
        }

        public AuthTokenDTO build() {
            return new AuthTokenDTO(shopId, shopName, userId, username, isRememberMe, authorities);
        }
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
