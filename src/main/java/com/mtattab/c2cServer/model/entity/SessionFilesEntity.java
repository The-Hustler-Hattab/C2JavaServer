package com.mtattab.c2cServer.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "session_files")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SessionFilesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "FILE", nullable = false)
    private String file;

    @Column(name = "FILE_STATUS", nullable = false)
    private String fileStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    @Column(name = "CREATED_AT", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    @Column(name = "UPDATED_AT", nullable = true)
    private Timestamp updatedAt;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "session_logs_ID", referencedColumnName = "id", nullable = true)
    private SessionLogEntity sessionLog;

}
