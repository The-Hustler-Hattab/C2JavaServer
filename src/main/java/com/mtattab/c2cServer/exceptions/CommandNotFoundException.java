package com.mtattab.c2cServer.exceptions;

public class CommandNotFoundException extends Exception{

    public CommandNotFoundException() {
        super("Command not found");
    }

    public CommandNotFoundException(String message) {
        super(message);
    }

    public CommandNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
