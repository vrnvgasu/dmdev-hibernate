package ru.edu.dao;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import ru.edu.entity.BaseEntity;

public interface Repository<K extends Serializable, E extends BaseEntity<K>> {

  E save(E entity);

  void delete(K id);

  void update(E entity);

  default Optional<E> findById(K id) {
    return findById(id, Collections.emptyMap());
  }

  // properties, чтобы делать запрос с графом
  Optional<E> findById(K id, Map<String, Object> properties);

  List<E> findAll();

}
