package com.mtattab.c2cServer.model.enums;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtattab.c2cServer.model.SocketCommunicationDTOModel;
import com.mtattab.c2cServer.service.ActiveSessionEventHandler;
import com.mtattab.c2cServer.util.DataManipulationUtil;
import com.mtattab.c2cServer.util.SocketUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;
@Slf4j
@Getter
public enum ActiveSessionsEvents {
    RECEIVED_NEW_CONNECTION("RECEIVED_NEW_CONNECTION", new ActiveSessionEventHandler() {
        @Override
        public void handle(WebSocketSession session, Set<WebSocketSession> sessions) {
            sessions.forEach(activeSession-> {
                SocketUtil.sendMessage(activeSession,new TextMessage(constructJsonMessage(activeSession,RECEIVED_NEW_CONNECTION.getValue())));
            });
            log.info("[+] {} handled",RECEIVED_NEW_CONNECTION.getValue());

        }
    }),
    LOST_CONNECTION("LOST_CONNECTION", new ActiveSessionEventHandler() {
        @Override
        public void handle(WebSocketSession session, Set<WebSocketSession> sessions) {
            sessions.forEach(activeSession-> {
                SocketUtil.sendMessage(activeSession,new TextMessage(constructJsonMessage(activeSession, LOST_CONNECTION.getValue())));
            });
            log.info("[+] {} handled", LOST_CONNECTION.getValue());

        }
    });

    private String value;

    private ActiveSessionEventHandler eventHandler;


    private ActiveSessionsEvents(String value ,ActiveSessionEventHandler eventHandler) {
        this.value = value;
        this.eventHandler = eventHandler;
    }


    private static String constructJsonMessage(WebSocketSession session, String message){
        return DataManipulationUtil.convertObjectToJson(SocketCommunicationDTOModel.builder()
                    .socketId(session.getId())
                    .socketAddress(Objects.requireNonNull(session.getRemoteAddress()).toString())
                    .message(message)
                    .build()
            );

    }



}
