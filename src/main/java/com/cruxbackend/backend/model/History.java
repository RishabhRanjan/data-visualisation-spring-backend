package com.cruxbackend.backend.model;

import java.util.List;

import org.hibernate.annotations.ColumnTransformer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import jakarta.persistence.GenerationType;

@Data
@Entity
public class History {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private long cid;

  @Column(columnDefinition = "json")
  @ColumnTransformer(write = "?::json")
  private String history;

  @Column(columnDefinition = "json")
  @ColumnTransformer(write = "?::json")
  private String csv;
}
