package dcc.formationservice.Service;



import dcc.formationservice.shared.CustomResponseException;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import io.minio.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;



@Service
@Slf4j
public class MinIOService {

    private MinioClient minioClient;

    public  MinIOService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Value("${minio.bucket.name:formations}")
    private String bucketName;

    @Value("${minio.url}")
    private String minioUrl;


    public String uploadFile(MultipartFile file, String folder) {
        try {
            // Create the bucket if it doesn’t exist
            createBucketIfNotExists();

            // Generate a unique filename
            String fileName = generateFileName(file.getOriginalFilename(), folder);

            // Upload the file
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            log.info("File uploaded successfully: {}", fileName);
            return getFileUrl(fileName);

        } catch (Exception e) {
            log.error("Error while uploading the file", e);
            throw CustomResponseException.InternalError("Error while uploading the file " + e.getMessage());
        }
    }


    public void deleteFile(String fileUrl) {
        try {
            String objectName = extractObjectNameFromUrl(fileUrl);
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            log.info("File deleted successfully: {}", objectName);
        } catch (Exception e) {
            log.error("Error while deleting the file", e);
            throw CustomResponseException.InternalError("Error while deleting the file");
        }
    }


//    Download a file from MinIO

    public InputStream downloadFile(String fileUrl) {
        try {
            String objectName = extractObjectNameFromUrl(fileUrl);
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            log.error("Error downloading file", e);
            throw CustomResponseException.ResourceNotFound("file not found");
        }
    }

    public boolean fileExists(String fileUrl) {
        try {
            String objectName = extractObjectNameFromUrl(fileUrl);
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void createBucketIfNotExists() throws Exception {
        boolean found = minioClient.bucketExists(
                BucketExistsArgs.builder()
                        .bucket(bucketName)
                        .build()
        );

        if (!found) {
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(bucketName)
                            .build()
            );
            log.info("Bucket créé: {}", bucketName);
        }
    }


    private String generateFileName(String originalFilename, String folder) {
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return folder + "/" + UUID.randomUUID() + extension;
    }


    private String getFileUrl(String objectName) {
        return minioUrl + "/" + bucketName + "/" + objectName;
    }


    private String extractObjectNameFromUrl(String fileUrl) {
        if (fileUrl == null || !fileUrl.contains(bucketName)) {
            throw CustomResponseException.BadRequest("Invalid file URL");
        }
        return fileUrl.substring(fileUrl.indexOf(bucketName) + bucketName.length() + 1);
    }
}
