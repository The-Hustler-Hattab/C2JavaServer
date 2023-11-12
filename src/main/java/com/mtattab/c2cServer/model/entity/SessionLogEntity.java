package com.mtattab.c2cServer.model.entity;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;

    @Column(name = "SESSION_ID", nullable = false)
    private String sessionId;

    @Column(name = "SESSION_REMOTE_ADDRESS", nullable = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String sessionRemoteAddress;

    @Column(name = "SESSION_LOCAL_ADDRESS", nullable = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String sessionLocalAddress;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    @Column(name = "SESSION_CREATED_AT", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Timestamp sessionCreatedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    @Column(name = "SESSION_CLOSED_AT", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonInclude(JsonInclude.Include.NON_NULL)
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

    @Column(name = "PUBLIC_IP", nullable = true)
    private String publicIp;

    @Column(name = "HAS_Files", nullable = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String hasFiles;

    @OneToMany(mappedBy = "sessionLog", fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST, targetEntity = SessionFilesEntity.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<SessionFilesEntity> sessionFiles;

    @Override
    public String toString() {
        return "SessionLogEntity{" +
                "id=" + id +
                ", sessionId='" + sessionId + '\'' +
                ", sessionRemoteAddress='" + sessionRemoteAddress + '\'' +
                ", sessionLocalAddress='" + sessionLocalAddress + '\'' +
                ", sessionCreatedAt=" + sessionCreatedAt +
                ", sessionClosedAt=" + sessionClosedAt +
                ", osName='" + osName + '\'' +
                ", osVersion='" + osVersion + '\'' +
                ", osArch='" + osArch + '\'' +
                ", userName='" + userName + '\'' +
                ", userHome='" + userHome + '\'' +
                ", userCurrentWorkingDir='" + userCurrentWorkingDir + '\'' +
                ", userLanguage='" + userLanguage + '\'' +
                ", hasFiles='" + hasFiles + '\'' +
                '}';
    }
}
