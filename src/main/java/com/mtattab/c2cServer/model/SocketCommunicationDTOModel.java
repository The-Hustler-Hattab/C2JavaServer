package com.mtattab.c2cServer.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SocketCommunicationDTOModel {

    private String socketId;
    private String socketAddress;
    private String message;

}
