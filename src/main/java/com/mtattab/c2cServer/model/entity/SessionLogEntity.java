package com.mtattab.c2cServer.model.entity;

import java.sql.Timestamp;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

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
}
