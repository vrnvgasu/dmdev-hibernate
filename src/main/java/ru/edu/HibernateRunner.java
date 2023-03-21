package ru.edu;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.edu.converter.BirthdateConverter;
import ru.edu.entity.Birthday;
import ru.edu.entity.Company;
import ru.edu.entity.PersonalInfo;
import ru.edu.entity.Role;
import ru.edu.entity.User;
import ru.edu.util.HibernateUtil;

@Slf4j// генерит строку private static final Logger log
public class HibernateRunner {

  // выбираем org.slf4j.Logger
  // HibernateRunner.class будет передаваться в %c в конфиг логгера
  // можно заменить аннотацией @Slf4j
//  private static final Logger log = LoggerFactory.getLogger(HibernateRunner.class);

  public static void main(String[] args) {
    Company company = Company.builder()
      .name("Google")
      .build();
    User user = User.builder()
      .username("petr3@gmail.ru")
      .personalInfo(PersonalInfo.builder()
        .firstname("petr")
        .personalLastname("petrov")
        .birthDate(new Birthday(LocalDate.of(2000, 1, 1)))
        .build())
      .company(company)
      .build();

    try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
      Session session1 = sessionFactory.openSession();
      try (session1) {
        session1.beginTransaction();

        session1.save(company);
        session1.save(user);

        session1.getTransaction().commit();
      }
    }
  }

  //  public static void main(String[] args) {
//    Configuration configuration = new Configuration();
//
//    // укажем Entity
//    // но предпочтительнее указывать mapping в конфиге hibernate.cfg.xml
////    configuration.addAnnotatedClass(User.class);
//
//    // в entity поле birthDate отличается от поля в БД birth_date
//    // стратегия по умолчанию в Configuration приводит birthDate к birthdate
//    // можем поменять стратегию приведения названий колонок birthDate к birth_date
//    // конечно лучше добавить @Column(name = "birth_date") в сущность. Но это пример,
//    // что все можно настроить и даже свои классы создать для приведения (правда зачем?)
////    configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
//
//    // по умолчанию ищет конфиг в resources/hibernate.cfg.xml
//    // если конфиг в другом месте, то указываем явно configuration.configure("path/to");
//    configuration.configure();
//
//    // BirthdateConverter будет маппить наш объект Birthday на sql
//    // вместо аннотации @Convert(converter = BirthdateConverter.class) у поля сущности
////    configuration.addAttributeConverter(new BirthdateConverter(), true);
//    // вместо второго параметра можно ставить @Converter(autoApply = true) в самом конвертере
//    configuration.addAttributeConverter(new BirthdateConverter());
//
//    // добавили тип из библиотеки hibernate-types-52
//    configuration.registerTypeOverride(new JsonBinaryType());
//
//    // configuration - имеет внутри себя много настроек + добавляет настройки из hibernate.cfg.xml
//    // на основе суммарных настроек создаем SessionFactory - аналог пула коннекшенов
//    // SessionFactory должен быть 1 на все приложение. Его надо закрыть при завершении
//    try (SessionFactory sessionFactory = configuration.buildSessionFactory();
//      // Session - аналог коннекта (подключения к бд)
//      Session session = sessionFactory.openSession()) {
//
//      // работа с сессией (коннектом) идет внутри транзакции
//      session.beginTransaction();
//
//      User user = User.builder()
//        .username("test10@test.ru")
//        .firstname("ivan")
//        .lastname("ivanov")
//        .birthDate(new Birthday(LocalDate.of(2000, 1, 1)))
//        .role(Role.ADMIN)
//        .info("""
//          {
//            "name": "Ivan",
//            "id": 26
//          }
//          """)
//        .build();
//
//      // передаем в сессию (коннект) сущность (@Entity)
//      // саму операцию выполняет отложено в конце транзакции
////      session.save(user);
//
//      // кинет exception, если нет такого пользователя
////      session.update(user);
//
//      // сохранит или создаст.
//      // Но вначале делает запрос select. Можно нагрузить так базу
//      // саму операцию выполняет отложено в конце транзакции
////      session.saveOrUpdate(user);
//
//      // сначала делает select.
//      // саму операцию выполняет отложено в конце транзакции выполняет delete, если есть
////      session.delete(user);
//
//      // получить сущность по ее идентификатору
//      // операцию выполняет сразу
//      User userFromDB1 = session.get(User.class, "test5@test.ru");
//      // второй раз тот же select не выполнится. Это first level cache
//      // PersistenceContext - это first level cache
//      User userFromDB2 = session.get(User.class, "test5@test.ru");
//
//      // удалит объект из PersistenceContext (из мапы сессии)
//      session.evict(userFromDB1);
//      // сделает селект, т.к. evict удалил объект из контекста
//      User userFromDB3 = session.get(User.class, "test5@test.ru");
//
//      // полностью чистит контекст
//      session.clear();
//      // сделает селект, т.к. clear полностью очистил контекст
//      User userFromDB4 = session.get(User.class, "test5@test.ru");
//
//      // если объект в контексте, то любое его изменение в java
//      // сразу же идет в БД (или подтягивает данные при запросе связи)
//      // это делает сессию грязной (dirty session)
//      userFromDB4.setLastname("Неожиданное меняет DB2:)");
//      // проверяем - грязная ли сессия (были ли неожиданные изменения в БД)
//      System.out.println(session.isDirty());
//
//      // принудительно отправляем все данные из контекста в БД
//      // синронизируемся с БД
//      session.flush();
//
//
//      session.getTransaction().commit();
//
//      // когда мы выходим из try with resource срабатывает
//      // session.close(); - соответственно контекст этой сессии очистится
//    }
//  }

}
