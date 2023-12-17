package com.mtattab.c2cServer.model.json.shell;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class InitialConnectionMessageModel {

    private String sessionId;
    private String aes256Key;
    private String initialMessage;

}
