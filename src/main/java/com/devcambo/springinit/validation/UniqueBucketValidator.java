package com.devcambo.springinit.validation;

import io.awspring.cloud.s3.S3Template;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UniqueBucketValidator implements ConstraintValidator<UniqueBucket, String> {

  private final S3Template s3Template;

  @Override
  public boolean isValid(
    final String bucketName,
    final ConstraintValidatorContext context
  ) {
    return !s3Template.bucketExists(bucketName);
  }
}
