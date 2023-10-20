package com.mtattab.c2cServer.controller;

import com.mtattab.c2cServer.service.impl.S3ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/v1/api/s3", produces = "application/json")
public class S3Controller {


    @Autowired
    S3ServiceImpl s3Service;

    @PostMapping(name = "/upload", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE} )
    public String uploadFile(@RequestPart("file") MultipartFile file, @RequestParam("sessionId") String sessionId) {

        s3Service.uploadFile(file, sessionId);

        return "upload-success"; // Return a success page or a message
    }
}
