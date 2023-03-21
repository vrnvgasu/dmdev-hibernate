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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Entity // сущность хибернейта
@Table(name = "users", schema = "public")
@TypeDef(name = "outTypeName", typeClass = JsonBinaryType.class)
// тип доступа к полям. AccessType.FIELD - по умолчанию через рефлексию
// но раньше было не можно и использовали геттеры - AccessType.PROPERTY
// AccessType.PROPERTY - нужно писать геттеры и ставить все аннотации над ними типа @Column
// AccessType.PROPERTY, сейчас не подходит, т.к. мы явно геттеры в коде не пишем из-за ломбока
@Access(AccessType.PROPERTY)
public class User {

  @EmbeddedId
  // указали явный маппинг поля personalLastname к колонке lastname
  @AttributeOverride(name = "personalLastname", column = @Column(name = "lastname"))
  private PersonalInfo personalInfo;

  @Column(unique = true)
  // Transient не стоит использовать. В сущности надо хранить только поля из БД
//  @Transient // не хотим сохранять и извлекать из БД
  private String username;

//  // TemporalType.TIMESTAMP - по умолчанию. Маппит на timestamp в БД
//  // всего 3 типа: TIMESTAMP, DATE, TIME
//  @Temporal(TemporalType.TIMESTAMP)
//  private Date date;
//  // НО! Вместо этого лучше использовать новые типы:
//  private LocalDateTime localDateTime;
//  private LocalDate localDate;
//  private LocalTime localTime;

  @Enumerated(EnumType.STRING) // приводим ENUM к строке
  private Role role;

  //  @Type(type = "com.vladmihalcea.hibernate.type.json.JsonBinaryType")
//  @Type(type = "jsonb") // работает, т.к. в JsonBinaryType есть метод public String getName() {return "jsonb";}
  @Type(type = "outTypeName") // переопределили лаконичное название JsonBinaryType выше в @TypeDef
  private String info;

}
