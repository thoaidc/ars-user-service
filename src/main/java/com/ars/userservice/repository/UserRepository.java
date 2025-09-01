package com.ars.userservice.repository;

import com.ars.userservice.dto.mapping.IAuthenticationDTO;
import com.ars.userservice.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
    @Query(
        value = """
            SELECT u.id, u.username, u.password, u.email, u.status
            FROM ars_user.users u
            WHERE u.username = ?1 AND status <> 3
        """,
        nativeQuery = true
    )
    Optional<IAuthenticationDTO> findAuthenticationByUsername(String username);
    boolean existsByUsername(String username);
}
