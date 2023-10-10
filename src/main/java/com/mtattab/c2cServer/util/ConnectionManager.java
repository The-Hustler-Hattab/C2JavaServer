package com.mtattab.c2cServer.util;

import lombok.experimental.UtilityClass;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;

@UtilityClass
public class ConnectionManager {
    public static HashMap<WebSocketSession, WebSocketSession > connectedManagerToReverseSessions= new HashMap<>();

    public static HashMap<WebSocketSession, WebSocketSession > connectedReverseToManagerSessions= new HashMap<>();


}
