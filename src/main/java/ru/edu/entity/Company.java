package ru.edu.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
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
  // orphanRemoval - что делать с сущностью на уровне БД, если удалим ее из коллекции users на уровне java
  @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
  // можем указывать колонку в дочерней сущности явно, если нет mappedBy
//  @JoinColumn(name = "company_id")
  @Builder.Default // чтобы установить дефолтное значение из поле при использовании билдера
  private Set<User> users = new HashSet<>(); // new HashSet - чтобы не проверять на null

  @Builder.Default
  @ElementCollection // подключает embeddable класс в коллекцию
  // указываем таблицу и внешний ключ в ней
  @CollectionTable(name = "company_locale", joinColumns = @JoinColumn(name = "company_id"))
  @AttributeOverride(name = "language", column = @Column(name = "lang"))
  private List<LocaleInfo> locales = new ArrayList<>();

  @Builder.Default
  @ElementCollection
  @CollectionTable(name = "company_locale", joinColumns = @JoinColumn(name = "company_id"))
  @Column(name = "description")
  // используем одно поле из таблицы справочника (только на чтение)
  private List<String> localeDescriptions = new ArrayList<>();

  public void addUser(User user) {
    users.add(user);
    user.setCompany(this);
  }

}
