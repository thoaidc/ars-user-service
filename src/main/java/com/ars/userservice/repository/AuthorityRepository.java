package com.ars.userservice.repository;

import com.ars.userservice.dto.mapping.IAuthorityDTO;
import com.ars.userservice.entity.Authority;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    @Query(
        value = """
            SELECT p.id, p.name, p.code, p.parent_id as parentId, p.parent_code as parentCode, p.description
            FROM ars_user.authority p
            ORDER BY p.code;
        """,
        nativeQuery = true
    )
    List<IAuthorityDTO> findAllByOrderByCodeAsc();

    @Query(
        value = """
            SELECT p.id
            FROM ars_user.authority p
            JOIN ars_user.role_authority rp on p.id = rp.authority_id
            WHERE rp.role_id = ?1
        """,
        nativeQuery = true
    )
    Set<Integer> findAllByRoleId(Integer roleId);

    @Query(value = "SELECT p.id, p.name, p.code FROM ars_user.authority p WHERE p.id in (?1);", nativeQuery = true)
    List<IAuthorityDTO> findAllByIds(Iterable<Integer> authorityIds);
}
