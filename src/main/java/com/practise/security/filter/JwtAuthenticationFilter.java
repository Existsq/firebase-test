package com.practise.security.filter;

import com.practise.security.model.security.jwt.JwtAuthenticationToken;
import com.practise.security.model.security.jwt.JwtToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final AuthenticationManager authenticationManager;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {

    String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      log.trace("Request does not contain Bearer Authorization header");
      filterChain.doFilter(request, response);
      return;
    }

    String tokenValue = authHeader.substring(7);
    JwtToken token = new JwtToken(tokenValue);
    log.debug("Received JWT token for authentication");

    JwtAuthenticationToken authRequest = new JwtAuthenticationToken(token);
    log.trace("Created JwtAuthenticationToken from token");

    try {
      Authentication authResult = authenticationManager.authenticate(authRequest);
      SecurityContextHolder.getContext().setAuthentication(authResult);
      log.info("JWT token authenticated successfully for user: {}", authResult.getName());
    } catch (AuthenticationException ex) {
      SecurityContextHolder.clearContext();
      log.warn("JWT authentication failed: {}", ex.getMessage());
    }

    filterChain.doFilter(request, response);
  }
}
