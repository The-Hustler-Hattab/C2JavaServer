package com.mtattab.c2cServer.model.enums.status;

import lombok.Getter;

@Getter
public enum S3FileStatus {

    FILE_CREATED("FILE_CREATED"),
    FILE_DELETED("FILE_DELETED");




    private String status;

    private S3FileStatus(String status){
        this.status = status;

    }
}
