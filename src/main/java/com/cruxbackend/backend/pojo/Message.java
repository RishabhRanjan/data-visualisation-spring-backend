package com.cruxbackend.backend.pojo;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable {
  public enum MessageType {
    CSV_UPLOAD, QUERY, CHART
  }

  public enum RoleType {
    ASSISTANT, HUMAN
  }

  public enum MessageAction {
    LIKED, FEEDBACK
  }

  private UUID uuid;
  private Long time;
  private MessageType type;
  private RoleType role;
  private Object content;
  private int liked;
  private String feedback;

  public Message() {
    this.liked = 0;
    this.feedback = "";
  }

  public Message(Long time, MessageType type, RoleType role, Object content, UUID uuid) {
    this.time = time;
    this.type = type;
    this.role = role;
    this.content = content;
    this.uuid = uuid;
    this.liked = 0;
    this.feedback = "";
  }

  public void setLiked(int liked) {
    this.liked = liked;
  }

  public int getLiked() {
    return liked;
  }

  public void setFeedback(String feedback) {
    this.feedback = feedback;
  }

  public String getFeedback() {
    return feedback;
  }

  public UUID getUuid() {
    return uuid;
  }

  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

  public Long getTime() {
    return time;
  }

  public void setTime(Long time) {
    this.time = time;
  }

  public MessageType getType() {
    return type;
  }

  public void setType(MessageType type) {
    this.type = type;
  }

  public RoleType getRole() {
    return role;
  }

  public void setRole(RoleType role) {
    this.role = role;
  }

  public Object getContent() {
    return content;
  }

  public void setContent(Object content) {
    this.content = content;
  }
}
