package com.alibou.security.common;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.experimental.Helper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.UUID;

@Log4j2
@Component
public class UploadFile {

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.s3.accessKeyId}")
    private String accessKeyId;

    @Value("${aws.s3.secretAccessKey}")
    private String secretAccessKey;

    // Tạo S3 client bằng SDK v1
    private AmazonS3 getS3Client() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
        return AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }

    public String createImageFile(String thumbUpload, String type) {
        if (thumbUpload == null || thumbUpload.trim().isEmpty()) {
            log.warn("Base64 image string is empty.");
            return null;
        }

        try {
            // Tách dữ liệu Base64 từ chuỗi
            String[] dataArray = thumbUpload.trim().split(",");
            String imgBase64 = dataArray.length > 1 ? dataArray[1] : thumbUpload;
            String fileExtension = "jpg"; // Mặc định là JPG

            if (dataArray.length > 1 && dataArray[0].contains("image/")) {
                fileExtension = dataArray[0].replace("data:image/", "").replace(";base64", "").toLowerCase();
            }

            // Tạo tên file ngẫu nhiên
            String fileName = UUID.randomUUID().toString() + "." + fileExtension;
            String s3Path = type + "/" + fileName;

            // Chuyển Base64 thành byte array
            byte[] imageBytes = Base64.getDecoder().decode(imgBase64);
            InputStream inputStream = new ByteArrayInputStream(imageBytes);

            // Tạo metadata cho file
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(imageBytes.length);
            metadata.setContentType("image/" + fileExtension);

            // Upload file lên S3
            AmazonS3 s3Client = getS3Client();
            PutObjectRequest request = new PutObjectRequest(bucketName, s3Path, inputStream, metadata);

            // Thực hiện upload
            s3Client.putObject(request);

            // Trả về URL của file vừa upload
            return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + s3Path;
        } catch (Exception e) {
            log.error("Error uploading image to S3: {}", e.getMessage(), e);
            return null;
        }
    }
}
