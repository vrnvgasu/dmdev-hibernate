package ru.edu;

import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.edu.entity.Birthday;
import ru.edu.entity.Company;
import ru.edu.entity.PersonalInfo;
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

        User userFromBD = session1.get(User.class, 1L);
        // companyRelation - обертка над Company
        Company companyRelation = userFromBD.getCompany();
        // вытянули target из companyRelation
        Object realCompany = Hibernate.unproxy(companyRelation);

        session1.getTransaction().commit();

        // делает select на связь, при FetchType.LAZY
//        System.out.println(userFromBD.getCompany());
      }
    }
  }

}
