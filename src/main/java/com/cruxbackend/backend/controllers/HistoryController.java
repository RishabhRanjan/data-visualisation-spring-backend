package com.cruxbackend.backend.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cruxbackend.backend.model.History;
import com.cruxbackend.backend.service.HistoryService;

@RestController
public class HistoryController {

  @Autowired
  private HistoryService service;

  @GetMapping("/history")
  public History getChatHistory(@RequestParam("cid") String cid) {
    return service.getHistory(Long.parseLong(cid));
  }

  @PostMapping("/feedback")
  public void addFeedback(@RequestParam("hid") String hid, @RequestParam("date") String date,
      @RequestParam("chatId") String chatId, @RequestParam("action") String action,
      @RequestParam("value") String value) {
    service.addFeedback(Long.parseLong(hid), Long.parseLong(date), UUID.fromString(chatId), action, value);
  }
}
