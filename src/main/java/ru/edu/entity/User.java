package ru.edu.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Convert;
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
  private String firstname;
  private String lastname;

  // BirthdateConverter будет маппить наш объект Birthday на sql
  // вместо @Convert можно сразу задать в настройках Configuration хибернейта
//  @Convert(converter = BirthdateConverter.class)
  @Column(name = "birth_date")
  // добавили наш собственный тип ru.edu.entity.Birthday
  private Birthday birthDate;

  @Enumerated(EnumType.STRING) // приводим ENUM к строке
  private Role role;

//  @Type(type = "com.vladmihalcea.hibernate.type.json.JsonBinaryType")
//  @Type(type = "jsonb") // работает, т.к. в JsonBinaryType есть метод public String getName() {return "jsonb";}
  @Type(type = "outTypeName") // переопределили лаконичное название JsonBinaryType выше в @TypeDef
  private String info;

}