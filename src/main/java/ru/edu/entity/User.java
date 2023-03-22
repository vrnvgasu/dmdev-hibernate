package ru.edu.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import ru.edu.converter.BirthdateConverter;

// POJO: @Data, @NoArgsConstructor, @AllArgsConstructor
// генерит пустой конструктор, геттеры, сеттеры,
// equals и хешкод, toString
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "company") // не делать select при отображении связи
@Entity // сущность хибернейта
@Table(name = "users", schema = "public")
@TypeDef(name = "outTypeName", typeClass = JsonBinaryType.class)
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Embedded
  // указали явный маппинг поля personalLastname к колонке lastname
  @AttributeOverride(name = "personalLastname", column = @Column(name = "lastname"))
  private PersonalInfo personalInfo;

  @Column(unique = true)
  private String username;

  @Enumerated(EnumType.STRING) // приводим ENUM к строке
  private Role role;

  //  @Type(type = "com.vladmihalcea.hibernate.type.json.JsonBinaryType")
//  @Type(type = "jsonb") // работает, т.к. в JsonBinaryType есть метод public String getName() {return "jsonb";}
  @Type(type = "outTypeName") // переопределили лаконичное название JsonBinaryType выше в @TypeDef
  private String info;

  // optional = true - default. Делает left join
  // optional = false. join - работает быстрее. Но company_id должен быть not null
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "company_id")
  private Company company;

}
