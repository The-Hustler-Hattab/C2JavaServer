package com.mtattab.c2cServer.service.impl;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.mtattab.c2cServer.service.S3Service;
import com.mtattab.c2cServer.util.DataManipulationUtil;
import com.mtattab.c2cServer.util.GenericOperationsUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;

import java.util.Objects;

@Service
public class S3ServiceImpl implements S3Service {
    @Value("${aws.s3.secret}")
    private String s3Secret;

    @Value("${aws.s3.key}")
    private String s3Key;

    @Value("${aws.s3.bucket-name}")
    private String s3BucketName;


    @Value("${aws.s3.endpoint}")
    private String endpointUrl;
    private AmazonS3 s3client;

    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(this.s3Key, this.s3Secret);
        // rewrite the below code to not use a deprecated version of the SDK
        this.s3client = new AmazonS3Client(credentials);
    }


    private void uploadFileTos3bucket(String fileName, File file) {
        // create file
        s3client.putObject(new PutObjectRequest(s3BucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.Private));
    }


    private String generateFileName(MultipartFile multiPart) {
        return GenericOperationsUtil.getDateTime() + "-" + Objects.requireNonNull(multiPart.getOriginalFilename()).replace(" ", "_");
    }




    public String uploadFile(MultipartFile multipartFile, String dir) {

        String fileUrl = "";
        try {
            File file = DataManipulationUtil.convertMultiPartToFile(multipartFile);
            String fileName = dir + "/" + this.generateFileName(multipartFile);
            fileUrl = endpointUrl + "/" + s3BucketName + "/"  + fileName;
            uploadFileTos3bucket(fileName, file);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
    }


    public String deleteFileFromS3Bucket(String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        s3client.deleteObject(new DeleteObjectRequest(s3BucketName + "/", fileName));
        return "Successfully deleted";
    }



}
