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


}
