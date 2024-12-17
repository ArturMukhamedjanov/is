package islab1.services;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;

@Service
public class MinioService {
    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    public void uploadFile(String fileName, MultipartFile file) throws Exception {
        // Ensure the bucket exists
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
        // Upload the file
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );
    }

    public void deleteFile(String fileName) throws Exception {
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!found) {
            throw new IllegalStateException("Bucket " + bucketName + " does not exist.");
        }
        minioClient.removeObject(
            RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build()
        );
    }

    public byte[] downloadFile(String fileName) throws Exception {
        // Ensure the bucket exists
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!found) {
            throw new IllegalStateException("Bucket " + bucketName + " does not exist.");
        }
        // Get the file as an InputStream
        InputStream stream = minioClient.getObject(
            GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build()
        );
        // Convert InputStream to byte array
        byte[] fileBytes = stream.readAllBytes();
        stream.close();
        return fileBytes;
    }
}
