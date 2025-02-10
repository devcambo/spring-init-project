package com.devcambo.springinit.model.dto.request;

import com.devcambo.springinit.validation.UniqueBucket;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

@Schema(name = "BucketCreationDto", description = "Bucket creation request schema")
public record BucketCreationDto(
  @NotEmpty(message = "Bucket name is required!")
  @UniqueBucket
  @Schema(description = "Bucket name", example = "my-bucket")
  String bucketName
) {}
