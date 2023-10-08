package com.mtattab.c2cServer.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ManagerCommunicationModel {


    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<SocketCommunicationDTOModel> webSocketSessionSet;

    private String msg;


}
