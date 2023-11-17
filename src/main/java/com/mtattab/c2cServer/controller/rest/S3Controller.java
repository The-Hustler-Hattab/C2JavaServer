package com.mtattab.c2cServer.controller.rest;

import com.mtattab.c2cServer.annotations.SessionExistsValidator;
import com.mtattab.c2cServer.model.json.RestOutputModel;
import com.mtattab.c2cServer.service.impl.s3.S3ServiceImpl;
import com.mtattab.c2cServer.util.DataManipulationUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.mtattab.c2cServer.util.Constants.S3_PATH;
import static com.mtattab.c2cServer.util.Constants.S3_UPLOAD;

@RestController
@RequestMapping(path = S3_PATH)
@Validated
public class S3Controller {


    @Autowired
    S3ServiceImpl s3Service;

    @PostMapping(value = S3_UPLOAD, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity<RestOutputModel> uploadFile(@RequestPart("file") MultipartFile file
            , @Valid  @RequestParam("sessionId") @SessionExistsValidator String sessionId) {
        RestOutputModel restOutputModel = s3Service.uploadFile(file, sessionId);

        return new ResponseEntity<>(restOutputModel, HttpStatusCode.valueOf(restOutputModel.getStatusCode()));

    }


    @GetMapping(value = "/getJsonListFilesInDirectory", produces = {MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity<RestOutputModel> getFilesInDirectory(@RequestParam("dir") String dir) {
        RestOutputModel restOutputModel = s3Service.getListOfFiles(dir);

        return new ResponseEntity<>(restOutputModel, HttpStatusCode.valueOf(restOutputModel.getStatusCode()));

    }

    @GetMapping(value = "/getJsonListFiles", produces = {MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity<RestOutputModel> getJsonListFiles() {
        RestOutputModel restOutputModel = s3Service.listFilesInBucket("");

        return new ResponseEntity<>(restOutputModel, HttpStatusCode.valueOf(restOutputModel.getStatusCode()));

    }



    @DeleteMapping(value = "/deleteFile", produces = {MediaType.APPLICATION_JSON_VALUE} )
    public ResponseEntity<RestOutputModel> deleteFile(@RequestParam("file") String file) {
        RestOutputModel restOutputModel = s3Service.deleteFileFromS3Bucket(file);

        return new ResponseEntity<>(restOutputModel, HttpStatusCode.valueOf(restOutputModel
                .getStatusCode()));

    }


    @GetMapping(value = "/getS3File", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE} )
    public ResponseEntity<byte[]> getS3File(String file) {

        RestOutputModel restOutputModel = s3Service.getFileFromS3Bucket(file);
        if (restOutputModel.getS3Object() == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        try {
            byte[] fileBytes = restOutputModel.getS3Object().getObjectContent().readAllBytes();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment",
                    DataManipulationUtil.getFileNameFromPath(file));

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileBytes);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }
}
