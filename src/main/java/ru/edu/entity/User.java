package ru.edu.entity;

import static ru.edu.util.StringUtils.SPACE;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedQuery;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FetchProfile;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

// POJO: @Data, @NoArgsConstructor, @AllArgsConstructor
// генерит пустой конструктор, геттеры, сеттеры,
// equals и хешкод, toString


// Можем выносить HQL запрос над сущностью. Не очень круто
@NamedQuery(name = "findUserByName",
  query = "select u from User u "
    + "where u.personalInfo.firstname = :firstName and u.company.name = :companyName " +
    "order by u.personalInfo.personalLastname desc")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"company", "userChats", "payments"}) // не делать select при отображении связи
@Entity // сущность хибернейта
//@EqualsAndHashCode(exclude = "profile")
@Table(name = "users", schema = "public") // нужна при SINGLE_TABLE
@TypeDef(name = "outTypeName", typeClass = JsonBinaryType.class)
// позволит подгружать связь company при запросе user по id
@FetchProfile(name = "withCompany", fetchOverrides = {
  @FetchProfile.FetchOverride(
    entity = User.class,
    association = "company", // название связи
    mode = FetchMode.JOIN // получим одним запрос с пользователем через join
  )
})
@FetchProfile(name = "withCompanyAndPayment", fetchOverrides = {
  @FetchProfile.FetchOverride(
    entity = User.class,
    association = "company", // название связи
    mode = FetchMode.JOIN // получим одним запрос с пользователем через join
  ),
  @FetchProfile.FetchOverride(
    entity = User.class,
    association = "payments", // название связи
    mode = FetchMode.JOIN // получим одним запрос с пользователем через join
  )
})
// тянем сразу 2 связи и одну подсвязь. Можно тянуть и связи связей при желании
@NamedEntityGraph(
  name = "WithCompanyAndChat",
  attributeNodes = {
    @NamedAttributeNode("company"),
    // коллекцию userChats и связь этой коллекции на chat
    @NamedAttributeNode(value = "userChats", subgraph = "withChats")
  },
  subgraphs = {
    @NamedSubgraph(name = "withChats", attributeNodes = @NamedAttributeNode("chat"))
  }
)
@NamedEntityGraph(
  name = "WithCompany",
  attributeNodes = {@NamedAttributeNode("company")}
)
// для использования кеша второго уровня
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Users")
public class User implements BaseEntity<Long>, Comparable<User> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Embedded
  // указали явный маппинг поля personalLastname к колонке lastname
  @AttributeOverride(name = "personalLastname", column = @Column(name = "lastname"))
//  @Valid // проверяет валидацию вложенных сущностей
  private PersonalInfo personalInfo;

  @Column(unique = true)
  private String username;

  @Enumerated(EnumType.STRING) // приводим ENUM к строке
  private Role role;

  //  @Type(type = "com.vladmihalcea.hibernate.type.json.JsonBinaryType")
//  @Type(type = "jsonb") // работает, т.к. в JsonBinaryType есть метод public String getName() {return "jsonb";}
  @Type(type = "outTypeName") // переопределили лаконичное название JsonBinaryType выше в @TypeDef
  private UUID info;

  // cascade - что делать со связью
  // PERSIST не логично, т.к. company - главная сущность в этом отношении
  // странно добавлять company к пользователю, пока компания не существует
  @ManyToOne(fetch = FetchType.LAZY /*cascade = CascadeType.ALL*/)
//  @BatchSize(size = 3) // для ManyToOne не работает
//  @Fetch(FetchMode.JOIN) // не работает
  @JoinColumn(name = "company_id")
  private Company company;

//  @OneToOne(
//    mappedBy = "user",
//    cascade = CascadeType.ALL,
//    // LAZY не сработает для сущности, у которой нет внешнего ключа на связь OneToOne
//    fetch = FetchType.LAZY
////    optional = false
//  )
//  private Profile profile;

  @Builder.Default // нельзя ставить для abstract
  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  // List не делает доп запросы перед insert (в отличие от Set)
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  private Set<UserChat> userChats = new HashSet<>();

  @Builder.Default
//  @Fetch(FetchMode.SUBSELECT)
  @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY)
  private List<Payment> payments = new ArrayList<>();

  @Override
  public int compareTo(User user) {
    // сделаем сортировку по username
    return username.compareTo(user.username);
  }

  public String fullName() {
    return getPersonalInfo().getFirstname() + SPACE + getPersonalInfo().getPersonalLastname();
  }

}
