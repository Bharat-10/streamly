package com.bharat.streamly.service;

import com.bharat.streamly.exception.FileStorageException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile file, String folder) {
        String key = folder + "/" + UUID.randomUUID() + extensionOf(file.getOriginalFilename());
        PutObjectRequest request = PutObjectRequest.builder().bucket(bucket).key(key).contentType(contentTypeOf(file.getOriginalFilename())).build();
        try {
            s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        } catch (IOException e) {
            throw new FileStorageException("Could not store the file");
        }
        return key;
    }

    public String contentTypeOf(String fileName){
        Optional<MediaType> mediaType = MediaTypeFactory.getMediaType(fileName);
        if(mediaType.isEmpty()){
            return "application/octet-stream";
        }
        return mediaType.get().toString();
    }

    private String extensionOf(String fileName) {
        if (fileName == null) {
            return "";
        }
        int i = fileName.lastIndexOf(".");
        if (i == -1) {
            return "";
        }
        return fileName.substring(i);
    }


    public String getPresignedUrl(String key){
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofHours(1))
                .getObjectRequest(getObjectRequest)
                .build();
        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }

}
