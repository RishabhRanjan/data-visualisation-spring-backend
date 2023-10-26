package com.cruxbackend.backend.pojo;

import java.io.Serializable;
import java.util.List;

public class MessageBlock implements Serializable{
  private Long date;
  private List<Message> thread;

  public MessageBlock() {
  }

  public MessageBlock(Long date, List<Message> thread) {
      this.date = date;
      this.thread = thread;
  }

  public Long getDate() {
    return date;
  }
  public void setDate(Long date) {
    this.date = date;
  }
  public List<Message> getThread() {
    return thread;
  }
  public void setThread(List<Message> thread) {
    this.thread = thread;
  }
}
