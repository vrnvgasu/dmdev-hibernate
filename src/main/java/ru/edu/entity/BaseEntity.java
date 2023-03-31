package ru.edu.entity;

import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

//@Getter
//@Setter
//@MappedSuperclass // наследники Entity видят поля
//// T - тип id (Long, Integer и тд). @Id должен быть Serializable
//public abstract class BaseEntity<T extends Serializable> {
//
//  @Id
//  // надо стараться делать в новых базах IDENTITY
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
//  private T id;
//
//}

// более гибкий вариант
public interface BaseEntity<T extends Serializable> {

  void setId(T id);

  T getId();

}
