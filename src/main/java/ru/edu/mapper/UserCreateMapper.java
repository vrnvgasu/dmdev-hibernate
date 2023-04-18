package ru.edu.mapper;

import lombok.RequiredArgsConstructor;
import ru.edu.dao.CompanyRepository;
import ru.edu.dto.UserCreateDto;
import ru.edu.entity.User;

@RequiredArgsConstructor
public class UserCreateMapper implements Mapper<UserCreateDto, User> {

  private final CompanyRepository companyRepository;

  @Override
  public User mapFrom(UserCreateDto object) {
    return User.builder()
      .personalInfo(object.personalInfo())
      .username(object.username())
      .info(object.info())
      .role(object.role())
      .company(companyRepository.findById(object.companyId()).orElseThrow(IllegalArgumentException::new))
      .build();
  }

}
