package com.DionysOS.Eatmoji.service;

import com.DionysOS.Eatmoji.util.CsvUtil;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;

import java.io.InputStream;


@Service
public class RecipeSearchService {

    private final AmazonS3 s3Client;
    private final String bucketName;
    private final String fileName;

    public RecipeSearchService(AmazonS3 s3Client) {
        this.s3Client = s3Client;
        Dotenv dotenv = Dotenv.load();
        this.bucketName = dotenv.get("S3_BUCKET_NAME");
        this.fileName = dotenv.get("FILE_NAME");
    }

    public String findRecipeByFood(String food) {
        try {
            S3Object s3Object = s3Client.getObject(bucketName, fileName);
            InputStream inputStream = s3Object.getObjectContent();
            return CsvUtil.findColumnValue(inputStream, 2, food, 0);  // CKG_NM: 1열, RCP_SNO: 3열
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
