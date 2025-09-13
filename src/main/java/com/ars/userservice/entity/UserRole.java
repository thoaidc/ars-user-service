package com.ars.userservice.entity;

import com.dct.config.entity.AbstractAuditingEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "user_role", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "role_id"})})
@SuppressWarnings("unused")
public class UserRole extends AbstractAuditingEntity {
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "role_id", nullable = false)
    private Integer roleId;

    public UserRole() {}

    public UserRole(Integer userId, Integer roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public static class Builder {
        private Integer userId;
        private Integer roleId;

        public Builder userId(Integer userId) {
            this.userId = userId;
            return this;
        }

        public Builder roleId(Integer roleId) {
            this.roleId = roleId;
            return this;
        }

        public UserRole build() {
            UserRole userRole = new UserRole();
            userRole.setUserId(this.userId);
            userRole.setRoleId(this.roleId);
            return userRole;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
