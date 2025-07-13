package com.practise.security.provider;

import com.practise.security.model.security.jwt.JwtAuthenticationToken;
import com.practise.security.model.security.jwt.JwtToken;
import com.practise.security.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

  private final JwtService jwtService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    log.trace("Attempting to validate JWT token");

    JwtToken token = (JwtToken) authentication.getCredentials();
    if (!jwtService.isTokenValid(token)) {
      log.warn("JWT token validation failed: invalid token");
      throw new BadCredentialsException("Invalid token");
    }

    UserDetails user = jwtService.extractUserDetails(token);
    log.info("JWT token is valid. Authenticated user: {}", user.getUsername());

    JwtAuthenticationToken authToken =
        new JwtAuthenticationToken(user, token, user.getAuthorities());
    log.debug("Created JwtAuthenticationToken for user: {}", user.getUsername());

    return authToken;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return JwtAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
