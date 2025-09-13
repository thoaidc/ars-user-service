package com.ars.userservice.entity;

import com.dct.config.entity.AbstractAuditingEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "role_authority",
    uniqueConstraints = {@UniqueConstraint(name = "uk_role_authority", columnNames = {"role_id", "authority_id"})}
)
@SuppressWarnings("unused")
public class RoleAuthority extends AbstractAuditingEntity {
    @Column(name = "role_id", nullable = false)
    private Integer roleId;

    @Column(name = "authority_id", nullable = false)
    private Integer authorityId;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Integer authorityId) {
        this.authorityId = authorityId;
    }

    public static class Builder {
        private Integer roleId;
        private Integer authorityId;

        public Builder roleId(Integer roleId) {
            this.roleId = roleId;
            return this;
        }

        public Builder authorityId(Integer authorityId) {
            this.authorityId = authorityId;
            return this;
        }

        public RoleAuthority build() {
            RoleAuthority roleAuthority = new RoleAuthority();
            roleAuthority.setRoleId(this.roleId);
            roleAuthority.setAuthorityId(this.authorityId);
            return roleAuthority;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
