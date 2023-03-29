package ru.edu.entity;

import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
// добавим статический метод инициализации с названием LocaleInfo.of
@AllArgsConstructor(staticName = "of")
@Embeddable
public class LocaleInfo {

  private String language;
  private String description;

}
