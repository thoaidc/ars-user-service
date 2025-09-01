package com.ars.userservice.repository;

import com.ars.userservice.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("unused")
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {}
