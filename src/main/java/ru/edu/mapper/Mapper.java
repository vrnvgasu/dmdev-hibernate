package ru.edu.mapper;

// конвертирует F(from) в T(to)
public interface Mapper<F, T> {

  T mapFrom(F object);

}
