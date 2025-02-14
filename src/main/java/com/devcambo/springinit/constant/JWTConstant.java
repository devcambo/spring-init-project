package com.devcambo.springinit.constant;

public final class JWTConstant {

  public static final String JWT_TOKEN_HEADER = "Authorization";
  public static final String JWT_TOKEN_PREFIX = "Bearer ";
  public static final String JWT_TOKEN_SECRET =
    "71b2b651807c3476080fc5d068979c9b7df45507dfc8bb69e2523c5121b1c701";
  public static final int JWT_TOKEN_EXPIRATION_TIME = 1000 * 60 * 30; // 30 minutes

  private JWTConstant() {}
}
