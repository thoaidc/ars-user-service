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

    @Column(name = "role_code", nullable = false, length = 50)
    private String roleCode;

    @Column(name = "authority_code", nullable = false, length = 50)
    private String authorityCode;

    @Column(name = "authority_parent_code", length = 50)
    private String authorityParentCode;

    // ===== GETTER/SETTER =====
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

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getAuthorityCode() {
        return authorityCode;
    }

    public void setAuthorityCode(String authorityCode) {
        this.authorityCode = authorityCode;
    }

    public String getAuthorityParentCode() {
        return authorityParentCode;
    }

    public void setAuthorityParentCode(String authorityParentCode) {
        this.authorityParentCode = authorityParentCode;
    }

    // ===== BUILDER =====
    public static class Builder {
        private Integer roleId;
        private Integer authorityId;
        private String roleCode;
        private String authorityCode;
        private String authorityParentCode;

        public Builder roleId(Integer roleId) {
            this.roleId = roleId;
            return this;
        }

        public Builder authorityId(Integer authorityId) {
            this.authorityId = authorityId;
            return this;
        }

        public Builder roleCode(String roleCode) {
            this.roleCode = roleCode;
            return this;
        }

        public Builder authorityCode(String authorityCode) {
            this.authorityCode = authorityCode;
            return this;
        }

        public Builder authorityParentCode(String authorityParentCode) {
            this.authorityParentCode = authorityParentCode;
            return this;
        }

        public RoleAuthority build() {
            RoleAuthority roleAuthority = new RoleAuthority();
            roleAuthority.setRoleId(this.roleId);
            roleAuthority.setRoleCode(this.roleCode);
            roleAuthority.setAuthorityId(this.authorityId);
            roleAuthority.setAuthorityCode(this.authorityCode);
            roleAuthority.setAuthorityParentCode(this.authorityParentCode);
            return roleAuthority;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
