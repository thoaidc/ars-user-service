package com.ars.userservice.repository;

import com.ars.userservice.entity.OutBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutBoxRepository extends JpaRepository<OutBox, Integer> {
    @Query(value = "SELECT * FROM outbox WHERE type = ?1 AND status = ?2 ORDER BY id DESC LIMIT 10", nativeQuery = true)
    List<OutBox> findTopOutBoxesByTypeAndStatus(String type, String status);
}
