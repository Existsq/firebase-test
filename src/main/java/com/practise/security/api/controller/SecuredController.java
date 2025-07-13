package com.practise.security.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecuredController {

  @GetMapping("/secured")
  public String getSecured() {
    return "Success";
  }

  @PostMapping("/secured")
  public String printString(@RequestBody String value) {
    return value;
  }
}
