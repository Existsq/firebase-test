package com.practise.security.model.security.web.response;

import com.practise.security.model.security.jwt.JwtToken;
import lombok.Data;

@Data
public class AuthSuccessResponse {
  private final JwtToken accessToken;
}
