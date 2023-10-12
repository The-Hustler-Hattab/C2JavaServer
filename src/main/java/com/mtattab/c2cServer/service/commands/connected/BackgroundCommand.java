package com.mtattab.c2cServer.service.commands.connected;

import com.mtattab.c2cServer.model.ManagerCommunicationModel;
import com.mtattab.c2cServer.service.Command;
import com.mtattab.c2cServer.util.ConnectionManager;
import com.mtattab.c2cServer.util.DataManipulationUtil;
import com.mtattab.c2cServer.util.SocketUtil;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

public class BackgroundCommand implements Command {
    @Override
    public void execute(List<String> args, WebSocketSession currentSocket) {
        ConnectionManager.disconnectConnection(currentSocket);
        SocketUtil.sendMessage(currentSocket, new TextMessage(DataManipulationUtil.convertObjectToJson(ManagerCommunicationModel.builder()
                .msg(String.format("You have returned to the manager screen successfuly" ))
                .success(true)
                .build()
        )));

    }
}
