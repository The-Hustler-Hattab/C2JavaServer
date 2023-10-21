package com.mtattab.c2cServer.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class InitialConnectionMessageModel {

    private String sessionId;
    private String initialMessage;

}
