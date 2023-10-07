package com.mtattab.c2cServer.service.impl;

import com.mtattab.c2cServer.model.MessageEventModels;
import com.mtattab.c2cServer.model.enums.ActiveSessionsEvents;
import com.mtattab.c2cServer.service.ReverseShellManagerService;
import com.mtattab.c2cServer.service.observable.ActiveSessionsObservable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Arrays;
import java.util.Optional;


@Service
@Slf4j
public class ReverseShellManagerObserverServiceImpl implements  ApplicationListener<MessageEventModels>, ReverseShellManagerService {

    @Autowired
    ActiveSessionsObservable activeSessionsObservable;



    @Override
    public void onApplicationEvent(MessageEventModels messageEventModels) {
        log.info("[+] onApplicationEvent triggered with event {}",messageEventModels);
        Optional<ActiveSessionsEvents> matchingEvent = Arrays.stream(ActiveSessionsEvents.values())
                .filter(event -> event.equals(messageEventModels.getEvent()))
                .findFirst();

        if (matchingEvent.isPresent()) {
            matchingEvent.get().getEventHandler().handle(messageEventModels.getSession(), activeSessionsObservable.getActiveMangerSessions());
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
}
