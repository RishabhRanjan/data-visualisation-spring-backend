package com.cruxbackend.backend.controllers;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cruxbackend.backend.model.Chat;
import com.cruxbackend.backend.model.History;
import com.cruxbackend.backend.service.ChatService;


@RestController
public class ChatController {
  
  @Autowired
  private ChatService chatservice;
  
  @PostMapping("/new-chat")
  public Chat createNewChat(@RequestParam("file") MultipartFile csvData, @RequestParam("uid") String uid ){
    return chatservice.createChat(Long.parseLong(uid),csvData);
  }

  @GetMapping("/chats")
  public List<Chat> getChats(@RequestParam("uid") String uid){
    List<Chat> result = chatservice.getChats(Long.parseLong(uid));
    result.removeIf(entry->(Boolean) entry.isDeleted());
    Collections.sort(result,(o1,o2)->o2.getC().compareTo(o1.getC()));
    return result;
  }

  @PostMapping("/ask")
  public History getResponse(@RequestParam("hid") String hid,@RequestParam("query") String query){
    return chatservice.getQueryResponse(Long.parseLong(hid), query);
  }

  @GetMapping("/delete-chat")
  public void deleteChat(@RequestParam("cid") String cid){
    chatservice.deleteChat(Long.parseLong(cid));
  }

  @PostMapping(value="/edit-title")
  public void postMethodName(@RequestParam("cid") String cid, @RequestParam("new_title") String newTitle) {
    chatservice.editChatTitle(Long.parseLong(cid), newTitle);
  }
  
}
