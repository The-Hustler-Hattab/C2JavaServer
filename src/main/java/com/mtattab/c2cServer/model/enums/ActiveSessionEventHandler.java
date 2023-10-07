package com.mtattab.c2cServer.model.enums;

import org.springframework.web.socket.WebSocketSession;

import java.util.Set;

public interface ActiveSessionEventHandler {

    void handle(WebSocketSession session, Set<WebSocketSession> Sessions);
}
