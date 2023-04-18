package ru.edu.mapper;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import ru.edu.dto.UserReadDto;
import ru.edu.entity.User;

// делаем вручную, но есть удобные фреймворки. Хороший - MapStruct
@RequiredArgsConstructor
public class UserReadMapper implements Mapper<User, UserReadDto> {

  private final CompanyReadMapper companyReadMapper;

  @Override
  public UserReadDto mapFrom(User object) {
    return new UserReadDto(
      object.getId(),
      object.getPersonalInfo(),
      object.getUsername(),
      object.getInfo(),
      object.getRole(),
      Optional.ofNullable(object.getCompany())
          .map(companyReadMapper::mapFrom)
            .orElse(null)
    );
  }

}
