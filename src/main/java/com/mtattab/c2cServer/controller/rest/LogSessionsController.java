package com.mtattab.c2cServer.controller.rest;

import com.mtattab.c2cServer.model.json.RestOutputModel;
import com.mtattab.c2cServer.service.LogSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping(path = "/v1/api/logs", produces = {MediaType.APPLICATION_JSON_VALUE})
public class LogSessionsController {

    @Autowired
    LogSessionService sessionService;

    @GetMapping(value = "/getNLogs" )
    public ResponseEntity<RestOutputModel> getNLogs(@RequestParam("count") Integer count) {

        RestOutputModel restOutputModel = sessionService.getNLogs(count);

        return new ResponseEntity<>(restOutputModel, HttpStatusCode.valueOf(restOutputModel.getStatusCode()));

    }

    @GetMapping(value = "/getLogsBetween2Dates" )
    public ResponseEntity<RestOutputModel> getLogsBetween2Dates(@RequestParam("start") String start,
                                                                @RequestParam("finish") String finish) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Timestamp startDate = new java.sql.Timestamp(dateFormat.parse(start).getTime());
        Timestamp endDate = new java.sql.Timestamp(dateFormat.parse(finish).getTime());


        RestOutputModel restOutputModel = sessionService.getLogsBetween2Dates(startDate,endDate);

        return new ResponseEntity<>(restOutputModel, HttpStatusCode.valueOf(restOutputModel.getStatusCode()));

    }


}
