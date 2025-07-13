package com.practise.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecuredController {

  @GetMapping("/secured")
  public String getSecured() {
    return "Success";
  }
}
