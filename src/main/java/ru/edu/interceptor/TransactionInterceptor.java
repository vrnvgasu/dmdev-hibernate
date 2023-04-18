package ru.edu.interceptor;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

@RequiredArgsConstructor
public class TransactionInterceptor {

  private final SessionFactory sessionFactory;

  /**
   * Своя реализация для @Transactional
   */

  @RuntimeType // не знаем, какой тип будет возвращен
  public Object intercept(
    @SuperCall Callable<Object> call, // вызываем не прокси, а реальный метод из сервиса
    @Origin Method method // метод, который вызываем у сервиса
  ) throws Exception {
    Transaction transaction = null;
    boolean transactionStarted = false;

    // если над метод есть аннотация @Transactional,
    // то будем открывать и закрывать транзакции автоматом
    if (method.isAnnotationPresent(Transactional.class)) {
      transaction = sessionFactory.getCurrentSession().getTransaction();

      if (!transaction.isActive()) {
        transaction.begin();
        transactionStarted = true;
      }
    }

    // вызываем сам метод
    Object result;
    try {
      result = call.call();
      if (transactionStarted) {
        transaction.commit();
      }
    } catch (Exception exception) {
      if (transactionStarted) {
        transaction.rollback();
      }
      throw exception;
    }

    return result;
  }

}
