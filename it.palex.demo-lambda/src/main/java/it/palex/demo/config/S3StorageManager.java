/**
 * Copyright (c) Alessandro Pagliaro. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.palex.demo.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;
import java.time.Duration;

@ApplicationScoped
public class S3StorageManager {

    @Inject
    S3Client s3Client;

    @ConfigProperty(name = "app.aws.region")
    String region;

    @ConfigProperty(name = "app.storage_bucket_name")
    String bucketName;

    @ConfigProperty(name = "app.signed_url_default_expiration_seconds")
    int defaultLinkExpiration;

    public String generateUploadPresignedUrl(String subfolder, String filename){
        if(subfolder==null || filename==null){
            throw new NullPointerException();
        }

        final String s3ObjectKey = concatPath(subfolder, filename);

        return this.generateUploadPresignedUrl(s3ObjectKey, this.defaultLinkExpiration);
    }

    public String generateUploadPresignedUrl(String s3ObjectKey, int expirationSecondsOffset) {
        if(s3ObjectKey==null){
            throw new NullPointerException();
        }
        if(expirationSecondsOffset<=0){
            throw new IllegalArgumentException();
        }

        S3Presigner presigner = S3Presigner.builder()
                .region(Region.of(this.region))
                .build();

        Duration duration = Duration.ofSeconds(expirationSecondsOffset);


        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(this.bucketName)
                .key(s3ObjectKey)
                .build();

        PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(duration)
                .putObjectRequest(putObjectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(putObjectPresignRequest);

        URL signedUrl = presignedRequest.url();

        return signedUrl.toExternalForm();
    }

    public String generateDownloadPresignedUrl(String s3ObjectKey) {
        if(s3ObjectKey==null){
            throw new NullPointerException();
        }

        return this.generateDownloadPresignedUrl(s3ObjectKey, this.defaultLinkExpiration);
    }

    public String generateDownloadPresignedUrl(String s3ObjectKey, int expirationSecondsOffset){
        if(s3ObjectKey==null){
            throw new NullPointerException();
        }
        if(expirationSecondsOffset<=0){
            throw new IllegalArgumentException();
        }
        if(s3ObjectKey.startsWith("/")){
            s3ObjectKey = s3ObjectKey.substring(1);
        }

        S3Presigner presigner = S3Presigner.builder()
                .region(Region.of(this.region))
                .build();

        Duration duration = Duration.ofSeconds(expirationSecondsOffset);

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(this.bucketName)
                .key(s3ObjectKey)
                .build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(duration)
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(getObjectPresignRequest);

        URL signedUrl = presignedRequest.url();

        return signedUrl.toString();
    }

    /**
     *
     * @param path
     * @param subpath
     * @return the path concatenated
     * @throws NullPointerException
     */
    public static String concatPath(String path, String subpath) {
        if(path==null || subpath==null) {
            throw new NullPointerException();
        }
        String p1 = path.replaceAll("\\\\", "/");
        String p2 = subpath.replaceAll("\\\\", "/");

        if(p1.endsWith("/") && p2.startsWith("/")) {
            return p1.substring(0, p1.length() - 1) + p2;
        }

        if(p1.endsWith("/") || p2.startsWith("/")) {
            return p1 + p2;
        }

        return p1+"/"+p2;
    }

}
