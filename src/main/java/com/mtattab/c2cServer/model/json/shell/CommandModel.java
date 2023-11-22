package com.mtattab.c2cServer.model.json.shell;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommandModel {
    private String commandName;
    private String description;
    private String exampleUsage;
    private String parameters;
}
