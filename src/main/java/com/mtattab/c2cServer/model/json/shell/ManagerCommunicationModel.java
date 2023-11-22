package com.mtattab.c2cServer.model.json.shell;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mtattab.c2cServer.model.json.SocketCommunicationDTOModel;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String msg;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean success;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String uuid = UUID.randomUUID().toString();


}
