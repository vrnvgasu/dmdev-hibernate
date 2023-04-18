package ru.edu.mapper;

import ru.edu.dto.CompanyReadDto;
import ru.edu.entity.Company;

// делаем вручную, но есть удобные фреймворки. Хороший - MapStruct
public class CompanyReadMapper implements Mapper<Company, CompanyReadDto> {

  @Override
  public CompanyReadDto mapFrom(Company object) {
    return new CompanyReadDto(
      object.getId(),
      object.getName(),
      object.getLocaleDescriptions()
    );
  }

}
