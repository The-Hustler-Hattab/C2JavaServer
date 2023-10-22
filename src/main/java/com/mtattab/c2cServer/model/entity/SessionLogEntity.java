package com.mtattab.c2cServer.model.entity;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "SESSION_LOGS")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SessionLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "SESSION_ID", nullable = false)
    private String sessionId;

    @Column(name = "SESSION_REMOTE_ADDRESS", nullable = false)
    private String sessionRemoteAddress;

    @Column(name = "SESSION_LOCAL_ADDRESS", nullable = false)
    private String sessionLocalAddress;

    @Column(name = "SESSION_CREATED_AT", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp sessionCreatedAt;

    @Column(name = "SESSION_CLOSED_AT", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp sessionClosedAt;

    @Column(name = "OS_NAME", nullable = true)
    private String osName;

    @Column(name = "OS_VERSION", nullable = true)
    private String osVersion;

    @Column(name = "OS_ARCH", nullable = true)
    private String osArch;

    @Column(name = "USERNAME", nullable = true)
    private String userName;

    @Column(name = "USER_HOME", nullable = true)
    private String userHome;

    @Column(name = "USER_CURRENT_WORKING_DIR", nullable = true)
    private String userCurrentWorkingDir;

    @Column(name = "USER_LANGUAGE", nullable = true)
    private String userLanguage;


    @OneToMany(mappedBy = "sessionLog", fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST, targetEntity = SessionFilesEntity.class)
    private List<SessionFilesEntity> sessionFiles;



}
