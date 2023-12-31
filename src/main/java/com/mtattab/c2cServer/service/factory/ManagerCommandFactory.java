package com.mtattab.c2cServer.service.factory;

import com.mtattab.c2cServer.model.enums.commands.ManagerCommands;
import com.mtattab.c2cServer.exceptions.CommandNotFoundException;
import com.mtattab.c2cServer.service.Command;
import com.mtattab.c2cServer.service.CommandFactory;
import com.mtattab.c2cServer.service.commands.manager.*;
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
    InfoCommand infoCommand;


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
                return new SessionsCommand();
            }
            case TERMINATE_ACTIVE_SESSION -> {
                return new TerminateCommand();
            }
            case CONNECT_TO_ACTIVE_SESSION -> {
                return new ConnectCommand();
            }
            case REQUEST_INFO_FOR_SESSION -> {
                return infoCommand;
            }
            case ALL_SEND_SESSION -> {
                return new AllSendCommand();
            }

            default -> throw new CommandNotFoundException();
        }
    }
}
