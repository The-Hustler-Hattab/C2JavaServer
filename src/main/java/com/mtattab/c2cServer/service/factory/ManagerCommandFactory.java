package com.mtattab.c2cServer.service.factory;

import com.mtattab.c2cServer.model.enums.commands.ManagerCommands;
import com.mtattab.c2cServer.model.exceptions.CommandNotFoundException;
import com.mtattab.c2cServer.service.Command;
import com.mtattab.c2cServer.service.CommandFactory;
import com.mtattab.c2cServer.service.commands.manager.ConnectCommand;
import com.mtattab.c2cServer.service.commands.manager.HelpManagerCommand;
import com.mtattab.c2cServer.service.commands.manager.SessionsCommand;
import com.mtattab.c2cServer.service.commands.manager.TerminateCommand;
import com.mtattab.c2cServer.util.DataManipulationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class ManagerCommandFactory implements CommandFactory {

    @Autowired
    SessionsCommand sessionsCommand;
    @Autowired
    TerminateCommand terminateCommand;

    @Autowired
    ConnectCommand connectCommand;

    public Command createCommand(String userInput) throws CommandNotFoundException {
        List<String> userInputAsList= DataManipulationUtil.stringToList(userInput, " ");
        Optional<ManagerCommands> command = Arrays.stream(ManagerCommands.values())
                .filter(cmd -> Objects.requireNonNull(userInputAsList.get(0))
                        .equalsIgnoreCase(cmd.getCommand()) )
                .findFirst();

        if (command.isPresent()){

            return getCommandByName(command.get());

        }else throw new CommandNotFoundException("Command does not exists: "+userInput);
    }

    Command getCommandByName(ManagerCommands command) throws CommandNotFoundException {
        switch (command) {
            case HELP_MANAGER -> {
                return new HelpManagerCommand();
            }
            case LIST_ACTIVE_SESSIONS -> {
                return sessionsCommand;
            }
            case TERMINATE_ACTIVE_SESSION -> {
                return terminateCommand;
            }
            case CONNECT_TO_ACTIVE_SESSION -> {
                return connectCommand;
            }
            default -> throw new CommandNotFoundException();
        }
    }
}
