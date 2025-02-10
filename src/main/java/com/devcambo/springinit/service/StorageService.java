package com.devcambo.springinit.service;

import io.awspring.cloud.s3.S3Resource;
import java.net.URL;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
  /**
   * Creates a new bucket with the specified name.
   *
   * @param bucketName the name of the bucket to create
   * @return the name of the created bucket
   */
  String createBucket(String bucketName);

  /**
   * Deletes the bucket with the specified name.
   *
   * @param bucketName the name of the bucket to delete
   */
  void deleteBucket(String bucketName);

  /**
   * Uploads the specified file to the storage service.
   *
   * @param multipartFile the file to upload
   * @return the URL of the uploaded file
   */
  String upload(MultipartFile multipartFile);

  /**
   * Deletes the object with the specified file name.
   *
   * @param fileName the name of the file to delete
   */
  void deleteObject(String fileName);

  /**
   * Downloads the object with the specified file name.
   *
   * @param fileName the name of the file to download
   * @return the resource representing the downloaded file
   */
  S3Resource download(String fileName);

  /**
   * Generates a signed URL for the object with the specified file name.
   *
   * @param fileName the name of the file to generate the URL for
   * @return the URL for the object
   */
  URL createSignedGetURL(String fileName);
}
