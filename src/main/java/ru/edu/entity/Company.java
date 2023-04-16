package ru.edu.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
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
import javax.persistence.MapKey;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.SortNatural;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "users")
@EqualsAndHashCode(exclude = "users")
@Entity
//@BatchSize(size = 3) // подтянет связь на Company при вызове из дочерних связей
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Companies")
public class Company {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  @Builder.Default
  @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
  @MapKey(name = "username")
  @SortNatural
  @NotAudited
  private Map<String, User> users = new TreeMap<>();

//  @Builder.Default
//  @ElementCollection // подключает embeddable класс в коллекцию
//  // указываем таблицу и внешний ключ в ней
//  @CollectionTable(name = "company_locale", joinColumns = @JoinColumn(name = "company_id"))
//  @AttributeOverride(name = "language", column = @Column(name = "lang"))
//  private List<LocaleInfo> locales = new ArrayList<>();

//  @Builder.Default
//  @ElementCollection
//  @CollectionTable(name = "company_locale", joinColumns = @JoinColumn(name = "company_id"))
//  @Column(name = "description")
//  @MapKeyColumn(name = "lang")
//  // используем одно поле из таблицы справочника (только на чтение)
//  private Map<String, String> localeDescriptions = new HashMap<>();

  public void addUser(User user) {
    users.put(user.getUsername(), user);
    user.setCompany(this);
  }

}
