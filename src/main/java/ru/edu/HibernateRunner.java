package ru.edu;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import java.time.LocalDate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;
import ru.edu.converter.BirthdateConverter;
import ru.edu.entity.Birthday;
import ru.edu.entity.Role;
import ru.edu.entity.User;

public class HibernateRunner {

  public static void main(String[] args) {
    Configuration configuration = new Configuration();

    // укажем Entity
    // но предпочтительнее указывать mapping в конфиге hibernate.cfg.xml
//    configuration.addAnnotatedClass(User.class);

    // в entity поле birthDate отличается от поля в БД birth_date
    // стратегия по умолчанию в Configuration приводит birthDate к birthdate
    // можем поменять стратегию приведения названий колонок birthDate к birth_date
    // конечно лучше добавить @Column(name = "birth_date") в сущность. Но это пример,
    // что все можно настроить и даже свои классы создать для приведения (правда зачем?)
//    configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());

    // по умолчанию ищет конфиг в resources/hibernate.cfg.xml
    // если конфиг в другом месте, то указываем явно configuration.configure("path/to");
    configuration.configure();

    // BirthdateConverter будет маппить наш объект Birthday на sql
    // вместо аннотации @Convert(converter = BirthdateConverter.class) у поля сущности
//    configuration.addAttributeConverter(new BirthdateConverter(), true);
    // вместо второго параметра можно ставить @Converter(autoApply = true) в самом конвертере
    configuration.addAttributeConverter(new BirthdateConverter());

    // добавили тип из библиотеки hibernate-types-52
    configuration.registerTypeOverride(new JsonBinaryType());

    // configuration - имеет внутри себя много настроек + добавляет настройки из hibernate.cfg.xml
    // на основе суммарных настроек создаем SessionFactory - аналог пула коннекшенов
    // SessionFactory должен быть 1 на все приложение. Его надо закрыть при завершении
    try (SessionFactory sessionFactory = configuration.buildSessionFactory();
      // Session - аналог коннекта (подключения к бд)
      Session session = sessionFactory.openSession()) {

      // работа с сессией (коннектом) идет внутри транзакции
      session.beginTransaction();

      User user = User.builder()
        .username("test10@test.ru")
        .firstname("ivan")
        .lastname("ivanov")
        .birthDate(new Birthday(LocalDate.of(2000, 1, 1)))
        .role(Role.ADMIN)
        .info("""
          {
            "name": "Ivan",
            "id": 26
          }
          """)
        .build();

      // передаем в сессию (коннект) сущность (@Entity)
      // саму операцию выполняет отложено в конце транзакции
//      session.save(user);

      // кинет exception, если нет такого пользователя
//      session.update(user);

      // сохранит или создаст.
      // Но вначале делает запрос select. Можно нагрузить так базу
      // саму операцию выполняет отложено в конце транзакции
//      session.saveOrUpdate(user);

      // сначала делает select.
      // саму операцию выполняет отложено в конце транзакции выполняет delete, если есть
//      session.delete(user);

      // получить сущность по ее идентификатору
      // операцию выполняет сразу
      User userFromDB = session.get(User.class, "test5@test.ru");

      session.getTransaction().commit();
    }
  }

}
