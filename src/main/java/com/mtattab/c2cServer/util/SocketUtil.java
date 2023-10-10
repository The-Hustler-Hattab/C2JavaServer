package com.mtattab.c2cServer.util;

import lombok.experimental.UtilityClass;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

@UtilityClass
public class SocketUtil {

    public static void sendMessage(WebSocketSession session, WebSocketMessage message){
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<WebSocketSession> findSessionByIdInSessionSet(Set<WebSocketSession> sessionSet , String sessionId){
        return sessionSet.stream()
                .filter(session -> sessionId.equalsIgnoreCase(session.getId()))
                .findFirst();
    }
}
