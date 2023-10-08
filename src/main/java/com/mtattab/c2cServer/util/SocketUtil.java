package com.mtattab.c2cServer.util;

import lombok.experimental.UtilityClass;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@UtilityClass
public class SocketUtil {

    public static void sendMessage(WebSocketSession session, WebSocketMessage message){
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
