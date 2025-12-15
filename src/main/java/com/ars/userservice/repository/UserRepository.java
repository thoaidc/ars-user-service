package com.ars.userservice.repository;

import com.ars.userservice.dto.mapping.IAuthenticationDTO;
import com.ars.userservice.dto.mapping.IUserDTO;
import com.ars.userservice.dto.mapping.ShopOwnerInfo;
import com.ars.userservice.entity.Users;
import com.dct.model.constants.BaseUserConstants;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
    @Query(
        value = """
            SELECT u.id, u.username, u.fullname, u.email, u.status, u.phone, u.type,
                   u.created_by AS createdBy, u.created_date AS createdDate,
                   u.last_modified_by AS lastModifiedBy, u.last_modified_date AS lastModifiedDate
            FROM ars_user.users u
            WHERE status <> 3
                AND (:fromDate IS NULL OR u.created_date >= :fromDate)
                AND (:toDate IS NULL OR u.created_date <= :toDate)
                AND (:status IS NULL OR u.status = :status)
                AND (:keyword IS NULL OR
                    (u.username LIKE :keyword OR u.email LIKE :keyword)
                )
            ORDER BY u.created_date DESC
        """,
        nativeQuery = true
    )
    Page<IUserDTO> findAllWithPaging(
        @Param("status") Byte status,
        @Param("keyword") String keyword,
        @Param("fromDate") String fromDate,
        @Param("toDate") String toDate,
        Pageable pageable
    );

    @Query(
        value = """
            SELECT u.id,
                   u.username,
                   u.password,
                   u.fullname,
                   u.email,
                   u.phone,
                   u.type,
                   u.status,
                   u.created_by as createdBy,
                   u.last_modified_by as lastModifiedBy,
                   u.created_date as createdDate,
                   u.last_modified_date as lastModifiedDate
            FROM ars_user.users u
            WHERE (u.username = ?1 OR u.email = ?1) AND status <>
        """ + BaseUserConstants.Status.DELETED,
        nativeQuery = true
    )
    Optional<IAuthenticationDTO> findAuthenticationByUsernameOrEmail(String credential);
    boolean existsByUsernameOrEmail(String username, String email);

    @Query(value = "SELECT (COUNT(*) > 0) FROM Users u WHERE (u.username = ?1 OR u.email = ?2) AND u.id <> ?3")
    boolean existsByUsernameOrEmailAndIdNot(String username, String email, Integer id);

    @Modifying
    @Query(value = "UPDATE ars_user.users a SET a.status = ?2 WHERE a.id = ?1", nativeQuery = true)
    void updateUserStatusById(Integer userId, byte status);
    Optional<Users> findByEmail(String email);

    @Query(
        value = """
            SELECT u.id as ownerId, u.fullname as ownerName, u.email as ownerEmail, u.phone as ownerPhone
            FROM ars_user.users u WHERE u.id IN (:ownerIds)
            """,
        nativeQuery = true
    )
    List<ShopOwnerInfo> getShopOwnerInfos(@Param("ownerIds") Iterable<Integer> ownerIds);
}
