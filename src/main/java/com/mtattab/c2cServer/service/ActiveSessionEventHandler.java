package com.mtattab.c2cServer.service;

import com.mtattab.c2cServer.model.json.MessageEventModel;
import org.springframework.web.socket.WebSocketSession;

import java.util.Set;

public interface ActiveSessionEventHandler {

    void handle(MessageEventModel messageEventModel, Set<WebSocketSession> sessions);
}
