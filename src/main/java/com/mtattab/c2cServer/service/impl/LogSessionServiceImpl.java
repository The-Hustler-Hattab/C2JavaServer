package com.mtattab.c2cServer.service.impl;

import com.mtattab.c2cServer.model.entity.SessionLogEntity;
import com.mtattab.c2cServer.model.json.RestOutputModel;
import com.mtattab.c2cServer.repository.SessionLogRepository;
import com.mtattab.c2cServer.repository.dao.SessionLogsDao;
import com.mtattab.c2cServer.service.LogSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class LogSessionServiceImpl implements LogSessionService {


    @Autowired
    SessionLogsDao sessionLogsDao;

    @Autowired
    SessionLogRepository sessionLogRepository;


    public RestOutputModel getNLogs(int n){
        RestOutputModel restOutputModel = new RestOutputModel();

        List<SessionLogEntity> sessionLogEntities= sessionLogsDao.getFirstNLogs(n);

        restOutputModel.setMsg("Retrieved Logs successfully");
        restOutputModel.setStatusCode(HttpStatus.OK.value());
        restOutputModel.setSessionLogEntities(sessionLogEntities);

        return restOutputModel;
    }

    public RestOutputModel getLogsBetween2Dates(Timestamp start, Timestamp end ){
        RestOutputModel restOutputModel = new RestOutputModel();

        List<SessionLogEntity> sessionLogEntities= sessionLogRepository.getLogsBetween2Dates(start, end);

        restOutputModel.setMsg("Retrieved Logs successfully");
        restOutputModel.setStatusCode(HttpStatus.OK.value());
        restOutputModel.setSessionLogEntities(sessionLogEntities);

        return restOutputModel;
    }

}
