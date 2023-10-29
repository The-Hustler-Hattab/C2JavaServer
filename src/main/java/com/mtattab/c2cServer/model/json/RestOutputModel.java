package com.mtattab.c2cServer.model.json;

import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mtattab.c2cServer.model.entity.SessionLogEntity;
import lombok.Data;

import java.util.List;

@Data
public class RestOutputModel {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String msg;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer statusCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> filenames;

    @JsonIgnore
    private S3Object s3Object;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<SessionLogEntity> sessionLogEntities;


}
