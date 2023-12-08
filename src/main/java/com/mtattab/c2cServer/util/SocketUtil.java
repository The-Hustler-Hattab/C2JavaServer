package com.mtattab.c2cServer.util;

import com.mtattab.c2cServer.model.json.SocketCommunicationDTOModel;
import com.mtattab.c2cServer.model.json.shell.ManagerCommunicationModel;
import lombok.experimental.UtilityClass;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@UtilityClass
public class SocketUtil {

    public static void sendMessage(WebSocketSession session, WebSocketMessage message){
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean keepAliveHandle(TextMessage message){
        String clientMessage = message.getPayload();
        return clientMessage.equalsIgnoreCase("KEEP_ALIVE");
    }

    public Optional<WebSocketSession> findSessionByIdInSessionSet(Set<WebSocketSession> sessionSet , String sessionId){
        return sessionSet.stream()
                .filter(session -> sessionId.equalsIgnoreCase(session.getId()))
                .findFirst();
    }

    public Optional<WebSocketSession> findSessionBySessionNumber( Set<SocketCommunicationDTOModel> socketCommunicationDTOModelSet
            , Integer sessionNumber){

        Optional<SocketCommunicationDTOModel> sessionModel = socketCommunicationDTOModelSet.stream().filter(sessions-> sessions.getSessionNumber().equals(sessionNumber)).findFirst();

        if (sessionModel.isEmpty()){
            return Optional.empty();
        }
        String sessionId = sessionModel.get().getSocketId();



        return ConnectionManager.activeReverseShellSessions.stream()
                .filter(session -> sessionId.equalsIgnoreCase(session.getId()))
                .findFirst();
    }

    public void getSessionByCommandArgs(Optional<WebSocketSession> targetSessionToBeKilled, List<String> args){
        if (targetSessionToBeKilled.isEmpty()){
            targetSessionToBeKilled = SocketUtil.findSessionBySessionNumber(ConnectionManager.activeReverseShellSessionsDTO,
                    DataManipulationUtil.parseStringToInteger(args.get(1)));

        }




    }

    public void closeSession(WebSocketSession targetSessionToBeKilled, WebSocketSession currentSocket ){
        try {
            targetSessionToBeKilled.close(CloseStatus.NORMAL);
            SocketUtil.sendMessage(currentSocket, new TextMessage(DataManipulationUtil.convertObjectToJson(ManagerCommunicationModel.builder()
                    .msg(String.format("session '%s' killed successfuly",targetSessionToBeKilled.getId()))
                            .success(true)
                    .build()
            )));
        }catch (Exception e){
            SocketUtil.sendMessage(currentSocket, new TextMessage(DataManipulationUtil.convertObjectToJson(ManagerCommunicationModel.builder()
                    .msg(String.format("Exception while closing session '%s' : %s",targetSessionToBeKilled.getId(),e.getMessage() ))
                            .success(false)
                    .build()
            )));
        }
    }
}
