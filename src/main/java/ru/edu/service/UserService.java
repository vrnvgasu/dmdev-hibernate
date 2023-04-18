package ru.edu.service;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import org.hibernate.graph.GraphSemantic;
import ru.edu.dao.UserRepository;
import ru.edu.dto.UserCreateDto;
import ru.edu.dto.UserReadDto;
import ru.edu.entity.User;
import ru.edu.mapper.Mapper;
import ru.edu.mapper.UserCreateMapper;
import ru.edu.mapper.UserReadMapper;
import ru.edu.validation.UpdateCheck;

@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  private final UserReadMapper userReadMapper;

  private final UserCreateMapper userCreateMapper;

  @Transactional
  public Long create(UserCreateDto userDto) {
    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    // validator лучше делать бином на все приложение
    Validator validator = validatorFactory.getValidator();
    // получили множество всех ошибок
//    Set<ConstraintViolation<UserCreateDto>> validationResult = validator.validate(userDto);
    // проверять только аннотации с меткой UpdateCheck.class
    Set<ConstraintViolation<UserCreateDto>> validationResult = validator.validate(userDto, UpdateCheck.class);
    if (!validationResult.isEmpty()) {
      throw new ConstraintViolationException(validationResult);
    }

    User user = userCreateMapper.mapFrom(userDto);
    return userRepository.save(user).getId();
  }

  @Transactional
  public Optional<UserReadDto> findById(Long id) {
    return findById(id, userReadMapper);
  }

  @Transactional
  public <T> Optional<T> findById(Long id, Mapper<User, T> mapper) {
    Map<String, Object> properties = Map.of(
      GraphSemantic.LOAD.getJpaHintName(),
      userRepository.getEntityManager().getEntityGraph("WithCompany")
    );
    return userRepository.findById(id, properties)
      .map(mapper::mapFrom);
  }

  @Transactional
  public boolean delete(Long id) {
    Optional<User> maybeUser = userRepository.findById(id);
    maybeUser.ifPresent(user -> userRepository.delete(user.getId()));
    return maybeUser.isPresent();
  }

}
