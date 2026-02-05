package br.gov.mt.seplag.backend.service.impl;

import br.gov.mt.seplag.backend.config.MinioConfig;
import br.gov.mt.seplag.backend.service.MinioStorageService;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class MinioStorageServiceImpl implements MinioStorageService {

    private final MinioClient minioClient;
    private final MinioConfig minioConfig;

    @Override
    public String upload(MultipartFile file, String folder) {
        try {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String objectKey = folder + "/" + fileName;

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .object(objectKey)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            return objectKey;
        } catch (Exception e) {
            log.error("Erro ao fazer upload para o MinIO: {}", e.getMessage());
            throw new RuntimeException("Não foi possível salvar o arquivo.");
        }
    }

    @Override
    public String generatePresignedUrl(String objectName, Integer expiry) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .object(objectName)
                            .method(Method.GET)
                            .expiry((int) Duration.ofMinutes(30).toSeconds())
                            .build()
            );
        } catch (Exception e) {
            log.error("Erro ao gerar URL assinada", e);
            throw new RuntimeException("Erro ao gerar URL da imagem");
        }
    }
}

