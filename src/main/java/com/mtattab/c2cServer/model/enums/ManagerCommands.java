package com.mtattab.c2cServer.model.enums;


import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor

public enum ManagerCommands {

    HELP("help", "show all commands", "help", "There is no required parameters"),
    LIST_ACTIVE_SESSIONS("sessions","List all active sessions", "sessions", "There is no required parameters"),
    CONNECT_TO_ACTIVE_SESSION("connect","connect to session by using its id", "connect <session-id>", "requires the use of a session id");



    private String command;

    private String description;

    private String example;

    private String requiredParams;

    private ManagerCommands(String value, String description, String example,String requiredParams) {
        this.command = value;
        this.description= description;
        this.example= example;
        this.requiredParams= requiredParams;
    }

}
