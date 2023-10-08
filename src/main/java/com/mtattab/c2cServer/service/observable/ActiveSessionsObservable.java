package com.mtattab.c2cServer.service.observable;

import com.mtattab.c2cServer.model.MessageEventModel;
import com.mtattab.c2cServer.model.enums.ActiveSessionsEvents;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Set;

import static com.mtattab.c2cServer.util.Constants.ACTIVE_SESSIONS_MANAGER_BEAN_QUALIFIER;
import static com.mtattab.c2cServer.util.Constants.ACTIVE_SESSIONS_REVERSE_SHELL_BEAN_QUALIFIER;

@Component
@Data
@Slf4j
public class ActiveSessionsObservable  implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    @Qualifier(ACTIVE_SESSIONS_REVERSE_SHELL_BEAN_QUALIFIER)
    Set<WebSocketSession> activeReverseShellSessions;

    @Autowired
    @Qualifier(ACTIVE_SESSIONS_MANAGER_BEAN_QUALIFIER)
    Set<WebSocketSession> activeMangerSessions;

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

        log.info("[+] Session Closed {}",session);


    }

    public void addManagerSession(WebSocketSession session){
        activeMangerSessions.add(session);
    }
    public void removeManagerSession(WebSocketSession session){
        activeMangerSessions.remove(session);

    }





}
