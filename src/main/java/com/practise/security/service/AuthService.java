package com.practise.security.service;

import com.practise.security.model.AuthUser;
import com.practise.security.model.security.jwt.JwtToken;
import com.practise.security.model.security.web.request.RegistrationRequest;
import com.practise.security.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public JwtToken register(RegistrationRequest request) {
    log.info("Register attempt for email: {}", request.getEmail());
    if (userService.existsByEmail(request.getEmail())) {
      log.warn("Registration failed: user with email {} already exists", request.getEmail());
      throw new IllegalArgumentException("User with this email exists");
    }

    AuthUser user = new AuthUser();
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setEnabled(true);

    userService.save(user);
    log.info("User registered successfully: {}", request.getEmail());

    JwtToken token = jwtService.generateToken(user);
    log.debug("JWT token generated for user: {}", request.getEmail());

    return token;
  }
}
