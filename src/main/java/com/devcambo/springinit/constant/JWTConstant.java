package com.devcambo.springinit.constant;

public final class JWTConstant {

  public static final String JWT_TOKEN_HEADER = "Authorization";
  public static final String JWT_TOKEN_PREFIX = "Bearer ";
  public static final String JWT_TOKEN_SECRET = "JWT_SECRET"; //TODO: set JWT_SECRET as env when deploy to prod
  public static final String JWT_TOKEN_DEFAULT_SECRET =
    "c7c3eaa333d372ebbbf1565423889356966194479630014fc1c505bf47657b03";
  public static final int JWT_TOKEN_EXPIRATION_TIME = 1000 * 60 * 30; // 30 minutes

  private JWTConstant() {}
}
