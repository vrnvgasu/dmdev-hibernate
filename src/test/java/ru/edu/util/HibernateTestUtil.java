package ru.edu.util;

import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;

@UtilityClass
public class HibernateTestUtil {

  // postgres:13 - docker Image
  private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13");

  // когда обратимся к HibernateTestUtil - проинициализируется статические поля и вызовется static блок
  // надо, чтобы на машине стоял докер
  // при запуске будет генерировать динамический url, username и password
  static {
    postgres.start();
  }

  public static SessionFactory buildSessionFactory() {
    Configuration configuration = HibernateUtil.buildConfiguration();

    configuration.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
    configuration.setProperty("hibernate.connection.username", postgres.getUsername());
    configuration.setProperty("hibernate.connection.password", postgres.getPassword());

    // подтягивает данные из src/test/resources/hibernate.cfg.xml
    // в которых не указывали url, username и password
    configuration.configure();


    return configuration.buildSessionFactory();
  }

}
