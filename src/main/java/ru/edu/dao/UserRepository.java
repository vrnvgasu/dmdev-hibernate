package ru.edu.dao;

import javax.persistence.EntityManager;
import ru.edu.entity.User;

public class UserRepository extends RepositoryBase<Long, User> {

  public UserRepository(EntityManager entityManager) {
    super(User.class, entityManager);
  }

}
