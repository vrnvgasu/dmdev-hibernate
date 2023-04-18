package ru.edu.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.edu.entity.BaseEntity;

@Getter
@RequiredArgsConstructor
public abstract class RepositoryBase<K extends Serializable, E extends BaseEntity<K>> implements Repository<K, E> {

  private final Class<E> clazz;
  private final EntityManager entityManager;

  @Override
  public E save(E entity) {
    entityManager.persist(entity);
    return entity;
  }

  @Override
  public void delete(K id) {
    entityManager.remove(id);
    entityManager.flush();
  }

  @Override
  public void update(E entity) {
    entityManager.merge(entity);
  }

  @Override
  public Optional<E> findById(K id, Map<String, Object> properties) {
    return Optional.ofNullable(entityManager.find(clazz, id, properties));
  }

  @Override
  public List<E> findAll() {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<E> criteria = cb.createQuery(clazz);
    criteria.from(clazz);

    return entityManager.createQuery(criteria)
      .getResultList();
  }

}
