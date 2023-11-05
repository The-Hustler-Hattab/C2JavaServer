package com.mtattab.c2cServer.service.impl.s3;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.mtattab.c2cServer.model.enums.s3.S3FileType;
import com.mtattab.c2cServer.model.enums.status.S3FileStatus;
import com.mtattab.c2cServer.model.json.RestOutputModel;
import com.mtattab.c2cServer.model.entity.SessionFilesEntity;
import com.mtattab.c2cServer.model.json.s3.S3FileStructureJsonModel;
import com.mtattab.c2cServer.repository.SessionFilesRepository;
import com.mtattab.c2cServer.repository.dao.SessionLogFilesIntegrationDao;
import com.mtattab.c2cServer.service.S3Service;
import com.mtattab.c2cServer.util.DataManipulationUtil;
import com.mtattab.c2cServer.util.GenericOperationsUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class S3ServiceImpl implements S3Service {

    @Autowired
    SessionLogFilesIntegrationDao sessionLogFilesIntegrationDao;

    @Autowired
    SessionFilesRepository sessionFilesRepository;

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
        SessionFilesEntity sessionFilesEntity = new SessionFilesEntity();
        String fileUrl = "";
        try {
            File file = DataManipulationUtil.convertMultiPartToFile(multipartFile);
            String fileName = dir + "/" + this.generateFileName(multipartFile);
            fileUrl = endpointUrl + "/" + s3BucketName + "/"  + fileName;


            uploadFileTos3bucket(fileName, file);
            file.delete();

//            save file into the db
            sessionFilesEntity.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
            sessionFilesEntity.setFile(fileName);
            sessionFilesEntity.setFileStatus(S3FileStatus.FILE_CREATED.getStatus());
            sessionLogFilesIntegrationDao.saveFilesIntoLog(Collections.singletonList(sessionFilesEntity),dir);

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
            String fileName = DataManipulationUtil.getFileNameFromPath(objectSummary.getKey());
            if (fileName.equalsIgnoreCase(startingPath)) continue;
            filenames.add(fileName);
        }
        restOutputModel.setFilenames(filenames);
        restOutputModel.setStatusCode(200);
        restOutputModel.setMsg("file names was retrieved successfully");
        return restOutputModel;
    }




    public RestOutputModel listFilesInBucket(String prefix) {
        RestOutputModel restOutputModel = new RestOutputModel();
        List<S3FileStructureJsonModel> result = new ArrayList<>();

        listObjects(prefix, result);

        restOutputModel.setMsg("Retrieved file structure successfully");
        restOutputModel.setS3FileStructureJsonModels(result);
        restOutputModel.setStatusCode(HttpStatus.OK.value());

        return restOutputModel;
    }

    private void listObjects(String prefix, List<S3FileStructureJsonModel> result) {
        ListObjectsV2Request request = new ListObjectsV2Request()
                .withBucketName(this.s3BucketName)
                .withPrefix(prefix);

        ListObjectsV2Result response = s3client.listObjectsV2(request);

        for (S3ObjectSummary summary : response.getObjectSummaries()) {
            if (summary.getKey().equals(prefix)) {
                // Skip the directory itself; we only want files and subdirectories
                continue;
            }

            setupFileInS3Model(summary, result);
        }
    }


    private void setupFileInS3Model(S3ObjectSummary summary, List<S3FileStructureJsonModel> result){

        String key = summary.getKey();
        String[] keyParts = key.split("/");

        if (keyParts.length == 1) {
            // This is a file at the root level
            S3FileStructureJsonModel file = S3FileStructureJsonModel.builder()
                    .name(key)
                    .path(key)
                    .type(S3FileType.FILE)
                    .updated(new Date(summary.getLastModified().getTime()))
                    .build();
            result.add(file);
        } else {
            // This is a file within a subdirectory
            String parentDirectory = keyParts[0];


            S3FileStructureJsonModel folder = result
                    .stream()
                    .filter(item -> item.getName().equals(parentDirectory))
                    .findFirst()
                    .orElse(null);

            if (folder == null
//            && summary.getETag() == null
            ) {

                    folder = S3FileStructureJsonModel.builder()
                            .name(parentDirectory)
                            .path(parentDirectory)
                            .type(S3FileType.FOLDER)
                            .subFolders(new ArrayList<>())
                            .build();
                    result.add(folder);


            }
            if (summary.getETag() != null && !DataManipulationUtil.endsWithSlash(summary.getKey())){
                S3FileStructureJsonModel file = S3FileStructureJsonModel.builder()
                        .name(keyParts[keyParts.length-1])
                        .path(key)
                        .type(S3FileType.FILE)
                        .updated(new Date(summary.getLastModified().getTime()))
                        .build();
                folder.getSubFolders().add(file);

            }

            System.out.println(summary);
        }
    }



    private boolean isFolder(S3ObjectSummary summary){
//        if etag is null than it is a folder
        if (summary.getETag() == null){
            return true;
        }else return false;

    }









    public RestOutputModel deleteFileFromS3Bucket( String fileName) {
        RestOutputModel restOutputModel = new RestOutputModel();
        String filePath= fileName;
        boolean fileExists = s3client.doesObjectExist(s3BucketName, filePath);

        if (fileExists){
            s3client.deleteObject(new DeleteObjectRequest(s3BucketName , filePath));
            restOutputModel.setMsg( "Successfully deleted" );
            restOutputModel.setStatusCode(200);
            deleteFolderIfEmpty(fileName);
            sessionFilesRepository.updateFileStatus(
                    Timestamp.valueOf(LocalDateTime.now()),
                    S3FileStatus.FILE_DELETED.getStatus(),
                    filePath );


            return restOutputModel;
        }else {

            restOutputModel.setMsg( "file does not exists: "+ filePath );
            restOutputModel.setStatusCode(404);

            return restOutputModel;
        }

    }

    private void deleteFolderIfEmpty(String folder){
//        turn the string to list of files and return the first file since it is the root folder
        List<String> fileStructure= DataManipulationUtil.stringToList(folder, "/");
        folder = fileStructure.get(0);

        if (getListOfFiles(folder).getFilenames().isEmpty()){
            s3client.deleteObject(s3BucketName, folder+"/");

        }

    }



    public RestOutputModel getFileFromS3Bucket( String fileName) {
        RestOutputModel restOutputModel = new RestOutputModel();
        String filePath= fileName;
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
