package com.ars.userservice.repository;

import com.ars.userservice.dto.mapping.IRoleDTO;
import com.ars.userservice.entity.Roles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Integer> {
    @Query(
        value = """
            SELECT r.id, r.name, r.code
            FROM ars_user.roles r
            JOIN ars_user.user_role ur on r.id = ur.role_id
            WHERE ur.user_id = ?1
        """,
        nativeQuery = true
    )
    List<IRoleDTO> findAllByUserId(Long userId);

    @Query(
        value = """
            SELECT r.id, r.name, r.code FROM ars_user.roles r
            WHERE :keyword IS NULL OR (r.code LIKE :keyword OR r.name LIKE :keyword)
        """,
        nativeQuery = true
    )
    Page<IRoleDTO> findAllWithPaging(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "SELECT r.id, r.name, r.code FROM ars_user.roles r WHERE r.id IN (?1)", nativeQuery = true)
    List<IRoleDTO> findAllByIds(Iterable<Long> roleIds);

    @Query(value = "SELECT r.id, r.name, r.code FROM ars_user.roles r WHERE r.id = ?1", nativeQuery = true)
    Optional<IRoleDTO> findIRoleById(Long roleId);

    Optional<Roles> findRoleByCode(String code);
    boolean existsByCodeOrName(String code, String name);
    boolean existsByCodeOrNameAndIdNot(String code, String name, Integer id);
}
