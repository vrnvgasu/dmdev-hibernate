package ru.edu.dao;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

import com.querydsl.core.Tuple;
import java.util.List;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import ru.edu.dto.CompanyDto;
import ru.edu.dto.PaymentFilter;
import ru.edu.entity.Payment;
import ru.edu.entity.User;
import ru.edu.util.HibernateTestUtil;
import ru.edu.util.TestDataImporter;

@TestInstance(PER_CLASS)
class UserDaoTest {

  private final SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();
  private final UserDao userDao = UserDao.getInstance();

  @BeforeAll
  public void initDb() {
    TestDataImporter.importData(sessionFactory);
  }

  @AfterAll
  public void finish() {
    sessionFactory.close();
  }

  @Test
  void findAll() {
    @Cleanup Session session = sessionFactory.openSession();
    session.beginTransaction();

    List<User> results = userDao.findAll(session);
    assertThat(results).hasSize(5);

    List<String> fullNames = results.stream().map(User::fullName).collect(toList());
    assertThat(fullNames).containsExactlyInAnyOrder("Bill Gates", "Steve Jobs", "Sergey Brin", "Tim Cook", "Diane Greene");

    session.getTransaction().commit();
  }

  @Test
  void findAllByFirstName() {
    @Cleanup Session session = sessionFactory.openSession();
    session.beginTransaction();

    List<User> results = userDao.findAllByFirstName(session, "Bill");

    assertThat(results).hasSize(1);
    assertThat(results.get(0).fullName()).isEqualTo("Bill Gates");

    session.getTransaction().commit();
  }

  @Test
  void findLimitedUsersOrderedByBirthday() {
    @Cleanup Session session = sessionFactory.openSession();
    session.beginTransaction();

    int limit = 3;
    List<User> results = userDao.findLimitedUsersOrderedByBirthday(session, limit);
    assertThat(results).hasSize(limit);

    List<String> fullNames = results.stream().map(User::fullName).collect(toList());
    assertThat(fullNames).contains("Diane Greene", "Steve Jobs", "Bill Gates");

    session.getTransaction().commit();
  }

  @Test
  void findAllByCompanyName() {
    @Cleanup Session session = sessionFactory.openSession();
    session.beginTransaction();

    List<User> results = userDao.findAllByCompanyName(session, "Google");
    assertThat(results).hasSize(2);

    List<String> fullNames = results.stream().map(User::fullName).collect(toList());
    assertThat(fullNames).containsExactlyInAnyOrder("Sergey Brin", "Diane Greene");

    session.getTransaction().commit();
  }

  @Test
  void findAllPaymentsByCompanyName() {
    @Cleanup Session session = sessionFactory.openSession();
    session.beginTransaction();

    List<Payment> applePayments = userDao.findAllPaymentsByCompanyName(session, "Apple");
    assertThat(applePayments).hasSize(5);

    List<Integer> amounts = applePayments.stream().map(Payment::getAmount).collect(toList());
    assertThat(amounts).contains(250, 500, 600, 300, 400);

    session.getTransaction().commit();
  }

  @Test
  void findAveragePaymentAmountByFirstAndLastNames() {
    @Cleanup Session session = sessionFactory.openSession();
    session.beginTransaction();

    PaymentFilter filter = PaymentFilter.builder()
      .firstName("Bill")
      .lastName("Gates")
      .build();
    Double averagePaymentAmount = userDao.findAveragePaymentAmountByFirstAndLastNames(session, filter);
    assertThat(averagePaymentAmount).isEqualTo(300.0);

    session.getTransaction().commit();
  }

  @Test
  void findCompanyNamesWithAvgUserPaymentsOrderedByCompanyName() {
    @Cleanup Session session = sessionFactory.openSession();
    session.beginTransaction();

//    List<Object[]> results = userDao.findCompanyNamesWithAvgUserPaymentsOrderedByCompanyName(session);
//    assertThat(results).hasSize(3);
//
//    List<String> orgNames = results.stream().map(a -> (String) a[0]).collect(toList());
//    assertThat(orgNames).contains("Apple", "Google", "Microsoft");
//
//    List<Double> orgAvgPayments = results.stream().map(a -> (Double) a[1]).collect(toList());
//    assertThat(orgAvgPayments).contains(410.0, 400.0, 300.0);


    List<com.querydsl.core.Tuple> results = userDao.findCompanyNamesWithAvgUserPaymentsOrderedByCompanyName(session);
    assertThat(results).hasSize(3);

    List<String> orgNames = results.stream().map(t -> t.get(0, String.class)).collect(toList());
    assertThat(orgNames).contains("Apple", "Google", "Microsoft");

    List<Double> orgAvgPayments = results.stream().map(t -> t.get(1, Double.class)).collect(toList());
    assertThat(orgAvgPayments).contains(410.0, 400.0, 300.0);

    session.getTransaction().commit();
  }

  @Test
  void isItPossible() {
    @Cleanup Session session = sessionFactory.openSession();
    session.beginTransaction();

    List<Tuple> results = userDao.isItPossible(session);
    assertThat(results).hasSize(2);

    List<String> names = results.stream().map(r -> r.get(0, User.class).fullName()).collect(toList());
    assertThat(names).contains("Sergey Brin", "Steve Jobs");

    List<Double> averagePayments = results.stream().map(r -> r.get(1, Double.class)).collect(toList());
    assertThat(averagePayments).contains(500.0, 450.0);

    session.getTransaction().commit();
  }
}
