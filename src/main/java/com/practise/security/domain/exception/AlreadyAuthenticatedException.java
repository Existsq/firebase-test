package com.practise.security.domain.exception;

import org.springframework.security.core.AuthenticationException;

public class AlreadyAuthenticatedException extends AuthenticationException {
  public AlreadyAuthenticatedException(String msg) {
    super(msg);
  }
}
