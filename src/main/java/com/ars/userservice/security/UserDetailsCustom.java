package com.ars.userservice.security;

import com.ars.userservice.entity.Users;
import com.dct.model.constants.BaseUserConstants;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class UserDetailsCustom extends User {

    private final Users user;
    private final Set<String> authorities = new HashSet<>();

    private UserDetailsCustom(Users user,
                              Collection<? extends GrantedAuthority> authorities,
                              boolean accountEnabled,
                              boolean accountNonExpired,
                              boolean credentialsNonExpired,
                              boolean accountNonLocked) {
        super(
            user.getUsername(),
            user.getPassword(),
            accountEnabled,
            accountNonExpired,
            credentialsNonExpired,
            accountNonLocked,
            authorities
        );
        this.user= user;
        this.authorities.addAll(authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));
    }

    public Users getUser() {
        return this.user;
    }

    public Set<String> getUserAuthorities() {
        return authorities;
    }

    public static Builder customBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private Users user;
        private Collection<? extends GrantedAuthority> authorities;
        private boolean accountEnabled = true;
        private boolean accountNonExpired = true;
        private boolean accountNonLocked = true;

        public Builder user(Users user) {
            this.user = user;
            return this;
        }

        public Builder status(byte status) {
            switch (status) {
                case BaseUserConstants.Status.ACTIVE -> accountEnabled = true;
                case BaseUserConstants.Status.INACTIVE -> accountEnabled = false;
                case BaseUserConstants.Status.LOCKED -> accountNonLocked = false;
                case BaseUserConstants.Status.DELETED -> accountNonExpired = false;
            }

            return this;
        }

        public Builder authorities(Collection<? extends GrantedAuthority> authorities) {
            this.authorities = authorities;
            return this;
        }

        public UserDetailsCustom build() {
            boolean credentialsNonExpired = true;

            return new UserDetailsCustom(
                user,
                authorities,
                accountEnabled,
                accountNonExpired,
                credentialsNonExpired,
                accountNonLocked
            );
        }
    }
}
