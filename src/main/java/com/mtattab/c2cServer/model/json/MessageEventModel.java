package com.mtattab.c2cServer.model.json;

import com.mtattab.c2cServer.model.enums.events.ActiveSessionsEvents;
import lombok.*;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.socket.WebSocketSession;


@Getter
@Setter
@ToString
public class MessageEventModel extends ApplicationEvent {
    private ActiveSessionsEvents event;
    private WebSocketSession session;

    private WebSocketSession targetConnectSession;

    public MessageEventModel(Object source, ActiveSessionsEvents event) {
        super(source);
        this.event = event;
    }
    public MessageEventModel(Object source, ActiveSessionsEvents event, WebSocketSession session ) {
        super(source);
        this.event = event;
        this.session = session;
    }
    public MessageEventModel(Object source, ActiveSessionsEvents event, WebSocketSession currentSession, WebSocketSession targetConnectSession) {
        super(source);
        this.event = event;
        this.session = currentSession;
        this.targetConnectSession= targetConnectSession;
    }
}
