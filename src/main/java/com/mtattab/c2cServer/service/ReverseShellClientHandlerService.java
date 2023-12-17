package com.mtattab.c2cServer.service;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public interface ReverseShellClientHandlerService {

    void addActiveSession(WebSocketSession session, TextMessage message);

    void removeActiveSession(WebSocketSession session);
    void handleReverseShellClient(WebSocketSession session, TextMessage message) ;

    void sendIntialMessage(WebSocketSession session);
}
