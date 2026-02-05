package br.gov.mt.seplag.backend.service;

import org.springframework.web.multipart.MultipartFile;

public interface MinioStorageService {

    String upload(MultipartFile file, String folder);

    String generatePresignedUrl(String objectName, Integer expiry);
}
