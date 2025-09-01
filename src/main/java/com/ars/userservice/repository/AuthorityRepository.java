package com.ars.userservice.repository;

import com.ars.userservice.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
    @Query(
        value = """
            SELECT a.code
            FROM ars_user.authority a
            JOIN ars_user.role_authority ra on a.id = ra.authority_id
            JOIN ars_user.user_role ur on ur.role_id = ra.role_id
            WHERE ur.user_id = ?1
        """,
        nativeQuery = true
    )
    Set<String> findAllByUserId(Integer userId);
}
