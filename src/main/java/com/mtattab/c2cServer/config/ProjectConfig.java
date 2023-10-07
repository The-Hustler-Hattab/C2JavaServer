package com.mtattab.c2cServer.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

import static com.mtattab.c2cServer.util.Constants.ACTIVE_SESSIONS_MANAGER_BEAN_QUALIFIER;
import static com.mtattab.c2cServer.util.Constants.ACTIVE_SESSIONS_REVERSE_SHELL_BEAN_QUALIFIER;

@Slf4j
@Configuration
public class ProjectConfig {


    @Bean
    @Qualifier(ACTIVE_SESSIONS_REVERSE_SHELL_BEAN_QUALIFIER)
    public Set<WebSocketSession> reverseShellSessions(){
        log.info("[+] Creating Reverse Shell Socket List bean");
        return new HashSet<>();
    }

    @Bean
    @Qualifier(ACTIVE_SESSIONS_MANAGER_BEAN_QUALIFIER)
    public Set<WebSocketSession> managerSessions(){
        log.info("[+] Creating Manager Socket List bean");
        return new HashSet<>();
    }
}
