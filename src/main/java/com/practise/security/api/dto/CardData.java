package com.practise.security.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardData {
  private String id;
  private String title;
  private Object value; // String или Number
  private String description;
  private Trend trend;
  private Footer footer;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Trend {
    private String value;
    @JsonProperty("isPositive")
    private boolean isPositive;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Footer {
    private String text;
    private String subtext;
  }
}
