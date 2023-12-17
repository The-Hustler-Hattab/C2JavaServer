package com.mtattab.c2cServer.repository;

import com.mtattab.c2cServer.model.entity.SessionLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;


@RepositoryRestResource(exported = true)
@Repository
public interface SessionLogRepository extends JpaRepository<SessionLogEntity, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE SessionLogEntity c SET c.sessionClosedAt = :sessionClosedAt " +
            "WHERE c.sessionId = :sessionId")
    int updateSessionToClosed(
            @Param("sessionClosedAt") Timestamp sessionClosedAt,
            @Param("sessionId") String sessionId
    );

    @Modifying
    @Transactional
    @Query("UPDATE SessionLogEntity c SET c.hasFiles = :hasFiles " +
            "WHERE c.sessionId = :sessionId")
    int updateSessionToHaveFiles(
            @Param("hasFiles") String hasFiles,
            @Param("sessionId") String sessionId
    );

    @Modifying
    @Transactional
    @Query("UPDATE SessionLogEntity c SET c.aes256HexKey = :aes256HexKey " +
            "WHERE c.sessionId = :sessionId")
    int updateSessionToIncludeEncryptionKey(
            @Param("aes256HexKey") String aes256HexKey,
            @Param("sessionId") String sessionId
    );

    @Query("SELECT c From SessionLogEntity c where c.sessionCreatedAt BETWEEN :startDate and :endDate ")
    List<SessionLogEntity> getLogsBetween2Dates(
            @Param("startDate") Timestamp startDate,
            @Param("endDate") Timestamp endDate
    );


    Optional<SessionLogEntity> findBySessionId(String sessionId);



}
