package com.mtattab.c2cServer.model.enums;


import lombok.Getter;


@Getter
public enum ManagerCommands {

    HELP("/?", "show all commands", "/?", "There is no required parameters"),
    LIST_ACTIVE_SESSIONS("sessions","List all active sessions", "sessions", "There is no required parameters"),
    TERMINATE_ACTIVE_SESSION("kill","terminate active session using its id", "kill <session-id>", "requires the use of a session id"),
//    the connect command is special and require a different type of logic
    CONNECT_TO_ACTIVE_SESSION("connect","connect to session by using its id", "connect <session-id>", "requires the use of a session id")

    ;




    private final String command;

    private final String description;

    private final String example;

    private final String requiredParams;

    private ManagerCommands(String value, String description, String example,String requiredParams) {
        this.command = value;
        this.description= description;
        this.example= example;
        this.requiredParams= requiredParams;
    }

}
