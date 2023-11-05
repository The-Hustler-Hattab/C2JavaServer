package com.mtattab.c2cServer.service.impl;

import com.mtattab.c2cServer.model.json.s3.S3FileStructureJsonModel;
import com.mtattab.c2cServer.service.impl.s3.S3ServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.mtattab.c2cServer.util.DataManipulationUtil.convertObjectToJson;

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


    @Test
    public void listFilesInBucketTest() {
        List<S3FileStructureJsonModel> listFilesInBucket = s3Service.listFilesInBucket( "").getS3FileStructureJsonModels() ;

        System.out.println(convertObjectToJson(listFilesInBucket));

    }




}
