package ru.edu.service;

import java.util.Map;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.graph.GraphSemantic;
import ru.edu.dao.UserRepository;
import ru.edu.dto.UserCreateDto;
import ru.edu.dto.UserReadDto;
import ru.edu.entity.User;
import ru.edu.mapper.Mapper;
import ru.edu.mapper.UserCreateMapper;
import ru.edu.mapper.UserReadMapper;

@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  private final UserReadMapper userReadMapper;

  private final UserCreateMapper userCreateMapper;

  @Transactional
  public Long create(UserCreateDto userDto) {
    // validation

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
