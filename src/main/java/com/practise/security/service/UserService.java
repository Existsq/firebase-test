package com.practise.security.service;

import com.practise.security.model.AuthUser;
import com.practise.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  public boolean existsByEmail(String email) {
    log.trace("Trying to find user with email {}", email);
    return userRepository.findByEmail(email).isPresent();
  }

  public void save(AuthUser user) {
    log.trace("Saving user {} to database", user);
    userRepository.save(user);
  }
}
