package com.practise.security.infrastructure.security.jwt;

import com.practise.security.domain.model.AuthUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JwtService {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration-ms:86400000}")
  private long expiration;

  private final SecretKey secretKey;

  public JwtService(@Value("${jwt.secret}") String secret) {
    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  public String extractUsername(JwtToken token) {
    log.trace("Extracting username from token");
    return extractClaim(token, Claims::getSubject);
  }

  public boolean isTokenValid(JwtToken token, UserDetails userDetails) {
    try {
      final String username = extractUsername(token);
      boolean valid = username.equals(userDetails.getUsername()) && !isTokenExpired(token);
      log.trace("Token validity for user {}: {}", username, valid);
      return valid;
    } catch (Exception e) {
      log.warn("Token validation failed: {}", e.getMessage());
      return false;
    }
  }

  public boolean isTokenValid(JwtToken token) {
    try {
      boolean valid = !isTokenExpired(token);
      log.trace("Token self-validation result: {}", valid);
      return valid;
    } catch (Exception e) {
      log.warn("Token validation error: {}", e.getMessage());
      return false;
    }
  }

  public Date extractExpiration(JwtToken token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public <T> T extractClaim(JwtToken token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(JwtToken token) {
    log.trace("Parsing claims from token");
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token.token())
        .getPayload();
  }

  private boolean isTokenExpired(JwtToken token) {
    boolean expired = extractExpiration(token).before(new Date());
    log.trace("Token expired: {}", expired);
    return expired;
  }

  public String generateToken(UserDetails userDetails) {
    log.trace("Generating JWT token for user {}", userDetails.getUsername());
    return Jwts.builder()
        .subject(userDetails.getUsername())
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(secretKey)
        .compact();
  }

  public UserDetails extractUserDetails(JwtToken token) {
    String username = extractUsername(token);
    log.trace("Extracted user details from token: {}", username);
    return new AuthUser(username);
  }
}
