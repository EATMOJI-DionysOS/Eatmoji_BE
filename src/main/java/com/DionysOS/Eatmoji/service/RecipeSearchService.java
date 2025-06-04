package com.DionysOS.Eatmoji.service;

import com.DionysOS.Eatmoji.util.CsvUtil;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.io.InputStream;

@Service
public class RecipeSearchService {

    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.s3.filename}")
    private String fileName;

    public RecipeSearchService(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    // 레시피가 없을 때의 예외 처리 필요
    public String findRecipeByFood(String food) {
        try {
            String recipe;
            S3Object s3Object = s3Client.getObject(bucketName, fileName);
            InputStream inputStream = s3Object.getObjectContent();

            //  RCP_SNO: 1열, CKG_NM: 3열
            recipe =  CsvUtil.findColumnValue(inputStream, 2, food, 0);

            if(recipe == null) {
                System.out.println("Recipe not found");
                return null;
            }

            return recipe;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
