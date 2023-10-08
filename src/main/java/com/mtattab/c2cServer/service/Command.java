package com.mtattab.c2cServer.service;

import org.springframework.web.socket.WebSocketSession;

import java.util.List;

public interface Command {

    void execute(List<String> args, WebSocketSession currentSocket);
}
