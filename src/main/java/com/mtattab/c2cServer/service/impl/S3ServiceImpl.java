package com.mtattab.c2cServer.service.impl;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.mtattab.c2cServer.model.RestOutputModel;
import com.mtattab.c2cServer.service.S3Service;
import com.mtattab.c2cServer.util.DataManipulationUtil;
import com.mtattab.c2cServer.util.GenericOperationsUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import java.util.ArrayList;
import java.util.List;
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




    public RestOutputModel uploadFile(MultipartFile multipartFile, String dir) {
        RestOutputModel restOutputModel = new RestOutputModel();
        String fileUrl = "";
        try {
            File file = DataManipulationUtil.convertMultiPartToFile(multipartFile);
            String fileName = dir + "/" + this.generateFileName(multipartFile);
            fileUrl = endpointUrl + "/" + s3BucketName + "/"  + fileName;
            uploadFileTos3bucket(fileName, file);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
            restOutputModel.setMsg("Failed to Save file into s3 due to: "+e.getMessage());
            restOutputModel.setStatusCode(500);
            return restOutputModel;
        }
        restOutputModel.setStatusCode(200);
        restOutputModel.setMsg("Saved file into s3 successfully with path: "+fileUrl);

        return restOutputModel;

    }



    public RestOutputModel getListOfFiles(String startingPath){
        RestOutputModel restOutputModel = new RestOutputModel();
        ObjectListing objectListing = s3client.listObjects(s3BucketName, startingPath);
//        ObjectListing objectListing = s3client.listObjects(s3BucketName);

        List<String >filenames= new ArrayList<>();
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
//            System.out.println("File Name: " + objectSummary.getKey());
            String fileName = extractFileName(objectSummary.getKey(), "/");
            if (fileName.equalsIgnoreCase(startingPath)) continue;
            filenames.add(fileName);
        }
        restOutputModel.setFilenames(filenames);
        restOutputModel.setStatusCode(200);
        restOutputModel.setMsg("file names was retrieved successfully");
        return restOutputModel;
    }

    private String extractFileName(String input, String delimiter) {
        try {
            // Split the input string using the '/' character
            String[] parts = input.split(delimiter);

            // Get the last part of the split array (which should be the filename)
            String fileName = parts[parts.length - 1];

            return fileName;
        }catch (Exception e){
            return input;

        }

    }

    public RestOutputModel deleteFileFromS3Bucket(String folder, String fileName) {
        RestOutputModel restOutputModel = new RestOutputModel();
        String filePath= folder+"/"+fileName;
        boolean fileExists = s3client.doesObjectExist(s3BucketName, filePath);

        if (fileExists){
            s3client.deleteObject(new DeleteObjectRequest(s3BucketName , filePath));
            restOutputModel.setMsg( "Successfully deleted" );
            restOutputModel.setStatusCode(200);
            deleteFolderIfEmpty(folder);
            return restOutputModel;
        }else {

            restOutputModel.setMsg( "file does not exists: "+ filePath );
            restOutputModel.setStatusCode(404);

            return restOutputModel;
        }

    }

    private void deleteFolderIfEmpty(String folder){

        if (getListOfFiles(folder).getFilenames().isEmpty()){
            s3client.deleteObject(s3BucketName, folder+"/");

        }

    }



    public RestOutputModel getFileFromS3Bucket(String folder, String fileName) {
        RestOutputModel restOutputModel = new RestOutputModel();
        String filePath= folder+"/"+fileName;
        System.out.println(filePath);
        boolean fileExists = s3client.doesObjectExist(s3BucketName, filePath);

        if (fileExists){

            S3Object s3Object = s3client.getObject(s3BucketName, filePath);

            restOutputModel.setS3Object(s3Object);
            restOutputModel.setMsg( "Successfully retrieved" );
            restOutputModel.setStatusCode(200);
            return restOutputModel;
        }else {

            restOutputModel.setMsg( "file does not exists: "+ filePath );
            restOutputModel.setStatusCode(404);

            return restOutputModel;
        }


    }




}
