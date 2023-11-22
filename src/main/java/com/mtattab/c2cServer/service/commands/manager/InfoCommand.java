package com.mtattab.c2cServer.service.commands.manager;

import com.mtattab.c2cServer.model.entity.SessionLogEntity;
import com.mtattab.c2cServer.model.json.shell.ManagerCommunicationModel;
import com.mtattab.c2cServer.repository.SessionLogRepository;
import com.mtattab.c2cServer.service.Command;
import com.mtattab.c2cServer.util.DataManipulationUtil;
import com.mtattab.c2cServer.util.SocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class InfoCommand implements Command {

    @Autowired
    SessionLogRepository sessionLogRepository;

    @Override
    public void execute(List<String> args, WebSocketSession currentSocket) {
        String sessionId = args.get(1);

        Optional<SessionLogEntity>  sessionInfo = sessionLogRepository.findBySessionId(sessionId);

        if (sessionInfo.isPresent()){
            sessionInfo.get().setSessionFiles(null);
            sessionInfo.get().setSessionRemoteAddress(null);
            sessionInfo.get().setSessionLocalAddress(null);


            SocketUtil.sendMessage(currentSocket, new TextMessage(

                    DataManipulationUtil.convertObjectToJson(
                            sessionInfo.get()
                    )));

        }else {
            SocketUtil.sendMessage(currentSocket, new TextMessage(DataManipulationUtil.convertObjectToJson(ManagerCommunicationModel.builder()
                    .msg(String.format("session '%s' not found ", sessionId))
                    .success(false)
                    .build()
            )));
        }


    }
}
