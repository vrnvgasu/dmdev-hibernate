package ru.edu.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Profile {

  @Id
  @Column(name = "user_id")
  private Long id;

  private String street;

  private String language;

  @OneToOne
  // можно по старинке JoinColumn
//  @JoinColumn(name = "user_id ")
  // а можно явно сказать, что первичный и внешний ключ в этой таблице совпадают
  @PrimaryKeyJoinColumn
  private User user;

  public void setUser(User user) {
    this.user = user;
    this.id = user.getId();

    user.setProfile(this);
  }

}
