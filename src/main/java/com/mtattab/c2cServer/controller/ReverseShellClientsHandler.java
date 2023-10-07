package com.mtattab.c2cServer.controller;

import com.mtattab.c2cServer.service.ReverseShellClientHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Controller
public class ReverseShellClientsHandler extends TextWebSocketHandler {

    @Autowired
    ReverseShellClientHandlerService reverseShellClientHandlerService;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        reverseShellClientHandlerService.addActiveSession(session);
        reverseShellClientHandlerService.handleReverseShellClient(session, message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        reverseShellClientHandlerService.removeActiveSession(session);

    }
}
