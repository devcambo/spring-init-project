package com.devcambo.springinit.model.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(
  name = "ErrorResponse",
  description = "Schema for error response. This schema is used to represent error responses from the API."
)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ErrorInfo {

  @Schema(description = "Flag indicating whether the response is an error or not")
  private boolean flag;

  @Schema(description = "HTTP status code of the error response")
  private int code;

  @Schema(description = "Human-readable message describing the error")
  private String message;

  @Schema(description = "Actual error object returned in the response")
  private Object error;

  public ErrorInfo(boolean flag, int code, String message) {
    this.flag = flag;
    this.code = code;
    this.message = message;
  }
}
