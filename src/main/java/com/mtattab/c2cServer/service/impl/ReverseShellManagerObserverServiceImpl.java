package com.mtattab.c2cServer.service.impl;

import com.mtattab.c2cServer.model.json.ManagerCommunicationModel;
import com.mtattab.c2cServer.model.json.MessageEventModel;
import com.mtattab.c2cServer.model.enums.events.ActiveSessionsEvents;
import com.mtattab.c2cServer.model.enums.commands.ManagerCommands;
import com.mtattab.c2cServer.exceptions.CommandNotFoundException;
import com.mtattab.c2cServer.service.Command;
import com.mtattab.c2cServer.service.CommandFactory;
import com.mtattab.c2cServer.service.ReverseShellManagerService;
import com.mtattab.c2cServer.service.commands.manager.SessionsCommand;
import com.mtattab.c2cServer.service.factory.ConnectedCommandFactory;
import com.mtattab.c2cServer.service.factory.ManagerCommandFactory;
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
    ManagerCommandFactory managerCommandFactory;

    @Autowired
    SessionsCommand sessionsCommand;


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
        log.debug("[+] Current active sessions are {}",ConnectionManager.connectedManagerToReverseSessions);
        if (ConnectionManager.connectedManagerToReverseSessions.get(session)!=null){
            CommandFactory connectedCommandFactory = new ConnectedCommandFactory();
            handleManagerCommandMessage(session, message, connectedCommandFactory);

        }
        else
            if (!handleManagerCommandMessage(session,message, managerCommandFactory)){
            SocketUtil.sendMessage(session, new TextMessage(
                    DataManipulationUtil.convertObjectToJson(ManagerCommunicationModel.builder()
                            .msg("Command Not Found. Use '"+ManagerCommands.HELP_MANAGER.getCommand()+"' for list of commands")
                            .build()
                    )));
        }

    }

    private boolean handleManagerCommandMessage(WebSocketSession session, TextMessage message, CommandFactory commandFactory) {
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

    public void showSessionsForIntialConnections(WebSocketSession session){

        Command command = sessionsCommand;
        command.execute(null, session);

    }





}
