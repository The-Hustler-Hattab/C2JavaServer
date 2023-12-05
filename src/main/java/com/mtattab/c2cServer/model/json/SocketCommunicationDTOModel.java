package com.mtattab.c2cServer.model.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mtattab.c2cServer.model.entity.SessionLogEntity;
import com.mtattab.c2cServer.util.DataManipulationUtil;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.web.socket.WebSocketSession;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Data
@SuperBuilder
public class SocketCommunicationDTOModel {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String socketId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String socketAddress;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String createdAt;

    public SocketCommunicationDTOModel(WebSocketSession session){
        this.socketId= session.getId();
        this.socketAddress= Objects.requireNonNull(session.getRemoteAddress()).toString();

    }

    public SocketCommunicationDTOModel(SessionLogEntity logEntity,WebSocketSession session){
        this.socketId= session.getId();
        if (logEntity== null )return;

        this.socketAddress= logEntity.getPublicIp() == null ? "" : logEntity.getPublicIp();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd : HH:mm:ss");

        this.createdAt= Objects.requireNonNull(logEntity.getSessionCreatedAt().toLocalDateTime().format(formatter));



    }

}
