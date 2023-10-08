package com.mtattab.c2cServer.service.factory;

import com.mtattab.c2cServer.model.enums.ManagerCommands;
import com.mtattab.c2cServer.model.exceptions.CommandNotFoundException;
import com.mtattab.c2cServer.service.Command;
import com.mtattab.c2cServer.service.commands.HelpCommand;
import com.mtattab.c2cServer.util.DataManipulationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class CommandFactory {



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
            case HELP -> {
                return new HelpCommand();
            }
//            case 2 -> System.out.println("Option 2 selected");
//            case 3 -> System.out.println("Option 3 selected");
            default -> throw new CommandNotFoundException();
        }
    }
}
