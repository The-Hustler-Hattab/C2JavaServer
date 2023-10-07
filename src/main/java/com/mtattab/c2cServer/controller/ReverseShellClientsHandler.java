package com.mtattab.c2cServer.controller;

import com.mtattab.c2cServer.service.ReverseShellClientHandlerService;
import com.mtattab.c2cServer.service.impl.ReverseShellClientHandlerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Controller
public class ReverseShellClientsHandler extends TextWebSocketHandler {

    @Autowired
    ReverseShellClientHandlerService reverseShellClientHandler;
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        reverseShellClientHandler.addActiveSession(session);
        String clientMessage = message.getPayload();

        // Handle the client's message (e.g., send a response back to the client)
        String responseMessage = "Received your message: " + clientMessage;


        session.sendMessage(new TextMessage(responseMessage));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        reverseShellClientHandler.removeActiveSession(session);

    }
}
