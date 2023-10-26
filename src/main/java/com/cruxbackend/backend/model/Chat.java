package com.cruxbackend.backend.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.GenerationType;

@Data
@Entity
@Table(name = "chat", schema = "public")
public class Chat {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "c", updatable = false)
  @CreationTimestamp
  private LocalDateTime c;

  @Column(name = "u")
  @UpdateTimestamp
  private LocalDateTime u;
  
  private long uid;
  private String title;
  private boolean deleted;

}
