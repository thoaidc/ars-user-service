package com.ars.userservice.entity;

import com.dct.config.entity.AbstractAuditingEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles")
@SuppressWarnings("unused")
public class Roles extends AbstractAuditingEntity {
    @Column(name = "name", nullable = false, length = 50, columnDefinition = "NVARCHAR(50)")
    private String name;

    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    @ManyToMany(
        cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH },
        fetch = FetchType.LAZY
    )
    @JoinTable(
        name = "role_authority",
        joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id")
    )
    @JsonIgnore
    private List<Authority> authorities = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    public static class Builder {
        private String name;
        private String code;
        private List<Authority> authorities;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder authorities(List<Authority> authorities) {
            this.authorities = authorities;
            return this;
        }

        public Roles build() {
            Roles role = new Roles();
            role.setName(this.name);
            role.setCode(this.code);
            role.setAuthorities(this.authorities);
            return role;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
