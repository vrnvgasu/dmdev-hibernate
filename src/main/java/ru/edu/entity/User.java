package ru.edu.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import java.time.LocalDate;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Entity // сущность хибернейта
@Table(name = "users", schema = "public")
@TypeDef(name = "outTypeName", typeClass = JsonBinaryType.class)
public class User {

  // главное требование к id - реализация Serializable
  // String implements Serializable
  @Id
  private String username;

  @Embedded // не обязательная аннотация (но так нагляднее)
  // указали явный маппинг поля personalLastname к колонке lastname
  @AttributeOverride(name = "personalLastname", column = @Column(name = "lastname"))
  private PersonalInfo personalInfo;

  @Enumerated(EnumType.STRING) // приводим ENUM к строке
  private Role role;

//  @Type(type = "com.vladmihalcea.hibernate.type.json.JsonBinaryType")
//  @Type(type = "jsonb") // работает, т.к. в JsonBinaryType есть метод public String getName() {return "jsonb";}
  @Type(type = "outTypeName") // переопределили лаконичное название JsonBinaryType выше в @TypeDef
  private String info;

}
