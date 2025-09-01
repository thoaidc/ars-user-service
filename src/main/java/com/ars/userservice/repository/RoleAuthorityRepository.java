package com.ars.userservice.repository;

import com.ars.userservice.entity.RoleAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("unused")
public interface RoleAuthorityRepository extends JpaRepository<RoleAuthority, Integer> {}
