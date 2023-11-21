package com.mtattab.c2cServer.service.commands.manager;

import com.mtattab.c2cServer.model.json.ManagerCommunicationModel;
import com.mtattab.c2cServer.model.json.SocketCommunicationDTOModel;
import com.mtattab.c2cServer.model.enums.commands.ManagerCommands;
import com.mtattab.c2cServer.service.Command;
import com.mtattab.c2cServer.service.observable.ActiveSessionsObservable;
import com.mtattab.c2cServer.util.ConnectionManager;
import com.mtattab.c2cServer.util.DataManipulationUtil;
import com.mtattab.c2cServer.util.SocketUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SessionsCommand implements Command {


    @Override
    public void execute(List<String> args, WebSocketSession currentSocket) {
//        List<SocketCommunicationDTOModel> activeReverseShellSessions =  ConnectionManager.activeReverseShellSessions.stream()
//                .map(SocketCommunicationDTOModel::new).collect(Collectors.toList());



        if (ConnectionManager.activeReverseShellSessionsDTO.isEmpty()){
            SocketUtil.sendMessage(currentSocket, new TextMessage(
                    DataManipulationUtil.convertObjectToJson(ManagerCommunicationModel.builder()
                            .msg("No session found")
                            .success(true)
                            .build()

                    )));
        }else {
            List<SocketCommunicationDTOModel> socketCommunicationDTOModelList = ConnectionManager.activeReverseShellSessionsDTO.stream()
                    .sorted(Comparator.nullsLast(Comparator.comparing(SocketCommunicationDTOModel::getCreatedAt)))
                    .toList();

            SocketUtil.sendMessage(currentSocket, new TextMessage(
                    DataManipulationUtil.convertObjectToJson(ManagerCommunicationModel.builder()
                            .webSocketSessionSet(socketCommunicationDTOModelList)
                            .msg("NOTE: You can use '"+ ManagerCommands.CONNECT_TO_ACTIVE_SESSION.getExample()+"' to connect to the session")
                            .success(true)
                            .build()

                    )));
        }


    }
}
