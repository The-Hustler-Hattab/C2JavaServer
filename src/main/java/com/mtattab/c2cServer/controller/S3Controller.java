package com.mtattab.c2cServer.controller;

import com.mtattab.c2cServer.model.RestOutputModel;
import com.mtattab.c2cServer.service.impl.S3ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/v1/api/s3")
public class S3Controller {


    @Autowired
    S3ServiceImpl s3Service;

    @PostMapping(value = "/upload", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity<RestOutputModel> uploadFile(@RequestPart("file") MultipartFile file, @RequestParam("sessionId") String sessionId) {
        RestOutputModel restOutputModel = s3Service.uploadFile(file, sessionId);

        return new ResponseEntity<>(restOutputModel, HttpStatusCode.valueOf(restOutputModel.getStatusCode()));

    }


    @GetMapping(value = "/getJsonListFilesInDirectory", produces = {MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity<RestOutputModel> getFilesInDirectory(@RequestParam("dir") String dir) {
        RestOutputModel restOutputModel = s3Service.getListOfFiles(dir);

        return new ResponseEntity<>(restOutputModel, HttpStatusCode.valueOf(restOutputModel.getStatusCode()));

    }

    @DeleteMapping(value = "/deleteFile", produces = {MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity<RestOutputModel> deleteFile(@RequestParam("dir") String dir
            , @RequestParam("file") String file) {
        RestOutputModel restOutputModel = s3Service.deleteFileFromS3Bucket(dir,file);

        return new ResponseEntity<>(restOutputModel, HttpStatusCode.valueOf(restOutputModel
                .getStatusCode()));

    }


    @GetMapping(value = "/getS3File", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE} )
    public ResponseEntity<byte[]> getS3File(String folder, String file) {

        RestOutputModel restOutputModel = s3Service.getFileFromS3Bucket(folder,file);
        if (restOutputModel.getS3Object() == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        try {
            byte[] fileBytes = restOutputModel.getS3Object().getObjectContent().readAllBytes();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", file);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileBytes);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }
}
