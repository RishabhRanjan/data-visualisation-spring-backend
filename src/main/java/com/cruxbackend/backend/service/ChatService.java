package com.cruxbackend.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cruxbackend.backend.model.Chat;
import com.cruxbackend.backend.model.History;
import com.cruxbackend.backend.repo.ChatRepository;

@Service
public class ChatService {
  @Autowired
  private ChatRepository chatrepo;

  @Autowired
  private HistoryService historyService;

  public List<Chat> getChats(Long uid){
    return chatrepo.findByUid(uid);
  }

  public Chat createChat(Long userId,MultipartFile csv){
    Chat newChat = new Chat();
    newChat.setUid(userId);
    newChat.setTitle(csv.getOriginalFilename());    
    newChat.setDeleted(false);
    Chat addedChat = chatrepo.save(newChat);
    historyService.createHistory(addedChat.getId(), csv);
    return addedChat;
  }

  public void editChatTitle(Long chatId, String title){
    Optional<Chat> chatOptional = chatrepo.findById(chatId);
    if(chatOptional.isPresent()){
      Chat chat = chatOptional.get();
      chat.setTitle(title);
      chatrepo.save(chat);
    }
  }

  public void deleteChat(Long cid){
    Optional<Chat> chatOptional = chatrepo.findById(cid);
    if(chatOptional.isPresent()){
      Chat chat = chatOptional.get();
      chat.setDeleted(true);
      chatrepo.save(chat);
    }
  }

  public History getQueryResponse(Long hid, String query){
    return historyService.addThread(hid, query);
  }
}
