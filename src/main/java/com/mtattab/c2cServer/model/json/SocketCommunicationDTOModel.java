package com.mtattab.c2cServer.model.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.web.socket.WebSocketSession;

import java.util.Objects;

@Data
@SuperBuilder
public class SocketCommunicationDTOModel {

    private String socketId;
    private String socketAddress;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    public SocketCommunicationDTOModel(WebSocketSession session){
        this.socketId= session.getId();
        this.socketAddress= Objects.requireNonNull(session.getRemoteAddress()).toString();
    }

}
