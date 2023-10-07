package com.mtattab.c2cServer.service.impl;

import com.mtattab.c2cServer.model.entity.SessionLogEntity;
import com.mtattab.c2cServer.repository.SessionLogRepository;
import com.mtattab.c2cServer.service.ReverseShellClientHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketSession;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;

@Service
@Slf4j
public class ReverseShellClientHandlerServiceImpl implements ReverseShellClientHandlerService {
    @Autowired
    Set<WebSocketSession> activeSessions;

    @Autowired
    SessionLogRepository sessionLogRepository;

    public void addActiveSession(WebSocketSession session){
        if (!activeSessions.contains(session)){
            activeSessions.add(session);
            logSessionInDb(session);
        }
    }

    public void removeActiveSession(WebSocketSession session){
        logSessionOutInDb(session);
        activeSessions.remove(session);
    }

    private void logSessionInDb(WebSocketSession session){
        try {
            sessionLogRepository.save(SessionLogEntity.builder()
                    .sessionId(session.getId())
                    .sessionLocalAddress(session.getLocalAddress().toString())
                    .sessionRemoteAddress(session.getRemoteAddress().toString())
                    .sessionCreatedAt(Timestamp.valueOf(LocalDateTime.now()))
                    .build());
        }catch (Exception e){
            log.error("[-] exception occurred while saving session {}",e.getMessage());
            e.printStackTrace();
        }
    }
    private void logSessionOutInDb(WebSocketSession session){
        System.out.println(session.getId());
        try {
            sessionLogRepository.updateSessionToClosed(Timestamp.valueOf(LocalDateTime.now()), session.getId());
        }catch (Exception e){
            log.error("[-] exception occurred while removing session {}",e.getMessage());
            e.printStackTrace();
        }
    }

    public Set<WebSocketSession> listActiveConnections(){
        System.out.println(activeSessions);
        return activeSessions;
    }


    public void handleReverseShellClient(){


    }


}
