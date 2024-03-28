package com.pintogether.backend.dto;

import com.pintogether.backend.service.AmazonS3Service;

import java.util.List;

public class S3PinPresignedURLReponseDTO {

    List<AmazonS3Service.AmazonS3Response> responseList;

}
