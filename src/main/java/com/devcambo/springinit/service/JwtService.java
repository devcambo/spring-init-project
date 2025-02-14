package com.devcambo.springinit.service;

import com.devcambo.springinit.constant.JWTConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtService {

  public String generateToken(String userEmail, String authorities) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("authorities", authorities);
    return createToken(claims, userEmail);
  }

  private String createToken(Map<String, Object> claims, String userEmail) {
    return Jwts
      .builder()
      .subject(userEmail)
      .issuer("https://devcambo.com")
      .issuedAt(new Date())
      .expiration(
        new Date(System.currentTimeMillis() + JWTConstant.JWT_TOKEN_EXPIRATION_TIME)
      )
      .claims(claims)
      .signWith(
        Keys.hmacShaKeyFor(JWTConstant.JWT_TOKEN_SECRET.getBytes(StandardCharsets.UTF_8))
      )
      .compact();
  }

  public String extractSubject(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts
      .parser()
      .verifyWith(
        Keys.hmacShaKeyFor(JWTConstant.JWT_TOKEN_SECRET.getBytes(StandardCharsets.UTF_8))
      )
      .build()
      .parseSignedClaims(token)
      .getPayload();
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public Boolean validateToken(String token, UserDetails userDetails) {
    final String email = extractSubject(token);
    return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }
}
