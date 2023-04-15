package ru.edu.entity;

import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
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
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.edu.listener.UserChatListener;

@Data
//@AllArgsConstructor
@NoArgsConstructor
//@Builder
@Entity
@ToString(exclude = {"user", "chat"})
@EqualsAndHashCode(exclude = {"user", "chat"})
@Table(name = "users_chat")
@EntityListeners(UserChatListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserChat extends AuditableEntity<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  public UserChat(Instant createdAt, Instant updatedAt, String createdBy) {
    super(createdAt, updatedAt, createdBy);
  }

  @Builder
  public UserChat(Long id, User user, Chat chat, Instant createdAt, Instant updatedAt, String createdBy) {
    super(createdAt, updatedAt, createdBy);
    this.id = id;
    this.user = user;
    this.chat = chat;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "chat_id")
  private Chat chat;

  public void setUser(User user) {
    this.user = user;
    this.user.getUserChats().add(this);
  }

  public void setChat(Chat chat) {
    this.chat = chat;
    this.chat.getUserChats().add(this);
  }

}
