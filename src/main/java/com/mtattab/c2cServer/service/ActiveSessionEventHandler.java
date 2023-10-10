package com.mtattab.c2cServer.service;

import com.mtattab.c2cServer.model.MessageEventModel;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Set;

public interface ActiveSessionEventHandler {

    void handle(MessageEventModel messageEventModel, Set<WebSocketSession> sessions, HashMap<WebSocketSession , WebSocketSession > sessionsToBeConnected);
}
