package com.practise.security.api.controller;

import com.practise.security.api.dto.CardData;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/secured")
public class SecuredController {

  @GetMapping
  public String getSecured() {
    return "Success";
  }

  @PostMapping
  public String printString(@RequestBody String value) {
    return value;
  }

  @GetMapping("/cards")
  public ResponseEntity<List<CardData>> getCards() {
    List<CardData> cards =
        List.of(
            new CardData(
                UUID.randomUUID().toString(),
                "Total Revenue",
                "$1,250.00",
                "Revenue",
                new CardData.Trend("+12.5%", true),
                new CardData.Footer("Trending up this month", "Visitors for the last 6 months")),
            new CardData(
                UUID.randomUUID().toString(),
                "New Customers",
                "1,234",
                "Customers",
                new CardData.Trend("-20%", false),
                new CardData.Footer("Down 20% this period", "Acquisition needs attention"))
//            new CardData(
//                UUID.randomUUID().toString(),
//                "Active Accounts",
//                "45,678",
//                "Accounts",
//                new CardData.Trend("+12.5%", true),
//                new CardData.Footer("Strong user retention", "Engagement exceed targets")),
//            new CardData(
//                UUID.randomUUID().toString(),
//                "Growth Rate",
//                "4.5%",
//                "Growth",
//                new CardData.Trend("-1%", false),
//                new CardData.Footer("Steady performance increase", "Meets growth projections"))
                );

    return ResponseEntity.ok(cards);
  }

  @GetMapping("/subscribed/cards")
  public ResponseEntity<List<CardData>> getSubscribedCards() {
    List<CardData> subscribedCards =
        List.of(
            new CardData(
                UUID.randomUUID().toString(),
                "Active Accounts",
                "45,678",
                "Accounts",
                new CardData.Trend("+12.5%", true),
                new CardData.Footer("Strong user retention", "Engagement exceed targets")),
            new CardData(
                UUID.randomUUID().toString(),
                "Growth Rate",
                "4.5%",
                "Growth",
                new CardData.Trend("-1%", false),
                new CardData.Footer("Steady performance increase", "Meets growth projections")));

    return ResponseEntity.ok(subscribedCards);
  }
}
