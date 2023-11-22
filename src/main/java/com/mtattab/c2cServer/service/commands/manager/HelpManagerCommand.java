package com.mtattab.c2cServer.service.commands.manager;

import com.mtattab.c2cServer.model.json.shell.CommandModel;
import com.mtattab.c2cServer.model.enums.commands.ManagerCommands;
import com.mtattab.c2cServer.service.Command;
import com.mtattab.c2cServer.util.DataManipulationUtil;
import com.mtattab.c2cServer.util.SocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;
@Slf4j
public class HelpManagerCommand implements Command {
    @Override
    public void execute(List<String> args, WebSocketSession currentSocket) {
        log.debug("[+] Help Method got triggered");
        List<CommandModel> listOfCommands = new ArrayList<>();
        for (ManagerCommands command : ManagerCommands.values()) {
            listOfCommands.add(CommandModel.builder()
                    .commandName(command.getCommand())
                    .description(command.getDescription())
                    .exampleUsage(command.getExample())
                    .parameters(command.getRequiredParams())
                    .build());
        }
        SocketUtil.sendMessage(currentSocket, new TextMessage(DataManipulationUtil.convertObjectToJson(listOfCommands)));
    }
}
