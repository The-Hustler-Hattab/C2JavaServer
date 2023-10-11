package com.mtattab.c2cServer.service.factory;

import com.mtattab.c2cServer.model.enums.commands.ConnectedCommands;
import com.mtattab.c2cServer.model.exceptions.CommandNotFoundException;
import com.mtattab.c2cServer.service.Command;
import com.mtattab.c2cServer.service.CommandFactory;
import com.mtattab.c2cServer.service.commands.connected.BackgroundCommand;
import com.mtattab.c2cServer.service.commands.connected.DefualtConnectedCommand;
import com.mtattab.c2cServer.service.commands.connected.DisconnectCommand;
import com.mtattab.c2cServer.service.commands.connected.HelpConnectedCommand;
import com.mtattab.c2cServer.util.DataManipulationUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ConnectedCommandFactory implements CommandFactory {
    @Override
    public Command createCommand(String userInput) throws CommandNotFoundException {
        List<String> userInputAsList= DataManipulationUtil.stringToList(userInput, " ");
        Optional<ConnectedCommands> command = Arrays.stream(ConnectedCommands.values())
                .filter(cmd -> Objects.requireNonNull(userInputAsList.get(0))
                        .equalsIgnoreCase(cmd.getCommand()) )
                .findFirst();

        if (command.isPresent()){

            return getCommandByName(command.get());

        } else return new DefualtConnectedCommand();
    }


    Command getCommandByName(ConnectedCommands command) {
        switch (command) {
            case HELP_CONNECTED -> {
                return new HelpConnectedCommand();
            }
            case DISCONNECT_KILL_CURRENT_CONNECTION -> {
                return new DisconnectCommand();
            }
            case BACKGROUND_SESSION -> {
                return new BackgroundCommand();
            }
        }
        return new DefualtConnectedCommand();

    }
}
