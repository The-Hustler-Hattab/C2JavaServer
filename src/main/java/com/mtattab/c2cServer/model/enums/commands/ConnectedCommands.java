package com.mtattab.c2cServer.model.enums.commands;


import lombok.Getter;


@Getter
public enum ConnectedCommands {

    HELP_CONNECTED("/?", "show all commands", "/?",
            "There is no required parameters"),

    DISCONNECT_KILL_CURRENT_CONNECTION("/kill-me","kills current session", "/kill-me",
            "There is no required parameters"),

    BACKGROUND_SESSION("/bg","returns to manager console", "/bg",
            "There is no required parameters"),
    REGULAR_REVERSE_SHELL_COMMAND("*","if the command you input does not match the above then it will be re-routed to command line terminal", "",
            "")


    ;




    private final String command;

    private final String description;

    private final String example;

    private final String requiredParams;

    ConnectedCommands(String value, String description, String example, String requiredParams) {
        this.command = value;
        this.description= description;
        this.example= example;
        this.requiredParams= requiredParams;
    }

}
