package ru.edu;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.edu.entity.User;
import ru.edu.util.HibernateUtil;

@Slf4j// генерит строку private static final Logger log
public class HibernateRunner {

  // выбираем org.slf4j.Logger
  // HibernateRunner.class будет передаваться в %c в конфиг логгера
  // можно заменить аннотацией @Slf4j
//  private static final Logger log = LoggerFactory.getLogger(HibernateRunner.class);

  public static void main(String[] args) {
    try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
      Session session = sessionFactory.openSession()) {
      session.beginTransaction();

      session.enableFetchProfile("withCompanyAndPayment");
      var user = session.get(User.class, 1L);
      System.out.println(user.getPayments().size());
      System.out.println(user.getCompany().getName());

//      var users = session.createQuery("select u from User u " +
//        "join fetch u.payments " +
//        "join fetch u.company", User.class).list();
//      users.forEach(user -> System.out.println(user.getPayments()));

      session.getTransaction().commit();
    }
  }

}
