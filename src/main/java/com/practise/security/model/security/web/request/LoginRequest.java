package com.practise.security.model.security.web.request;

import lombok.Data;

@Data
public class LoginRequest {
  private String email;
  private String password;
}
