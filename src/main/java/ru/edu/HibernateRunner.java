package ru.edu;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateRunner {

  public static void main(String[] args) {
    Configuration configuration = new Configuration();

    // по умолчанию ищет конфиг в resources/hibernate.cfg.xml
    // если конфиг в другом месте, то указываем явно configuration.configure("path/to");
    configuration.configure();

    // configuration - имеет внутри себя много настроек + добавляет настройки из hibernate.cfg.xml
    // на основе суммарных настроек создаем SessionFactory - аналог пула коннекшенов
    // SessionFactory должен быть 1 на все приложение. Его надо закрыть при завершении
    try (SessionFactory sessionFactory = configuration.buildSessionFactory();
        // Session - аналог коннекта (подключения к бд)
        Session session = sessionFactory.openSession()) {
      System.out.println("OK");
    }
  }

}
