package ru.edu.converter;

import java.sql.Date;
import java.util.Optional;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import ru.edu.entity.Birthday;

// преобразовывает наш собственный тип Birthday в sql Date
@Converter(autoApply = true)
public class BirthdateConverter implements AttributeConverter<Birthday, Date> {

  @Override
  public Date convertToDatabaseColumn(Birthday attribute) {
    return Optional.ofNullable(attribute)
      .map(Birthday::birthday)// берем поле birthday из класса Birthday
      .map(Date::valueOf) // преобразуем это поле в объект java.sql.Date
      .orElse(null);
  }

  @Override
  public Birthday convertToEntityAttribute(Date dbData) {
    return Optional.ofNullable(dbData)
      .map(Date::toLocalDate)
      .map(Birthday::new)
      .orElse(null);
  }

}
