package ru.edu.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable // встроенный компонент
public class PersonalInfo {

  private String firstname;

  // для этого поля сделаем маппинг с колонкой в Entity
  private String personalLastname;

  // BirthdateConverter будет маппить наш объект Birthday на sql
  // вместо @Convert можно сразу задать в настройках Configuration хибернейта
//  @Convert(converter = BirthdateConverter.class)
  @Column(name = "birth_date")
  // добавили наш собственный тип ru.edu.entity.Birthday
  private Birthday birthDate;

}
