package com.devcambo.springinit.constant;

public final class SwaggerConstant {

  public static final String[] SWAGGER_WHITE_LIST_URL = {
    "/v2/api-docs",
    "/v3/api-docs",
    "/v3/api-docs/**",
    "/swagger-resources",
    "/swagger-resources/**",
    "/configuration/ui",
    "/configuration/security",
    "/swagger-ui/**",
    "/webjars/**",
    "/swagger-ui.html",
  };

  private SwaggerConstant() {}
}
