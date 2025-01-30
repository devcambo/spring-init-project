package com.devcambo.springinit.model.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(
  name = "APIResponse",
  description = "Standardized response object for API calls, containing a success flag, HTTP status code, human-readable message, and optional data payload."
)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class APIResponse {

  @Schema(description = "Response flag indicating success or failure")
  private boolean flag;

  @Schema(description = "HTTP status code of the response")
  private int code;

  @Schema(description = "Human-readable message describing the response")
  private String message;

  @Schema(description = "Actual data returned in the response")
  private Object data;

  public APIResponse(boolean flag, int code, String message) {
    this.flag = flag;
    this.code = code;
    this.message = message;
  }
}
