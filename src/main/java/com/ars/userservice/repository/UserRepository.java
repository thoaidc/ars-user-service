package com.ars.userservice.repository;

import com.ars.userservice.dto.mapping.IAuthenticationDTO;
import com.ars.userservice.entity.Users;

import com.dct.model.constants.BaseUserConstants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
    @Query(
        value = """
            SELECT u.id,
                   u.username,
                   u.password,
                   u.fullname,
                   u.email,
                   u.address,
                   u.phone,
                   u.is_admin isAdmin,
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
}
