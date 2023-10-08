package com.mtattab.c2cServer.service;

import org.springframework.web.socket.WebSocketSession;

public interface ManagerCommandHandler {


    void handleCommand(String userInput, WebSocketSession currentMangerSocket);
}
