package com.mtattab.c2cServer.controller;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class WebSocketHandler extends TextWebSocketHandler {

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String clientMessage = message.getPayload();
        // Handle the client's message (e.g., send a response back to the client)
        String responseMessage = "Received your message: " + clientMessage;
        System.out.println(responseMessage);

        session.sendMessage(new TextMessage(responseMessage));
    }
}
