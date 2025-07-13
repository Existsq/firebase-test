package com.practise.security.api.dto;

import lombok.Data;

@Data
public class LoginRequest {
  private String email;
  private String password;
}
