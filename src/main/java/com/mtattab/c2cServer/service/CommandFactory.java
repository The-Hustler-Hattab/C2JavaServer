package com.mtattab.c2cServer.service;

import com.mtattab.c2cServer.model.exceptions.CommandNotFoundException;

public interface CommandFactory {

    Command createCommand(String userInput) throws CommandNotFoundException ;
}
