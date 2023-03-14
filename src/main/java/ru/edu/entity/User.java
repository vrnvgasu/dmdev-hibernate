package ru.edu.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// POJO: @Data, @NoArgsConstructor, @AllArgsConstructor
// генерит пустой конструктор, геттеры, сеттеры,
// equals и хешкод, toString
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity // сущность хибернейта
@Table(name = "users", schema = "public")
public class User {

  // главное требование к id - реализация Serializable
  // String implements Serializable
  @Id
  private String username;
  private String firstname;
  private String lastname;

  @Column(name = "birth_date")
  private LocalDate birthDate;
  private Integer age;

}
