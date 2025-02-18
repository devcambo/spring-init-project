package com.devcambo.springinit.service.impl;

import com.devcambo.springinit.constant.StorageConstant;
import com.devcambo.springinit.exception.ResourceNotFoundException;
import com.devcambo.springinit.service.StorageService;
import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Exception;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

  private final S3Template s3Template;

  @Override
  public String createBucket(String bucketName) {
    return s3Template.createBucket(bucketName);
  }

  @Override
  public void deleteBucket(String bucketName) {
    s3Template.deleteBucket(bucketName);
  }

  @Override
  public String upload(MultipartFile multipartFile) {
    try {
      String key = generateUniqueFileName(
        StringUtils.cleanPath(multipartFile.getOriginalFilename())
      );
      ObjectMetadata metadata = new ObjectMetadata.Builder()
        .contentType(multipartFile.getContentType())
        .contentLength(multipartFile.getSize())
        .build();
      InputStream inputStream = multipartFile.getInputStream();
      s3Template.upload(StorageConstant.BUCKET_NAME, key, inputStream, metadata);
      return key;
    } catch (Exception e) {
      throw new S3Exception(e.getMessage(), e.getCause().getCause());
    }
  }

  @Override
  public void deleteObject(String key) {
    getFileByName(key);
    s3Template.deleteObject(StorageConstant.BUCKET_NAME, key);
  }

  @Override
  public S3Resource download(String fileName) {
    getFileByName(fileName);
    return s3Template.download(StorageConstant.BUCKET_NAME, fileName);
  }

  @Override
  public URL createSignedGetURL(String fileName) {
    getFileByName(fileName);
    return s3Template.createSignedGetURL(
      StorageConstant.BUCKET_NAME,
      fileName,
      Duration.of(1, ChronoUnit.MINUTES)
    );
  }

  private void getFileByName(String key) {
    if (!s3Template.objectExists(StorageConstant.BUCKET_NAME, key)) {
      throw new ResourceNotFoundException("fileName", key);
    }
  }

  private String generateUniqueFileName(String originalImageName) {
    return String.format(
      "%s%s",
      UUID.randomUUID().toString(),
      originalImageName.substring(originalImageName.lastIndexOf("."))
    );
  }
}
