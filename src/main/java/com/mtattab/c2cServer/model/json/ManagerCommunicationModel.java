package com.mtattab.c2cServer.model.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Builder
@ToString
public class ManagerCommunicationModel {


    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<SocketCommunicationDTOModel> webSocketSessionSet;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String masterSessionId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String slaveSessionId;


    private String msg;

    private Boolean success;


}
