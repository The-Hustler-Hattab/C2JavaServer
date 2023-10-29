package com.mtattab.c2cServer.service;

import com.mtattab.c2cServer.model.json.RestOutputModel;

import java.sql.Timestamp;

public interface LogSessionService {

    public RestOutputModel getNLogs(int n);

    public RestOutputModel getLogsBetween2Dates(Timestamp start, Timestamp end );
}
