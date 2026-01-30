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
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MinioStorageServiceImpl implements MinioStorageService {

    private final MinioClient minioClient;
    private final MinioConfig minioConfig;

    @Override
    public String upload(MultipartFile file) {
        try {
            String objectName = UUID.randomUUID() + "-" + file.getOriginalFilename();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            log.info("Arquivo enviado ao MinIO: {}", objectName);
            return objectName;

        } catch (Exception e) {
            log.error("Erro ao fazer upload no MinIO", e);
            throw new RuntimeException("Erro ao salvar imagem da capa");
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

