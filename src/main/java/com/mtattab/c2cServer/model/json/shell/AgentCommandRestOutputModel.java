package com.mtattab.c2cServer.model.json.shell;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
public class AgentCommandRestOutputModel {
    @JsonProperty("output")
    String output;
    @JsonProperty("uuid")
    String uuid;

    @JsonCreator
    public AgentCommandRestOutputModel(
            @JsonProperty("uuid") String uuid,
            @JsonProperty("output") String output

    ) {
        this.output = output;
        this.uuid = uuid;
    }

}
