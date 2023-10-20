package com.mtattab.c2cServer.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@SpringBootTest
public class S3ServiceTest {

    @Autowired
    S3ServiceImpl s3Service;

    @Test
    public void uploadFileToS3Test() throws Exception {
        // Create a mock file
        String originalFilename = "testfile.txt";


//        s3Service.uploadFileToS3(convertFileToMultipartFile(originalFilename),"test");


;
    }




}
