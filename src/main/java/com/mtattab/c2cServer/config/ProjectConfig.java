package com.mtattab.c2cServer.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Configuration
public class ProjectConfig {


    @Bean("serverActiveSockets")
    public Set<WebSocketSession> serverActiveSockets(){
        log.info("[+] Creating Active Socket List bean");
        return new HashSet<>();
    }
}
