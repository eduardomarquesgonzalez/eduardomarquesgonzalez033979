package br.gov.mt.seplag.backend.service;

import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class MinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    public void ensureBucketExists() {
        try {
            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(bucketName)
                            .build()
            );

            if (!exists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(bucketName)
                                .build()
                );
                log.info("Bucket '{}' criado com sucesso", bucketName);
            }
        } catch (Exception e) {
            log.error("Erro ao verificar/criar bucket: {}", bucketName, e);
            throw new RuntimeException("Erro ao garantir existência do bucket", e);
        }
    }

    public String uploadFile(MultipartFile file) {
        ensureBucketExists();

        String objectKey = generateObjectKey(file.getOriginalFilename());

        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectKey)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            log.info("Arquivo '{}' enviado com sucesso como '{}'", file.getOriginalFilename(), objectKey);
            return objectKey;

        } catch (Exception e) {
            log.error("Erro ao fazer upload do arquivo: {}", file.getOriginalFilename(), e);
            throw new RuntimeException("Erro ao fazer upload do arquivo", e);
        }
    }

    public String getPresignedUrl(String objectKey) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectKey)
                            .expiry(7, TimeUnit.DAYS)
                            .build()
            );
        } catch (Exception e) {
            log.error("Erro ao gerar URL presigned para: {}", objectKey, e);
            throw new RuntimeException("Erro ao gerar URL de acesso ao arquivo", e);
        }
    }

    public boolean fileExists(String objectKey) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectKey)
                            .build()
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public ObjectStat getFileInfo(String objectKey) {
        try {
            StatObjectResponse response = minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectKey)
                            .build()
            );

            return ObjectStat.builder()
                    .objectKey(objectKey)
                    .contentType(response.contentType())
                    .size(response.size())
                    .build();

        } catch (Exception e) {
            log.error("Erro ao obter informações do arquivo: {}", objectKey, e);
            throw new RuntimeException("Erro ao obter informações do arquivo", e);
        }
    }

    /**
     * Gera uma chave única para o objeto
     */
    private String generateObjectKey(String originalFilename) {
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return UUID.randomUUID() + extension;
    }

    /**
     * Classe interna para informações do arquivo
     */
    @lombok.Data
    @lombok.Builder
    public static class ObjectStat {
        private String objectKey;
        private String contentType;
        private Long size;
    }
}