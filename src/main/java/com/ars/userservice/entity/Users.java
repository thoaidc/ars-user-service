package com.ars.userservice.entity;

import com.dct.config.entity.AbstractAuditingEntity;
import com.dct.model.constants.BaseUserConstants;
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
@Table(name = "users")
@SuppressWarnings("unused")
public class Users extends AbstractAuditingEntity {
    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "fullname", nullable = false, length = 100, columnDefinition = "NVARCHAR(100)")
    private String fullname;

    @Column(name = "normalized_name", length = 50)
    private String normalizedName;

    @Column(name = "email", length = 100, unique = true)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "type", length = 20, nullable = false)
    private String type;

    /**
     * 0: Inactive <p>
     * 1: Active <p>
     * 2: Locked <p>
     * 3: Deleted
     */
    @Column(name = "status", nullable = false, columnDefinition = "TINYINT UNSIGNED DEFAULT 1")
    private byte status = BaseUserConstants.Status.ACTIVE;

    /**
     * 1 = admin <p>
     * 0 = user
     */
    @Column(name = "is_admin", nullable = false, columnDefinition = "TINYINT(1) DEFAULT FALSE")
    private boolean isAdmin = false;

    @ManyToMany(
        cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH },
        fetch = FetchType.LAZY
    )
    @JoinTable(
        name = "user_role",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private List<Roles> roles = new ArrayList<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getNormalizedName() {
        return normalizedName;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Roles> getRoles() {
        return roles;
    }

    public void setRoles(List<Roles> roles) {
        this.roles = roles;
    }

    public static class Builder {
        private String username;
        private String password;
        private String fullname;
        private String normalizedName;
        private String email;
        private String phone;
        private String type;
        private byte status = BaseUserConstants.Status.ACTIVE;
        private boolean isAdmin = false;
        private List<Roles> roles = new ArrayList<>();

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder fullname(String fullname) {
            this.fullname = fullname;
            return this;
        }

        public Builder normalizedName(String normalizedName) {
            this.normalizedName = normalizedName;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder status(byte status) {
            this.status = status;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder isAdmin(boolean isAdmin) {
            this.isAdmin = isAdmin;
            return this;
        }

        public Builder roles(List<Roles> roles) {
            this.roles = roles;
            return this;
        }

        public Users build() {
            Users user = new Users();
            user.setUsername(this.username);
            user.setPassword(this.password);
            user.setFullname(this.fullname);
            user.setNormalizedName(this.normalizedName);
            user.setEmail(this.email);
            user.setPhone(this.phone);
            user.setStatus(this.status);
            user.setIsAdmin(this.isAdmin);
            user.setType(this.type);
            user.setRoles(this.roles);
            return user;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
