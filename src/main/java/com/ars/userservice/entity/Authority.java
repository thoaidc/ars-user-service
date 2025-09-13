package com.ars.userservice.entity;

import com.dct.config.entity.AbstractAuditingEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "authority")
@SuppressWarnings("unused")
public class Authority extends AbstractAuditingEntity {
    @Column(name = "parent_id")
    private Integer parentId;

    @Column(name = "parent_code", length = 50)
    private String parentCode;

    @Column(name = "name", nullable = false, columnDefinition = "NVARCHAR(50)")
    private String name;

    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "description", columnDefinition = "NVARCHAR(255)")
    private String description;

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static class Builder {
        private Integer parentId;
        private String parentCode;
        private String name;
        private String code;
        private String description;

        public Builder parentId(Integer parentId) {
            this.parentId = parentId;
            return this;
        }

        public Builder parentCode(String parentCode) {
            this.parentCode = parentCode;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Authority build() {
            Authority authority = new Authority();
            authority.setParentId(this.parentId);
            authority.setParentCode(this.parentCode);
            authority.setName(this.name);
            authority.setCode(this.code);
            authority.setDescription(this.description);
            return authority;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
