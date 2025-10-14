package com.example.demo;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "todo")
public class Todo {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String title;
  private boolean completed;
  @Column(name="created_at", updatable=false, insertable=false)
  private java.time.LocalDateTime createdAt;
  // getters/setters
}
