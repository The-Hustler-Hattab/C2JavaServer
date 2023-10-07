package com.mtattab.c2cServer.service;

import org.springframework.web.socket.WebSocketSession;

public interface ReverseShellClientHandlerService {

    void addActiveSession(WebSocketSession session);

    void removeActiveSession(WebSocketSession session);

}
