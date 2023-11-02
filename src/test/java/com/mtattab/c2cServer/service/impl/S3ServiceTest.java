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
    public void listFilesInS3Test() throws Exception {

        System.out.println(s3Service.getListOfFiles("0b9add0a-9aa9-19e5-72cd-dcf280b60677"));

//        s3Service.uploadFileToS3(convertFileToMultipartFile(originalFilename),"test");
    }

    @Test
    public void deleteFileInS3Test() {

        System.out.println(s3Service.deleteFileFromS3Bucket
                ( "af07b7fd-257c-d032-f4b8-a40bbad409be\\2023-10-21_19-01-pom.xml"));

    }




}
