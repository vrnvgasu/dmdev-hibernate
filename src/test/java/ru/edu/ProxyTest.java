package ru.edu;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.junit.jupiter.api.Test;
import ru.edu.entity.Company;

public class ProxyTest {

  @Test
  void testDynamic() {
    // Простой пример proxy
    Company company = new Company();
    Proxy.newProxyInstance(company.getClass().getClassLoader(), company.getClass().getInterfaces(),
      // method - метод прокси объекта
      // args - набор аргументов, переданных в method
      (proxy, method, args) -> {
        // много вариантов, что будет делать. Самый простой вызвать этот метод у объекта
        return method.invoke(company, args);
      }
    );
  }

}
