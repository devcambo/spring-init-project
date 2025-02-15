package com.devcambo.springinit.service;

import com.devcambo.springinit.constant.JWTConstant;
import com.devcambo.springinit.exception.JwtTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtService {

  @Autowired
  private Environment environment;

  private String getSecretKey() {
    return environment.getProperty(
      JWTConstant.JWT_TOKEN_SECRET,
      JWTConstant.JWT_TOKEN_DEFAULT_SECRET
    );
  }

  public String generateToken(String userEmail, String authorities) {
    try {
      Map<String, Object> claims = new HashMap<>();
      claims.put("authorities", authorities);
      return createToken(claims, userEmail);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  private String createToken(Map<String, Object> claims, String userEmail) {
    try {
      return Jwts
        .builder()
        .subject(userEmail)
        .issuer("https://devcambo.com")
        .issuedAt(new Date())
        .expiration(
          new Date(System.currentTimeMillis() + JWTConstant.JWT_TOKEN_EXPIRATION_TIME)
        )
        .claims(claims)
        .signWith(Keys.hmacShaKeyFor(getSecretKey().getBytes(StandardCharsets.UTF_8)))
        .compact();
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  public String extractSubject(String token) {
    try {
      return extractClaim(token, Claims::getSubject);
    } catch (JwtException e) {
      throw new JwtTokenException(e.getMessage(), e);
    }
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    try {
      final Claims claims = extractAllClaims(token);
      return claimsResolver.apply(claims);
    } catch (JwtException e) {
      throw new JwtTokenException(e.getMessage(), e);
    }
  }

  private Claims extractAllClaims(String token) {
    try {
      return Jwts
        .parser()
        .verifyWith(Keys.hmacShaKeyFor(getSecretKey().getBytes(StandardCharsets.UTF_8)))
        .build()
        .parseSignedClaims(token)
        .getPayload();
    } catch (JwtException e) {
      throw new JwtTokenException(e.getMessage(), e);
    }
  }

  public Date extractExpiration(String token) {
    try {
      return extractClaim(token, Claims::getExpiration);
    } catch (JwtException e) {
      throw new JwtTokenException(e.getMessage(), e);
    }
  }

  private Boolean isTokenExpired(String token) {
    try {
      return extractExpiration(token).before(new Date());
    } catch (JwtException e) {
      throw new JwtTokenException(e.getMessage(), e);
    }
  }

  public Boolean validateToken(String token, UserDetails userDetails) {
    try {
      final String email = extractSubject(token);
      return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    } catch (JwtException e) {
      throw new JwtTokenException(e.getMessage(), e);
    }
  }
}
