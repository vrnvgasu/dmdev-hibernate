package ru.edu.interceptor;

import java.io.Serializable;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

public class GlobalInterceptor extends EmptyInterceptor {

  @Override
  // можем сравнить глобальное и предыдущие состояние
  public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
    String[] propertyNames, Type[] types) {
    return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
  }

}
