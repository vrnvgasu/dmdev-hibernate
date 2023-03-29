package ru.edu.entity;

import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EqualsAndHashCode(exclude = {"user", "chat"})
@Table(name = "users_chat")
public class UserChat {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "chat_id")
  private Chat chat;

  @Column(name = "created_at")
  // java.time.Instant мапится на timestamp
  private Instant createdAt;

  @Column(name = "created_by")
  private String createdBy;

  public void setUser(User user) {
    this.user = user;
    this.user.getUserChats().add(this);
  }

  public void setChat(Chat chat) {
    this.chat = chat;
    this.chat.getUserChats().add(this);
  }

}