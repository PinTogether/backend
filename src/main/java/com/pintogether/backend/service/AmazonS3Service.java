package com.pintogether.backend.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
@Transactional
public class AmazonS3Service {
    private final AmazonS3 amazonS3;
    private final String bucket;

    public AmazonS3Service(AmazonS3 amazonS3, @Value("${cloud.aws.s3.bucket}") String bucket) {
        this.amazonS3 = amazonS3;
        this.bucket = bucket;
    }

    private String getFormattedDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss'KST'");
        return simpleDateFormat.format(new Date());
    }
    private String makeObjectKey(String domainType, Long id, String extension) {
        return domainType + ":" + id + "-" + getFormattedDate() + "-" + UUID.randomUUID().toString() + "." + extension;
    }

    private Date getPresignedUrlExpiration() {
        Date expiration = new Date();
        expiration.setTime(expiration.getTime() + 1000 * 60);
        return expiration;
    }

    private String convertPresignedUrlToImageUrl(String presignedUrl) {
        return URLDecoder.decode(presignedUrl.toString().split("\\?")[0], StandardCharsets.UTF_8);
    }
    public AmazonS3Response getneratePresignedUrlAndImageUrl(String contentType, String domainType, Long id) {
        String objectKey = makeObjectKey(domainType, id, contentType.split("/")[1]);
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, objectKey)
                .withMethod(HttpMethod.PUT)
                .withExpiration(getPresignedUrlExpiration())
                .withContentType(contentType);
        String presignedUrl = amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();
        String imageUrl = convertPresignedUrlToImageUrl(presignedUrl);
        return new AmazonS3Response(id, presignedUrl, imageUrl);
    }

    public void deleteS3Image(String objectKey) {
        amazonS3.deleteObject(bucket, objectKey);
    }

    @AllArgsConstructor
    @Getter
    public static class AmazonS3Response {
        private Long id;
        private String presignedUrl;
        private String imageUrl;
    }
}
