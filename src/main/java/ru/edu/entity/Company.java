package ru.edu.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "users")
@EqualsAndHashCode(exclude = "users")
@Entity
public class Company {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  // если есть @ManyToOne у дочерней сущности, то можем просто указать mappedBy
  @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
  // можем указывать колонку в дочерней сущности явно, если нет mappedBy
//  @JoinColumn(name = "company_id")
  @Builder.Default // чтобы установить дефолтное значение из поле при использовании билдера
  private Set<User> users = new HashSet<>(); // new HashSet - чтобы не проверять на null

  public void addUser(User user) {
    users.add(user);
    user.setCompany(this);
  }

}
