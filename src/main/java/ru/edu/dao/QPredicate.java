package ru.edu.dao;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QPredicate {

  private final List<Predicate> predicates = new ArrayList<>();

  public static QPredicate builder() {
    return new QPredicate();
  }

  public <T> QPredicate add(T object, Function<T, Predicate> function) {
    if(object != null) {
      predicates.add(function.apply(object));
    }

    return this;
  }

  public Predicate buildAnd() {
    // соединяет список predicates в один объект Predicate
    return ExpressionUtils.allOf(predicates);
  }

  public Predicate buildOf() {
    return ExpressionUtils.anyOf(predicates);
  }

}
