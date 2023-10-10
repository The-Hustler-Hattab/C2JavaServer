package com.mtattab.c2cServer.model.enums;

import com.mtattab.c2cServer.model.MessageEventModel;
import com.mtattab.c2cServer.model.SocketCommunicationDTOModel;
import com.mtattab.c2cServer.service.ActiveSessionEventHandler;
import com.mtattab.c2cServer.util.DataManipulationUtil;
import com.mtattab.c2cServer.util.SocketUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Objects;
import java.util.Set;
@Slf4j
@Getter
public enum ActiveSessionsEvents {
    RECEIVED_NEW_CONNECTION("RECEIVED_NEW_CONNECTION", new ActiveSessionEventHandler() {
        @Override
        public void handle(MessageEventModel reverseShellSession, Set<WebSocketSession> mangerSessions, HashMap<WebSocketSession, WebSocketSession> sessionsToBeConnected) {
            mangerSessions.forEach(activeSession-> {
                SocketUtil.sendMessage(activeSession,new TextMessage(constructJsonMessage(reverseShellSession.getSession(),RECEIVED_NEW_CONNECTION.getValue())));
            });
            log.info("[+] {} handled",RECEIVED_NEW_CONNECTION.getValue());

        }
    }),
    LOST_CONNECTION("LOST_CONNECTION", new ActiveSessionEventHandler() {
        @Override
        public void handle(MessageEventModel reverseShellSession, Set<WebSocketSession> mangerSessions, HashMap<WebSocketSession, WebSocketSession> sessionsToBeConnected) {
            mangerSessions.forEach(activeSession-> {
                SocketUtil.sendMessage(activeSession,new TextMessage(constructJsonMessage(reverseShellSession.getSession(), LOST_CONNECTION.getValue())));
            });
            log.info("[+] {} handled", LOST_CONNECTION.getValue());

        }
    }),
    MANAGER_TO_REVERSE_SHELL_CONNECTION("MANAGER_TO_REVERSE_SHELL_CONNECTION", new ActiveSessionEventHandler() {
        @Override
        public void handle(MessageEventModel messageEventModel, Set<WebSocketSession> sessions, HashMap<WebSocketSession, WebSocketSession> sessionsToBeConnected) {

            sessionsToBeConnected.put(messageEventModel.getSession(), messageEventModel.getTargetConnectSession());


            log.info("[+] {} handled", MANAGER_TO_REVERSE_SHELL_CONNECTION.getValue());

        }
    })

    ;

    private String value;

    private ActiveSessionEventHandler eventHandler;


    private ActiveSessionsEvents(String value ,ActiveSessionEventHandler eventHandler) {
        this.value = value;
        this.eventHandler = eventHandler;
    }


    private static String constructJsonMessage(WebSocketSession session, String message){
        return DataManipulationUtil.convertObjectToJson(SocketCommunicationDTOModel.builder()
                    .socketId(session.getId())
                    .socketAddress(Objects.requireNonNull(session.getRemoteAddress()).toString())
                    .message(message)
                    .build()
            );

    }



}
