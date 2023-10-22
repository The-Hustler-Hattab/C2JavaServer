package com.mtattab.c2cServer.repository;

import com.mtattab.c2cServer.model.entity.SessionFilesEntity;
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
public interface SessionFilesRepository extends JpaRepository<SessionFilesEntity, Long> {


    @Modifying
    @Transactional
    @Query("UPDATE SessionFilesEntity c SET c.updatedAt = :updatedAt, c.fileStatus = :fileStatus " +
            "WHERE c.file = :file")
    int updateFileStatus(
            @Param("updatedAt") Timestamp updatedAt,
            @Param("fileStatus") String fileStatus,
            @Param("file") String file
    );
}
