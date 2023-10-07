package com.mtattab.c2cServer.model.enums;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Set;
@Slf4j
public enum ActiveSessionsEvents {

    RECEIVED_NEW_CONNECTION("RECEIVED_NEW_CONNECTION", new ActiveSessionEventHandler() {
        @Override
        public void handle(WebSocketSession session, Set<WebSocketSession> sessions) {
            log.info("[+] {} handled",RECEIVED_NEW_CONNECTION.getValue());
//            sessions.add(session);
            sessions.forEach(activeSession-> {
                sendMessage(activeSession,new TextMessage("New connection established "+activeSession));
            });
        }
    }),
    REMOVED_CONNECTION("REMOVED_CONNECTION", new ActiveSessionEventHandler() {
        @Override
        public void handle(WebSocketSession session, Set<WebSocketSession> sessions) {
            log.info("[+] {} handled",REMOVED_CONNECTION.getValue());
            sessions.forEach(activeSession-> {
                sendMessage(activeSession,new TextMessage("Connection closed "+activeSession));
            });
//            sessions.remove(session);
        }
    });

    private String value;

    private ActiveSessionEventHandler eventHandler;

    private ActiveSessionsEvents(String value ,ActiveSessionEventHandler eventHandler) {
        this.value = value;
        this.eventHandler = eventHandler;
    }

    public String getValue() {
        return value;
    }
    public ActiveSessionEventHandler getEventHandler() {
        return eventHandler;
    }




    private static void sendMessage(WebSocketSession session, WebSocketMessage message){
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
