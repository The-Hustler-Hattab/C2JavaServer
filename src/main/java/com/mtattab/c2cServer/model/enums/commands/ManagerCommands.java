package com.mtattab.c2cServer.model.enums.commands;


import lombok.Getter;


@Getter
public enum ManagerCommands {

    HELP_MANAGER("/?", "show all commands", "/?",
            "There is no required parameters"),

    LIST_ACTIVE_SESSIONS("sessions","List all active sessions", "sessions",
            "There is no required parameters"),

    TERMINATE_ACTIVE_SESSION("kill","terminate active session using its id", "kill <session-id>",
            "requires the use of a session id"),

    CONNECT_TO_ACTIVE_SESSION("connect","connect to session by using its id", "connect <session-id> [-f]",
        "requires the use of a session id. '-f' is an optional parameter which force connects to a client by closing any connection it has with other managers "),

    ;




    private final String command;

    private final String description;

    private final String example;

    private final String requiredParams;

    ManagerCommands(String value, String description, String example,String requiredParams) {
        this.command = value;
        this.description= description;
        this.example= example;
        this.requiredParams= requiredParams;
    }

}
