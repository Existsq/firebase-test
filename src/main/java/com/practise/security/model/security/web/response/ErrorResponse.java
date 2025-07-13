package com.practise.security.model.security.web.response;

import lombok.Data;

@Data
public class ErrorResponse {
  private final String error;
  private final String message;
}
