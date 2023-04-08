package ru.edu.dao;

import static ru.edu.entity.QCompany.company;
import static ru.edu.entity.QPayment.payment;
import static ru.edu.entity.QUser.user;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import org.hibernate.Session;
import ru.edu.dto.CompanyDto;
import ru.edu.dto.PaymentFilter;
import ru.edu.entity.Company;
import ru.edu.entity.Company_;
import ru.edu.entity.Payment;
import ru.edu.entity.Payment_;
import ru.edu.entity.PersonalInfo_;
import ru.edu.entity.Profile_;
import ru.edu.entity.QCompany;
import ru.edu.entity.QPayment;
import ru.edu.entity.QPersonalInfo;
import ru.edu.entity.QUser;
import ru.edu.entity.User;
import ru.edu.entity.User_;

public class UserDao {

  private static final UserDao INSTANCE = new UserDao();

  /**
   * Возвращает всех сотрудников
   */
  public List<User> findAll(Session session) {
//    return session.createQuery("select u from User u", User.class)
//      .list();

//    CriteriaBuilder cb = session.getCriteriaBuilder();
//    // что возвращаем из запроса. Аналог второго параметра в session.createQuery
//    CriteriaQuery<User> criteria = cb.createQuery(User.class);
//    // from User u
//    Root<User> user = criteria.from(User.class);
//    // select u
//    criteria.select(user);
//
//    return session.createQuery(criteria)
//      .list();

    // в конструктор надо передать EntityManager. Session как раз его реализует
    // User - что в итоге получим из запроса
    return new JPAQuery<User>(session)
      // QUser.user - сгенерирован apt-maven-plugin
      .select(user) // select u
      .from(user) // from User u
      .fetch(); // list()
  }

  /**
   * Возвращает всех сотрудников с указанным именем
   */
  public List<User> findAllByFirstName(Session session, String firstName) {
//    return session.createQuery("select u from User u " +
//        "where u.personalInfo.firstname = :firstName", User.class)
//      .setParameter("firstName", firstName)
//      .list();

//    CriteriaBuilder cb = session.getCriteriaBuilder();
//    // что возвращаем из запроса. Аналог второго параметра в session.createQuery
//    CriteriaQuery<User> criteria = cb.createQuery(User.class);
//    // from User u
//    var user = criteria.from(User.class);
//    // select u
//    criteria.select(user)
//      // where
//      .where(
//        //  u.personalInfo.firstname = :firstName
//        cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.firstname), firstName));
//    // User_.personalInfo - генерит библиотека hibernate-jpamodelgen
//
//    return session.createQuery(criteria).list();

    return new JPAQuery<User>(session)
      .select(user)
      .from(user)
      .where(user.personalInfo.firstname.eq(firstName))
      .fetch();
  }

  /**
   * Возвращает первые {limit} сотрудников, упорядоченных по дате рождения (в порядке возрастания)
   */
  public List<User> findLimitedUsersOrderedByBirthday(Session session, int limit) {
//    return session.createQuery("select u from User u order by u.personalInfo.birthDate", User.class)
//      .setMaxResults(limit)
//      .list();

//    var cb = session.getCriteriaBuilder();
//    var criteria = cb.createQuery(User.class);
//    var user = criteria.from(User.class);
//    criteria.select(user)
//      // order by
//      .orderBy(
//        // by u.personalInfo.birthDate
//        cb.asc(user.get(User_.personalInfo).get(PersonalInfo_.birthDate))
//      );
//
//    return session.createQuery(criteria)
//      .setMaxResults(limit) // limit как и при HQL
//      .list();

    return new JPAQuery<User>(session)
      .select(user)
      .from(user)
      .orderBy(user.personalInfo.birthDate.asc())
      .limit(limit)
      .fetch();
  }

  /**
   * Возвращает всех сотрудников компании с указанным названием
   */
  public List<User> findAllByCompanyName(Session session, String companyName) {
//    return session.createQuery("select u from Company c " +
//        "join c.users u " +
//        "where c.name = :companyName", User.class)
//      .setParameter("companyName", companyName)
//      .list();

//    var cb = session.getCriteriaBuilder();
//    var criteria = cb.createQuery(User.class);
//    //from Company c
//    var company = criteria.from(Company.class);
//    //join c.users u
//    var users = company.join(Company_.users);
//    // select u
//    criteria.select(users)
//      //where
//      .where(
//        //c.name = :companyName
//        cb.equal(company.get(Company_.name), companyName)
//      );
//
//    return session.createQuery(criteria).list();

    return new JPAQuery<User>(session)
      .select(user)
      .from(company)
      .join(company.users, user)
      .where(company.name.eq(companyName))
      .fetch();
  }

  /**
   * Возвращает все выплаты, полученные сотрудниками компании с указанными именем,
   * упорядоченные по имени сотрудника, а затем по размеру выплаты
   */
  public List<Payment> findAllPaymentsByCompanyName(Session session, String companyName) {
//    return session.createQuery("select p from Payment p " +
//        "join p.receiver u " +
//        "join u.company c " +
//        "where c.name = :companyName " +
//        "order by u.personalInfo.firstname, p.amount", Payment.class)
//      .setParameter("companyName", companyName)
//      .list();

//    var cb = session.getCriteriaBuilder();
//    var criteria = cb.createQuery(Payment.class);
//
//    var payment = criteria.from(Payment.class);
//    var user = payment.join(Payment_.receiver);
//    payment.fetch(Payment_.receiver); // добавит users в select после вызова join
//    var company = user.join(User_.company);
//
//    criteria.select(payment)
//      .where(cb.equal(company.get(Company_.name), companyName))
//      .orderBy(
//        cb.asc(user.get(User_.personalInfo).get(PersonalInfo_.firstname)),
//        cb.asc(payment.get(Payment_.amount))
//      );
//
//    return session.createQuery(criteria).list();

    return new JPAQuery<Payment>(session)
      .select(payment)
      .from(payment)
      // добавит users в select после вызова join
      .join(payment.receiver, user).fetchJoin()
      .join(payment.receiver.company, company)
      .where(company.name.eq(companyName))
      .orderBy(user.personalInfo.firstname.asc(), payment.amount.asc())
      .fetch();
  }

  /**
   * Возвращает среднюю зарплату сотрудника с указанными именем и фамилией
   */
  public Double findAveragePaymentAmountByFirstAndLastNames(Session session, PaymentFilter filter) {
//    return session.createQuery("select avg(p.amount) from Payment p " +
//        "join p.receiver u " +
//        "where u.personalInfo.firstname = :firstName " +
//        "   and u.personalInfo.personalLastname = :lastName", Double.class)
//      .setParameter("firstName", firstName)
//      .setParameter("lastName", lastName)
//      .uniqueResult();

//    var cb = session.getCriteriaBuilder();
//    var criteria = cb.createQuery(Double.class);
//    var payment = criteria.from(Payment.class);
//    var receiver = payment.join(Payment_.receiver);
//
//    List<Predicate> predicates = new ArrayList<>();
//    if (firstName != null) {
//      predicates.add(cb.equal(receiver.get(User_.personalInfo).get(PersonalInfo_.firstname), firstName));
//    }
//    if (lastName != null) {
//      predicates.add(cb.equal(receiver.get(User_.personalInfo).get(PersonalInfo_.personalLastname), lastName));
//    }
//
//    //select avg(p.amount)
//    criteria.select(cb.avg(payment.get(Payment_.amount)))
//      // могли условия cb.equal перечислять через запятую
//      .where(predicates.toArray(Predicate[]::new));
//
//    return session.createQuery(criteria).uniqueResult();

//    // динамический запрос с querydsl
//    List<com.querydsl.core.types.Predicate> predicates = new ArrayList<>();
//    if (filter.getFirstName() != null) {
//      predicates.add(user.personalInfo.firstname.eq(filter.getFirstName()));
//    }
//    if (filter.getLastName() != null) {
//      predicates.add(user.personalInfo.personalLastname.eq(filter.getLastName()));
//    }

    Predicate predicate = QPredicate.builder()
      .add(filter.getFirstName(), user.personalInfo.firstname::eq)
      .add(filter.getLastName(), user.personalInfo.personalLastname::eq)
      .buildAnd();

    return new JPAQuery<Double>(session)
      .select(payment.amount.avg())
      .from(payment)
      .join(payment.receiver, user)
      .where(predicate)
      .fetchOne();
  }

  /**
   * Возвращает для каждой компании: название, среднюю зарплату всех её сотрудников. Компании упорядочены по названию.
   */
  public List<com.querydsl.core.Tuple> findCompanyNamesWithAvgUserPaymentsOrderedByCompanyName(Session session) {
//    return session.createQuery("select c.name, avg(p.amount) from Company c " +
//        "join c.users u " +
//        "join u.payments p " +
//        "group by c.name " +
//        "order by c.name", Object[].class)
//      .list();

    //  вариант с multiselect. Под капотом вернет Object[]
//    var cb = session.getCriteriaBuilder();
//    var criteria = cb.createQuery(Object[].class);
//    var company = criteria.from(Company.class);
//    // можно вторым параметром выбирать тип join
//    var user = company.join(Company_.users, JoinType.INNER);
//    var payment = user.join(User_.payments);

//    criteria.multiselect(
//      company.get(Company_.name),
//      cb.avg(payment.get(Payment_.amount))
//    )
//      .groupBy(company.get(Company_.name))
//      .orderBy(cb.asc(company.get(Company_.name)));

    // вариант с DTO
//    var cb = session.getCriteriaBuilder();
//    var criteria = cb.createQuery(CompanyDto.class);
//    var company = criteria.from(Company.class);
//    // можно вторым параметром выбирать тип join
//    var user = company.join(Company_.users, JoinType.INNER);
//    var payment = user.join(User_.payments);
//    criteria.select(
//        cb.construct(
//          CompanyDto.class, // dto
//          // переменные в dto
//          company.get(Company_.name),
//          cb.avg(payment.get(Payment_.amount))
//        ))
//      .groupBy(company.get(Company_.name))
//      .orderBy(cb.asc(company.get(Company_.name)));
//
//    return session.createQuery(criteria).list();

    // К сожалению JPAQuery не умеет работать с DTO
    return new JPAQuery<com.querydsl.core.Tuple>(session)
      .select(company.name, payment.amount.avg())
      .from(company)
      .join(company.users, user)
      .join(user.payments, payment)
      .groupBy(company.name)
      .orderBy(company.name.asc())
      .fetch();
  }

  /**
   * Возвращает список: сотрудник (объект User), средний размер выплат, но только для тех сотрудников, чей средний размер выплат
   * больше среднего размера выплат всех сотрудников
   * Упорядочить по имени сотрудника
   */
  public List<Tuple> isItPossible(Session session) {
//    return session.createQuery("select u, avg(p.amount) from User u " +
//        "join u.payments p " +
//        "group by u " +
//        "having avg(p.amount) > (select avg(p.amount) from Payment p) " +
//        "order by u.personalInfo.firstname", Object[].class)
//      .list();

    // через Tuple
//    var cb = session.getCriteriaBuilder();
//    // вернем Tuple вместо Object[]
//    var criteria = cb.createQuery(Tuple.class);
//    var user = criteria.from(User.class);
//    var payment = user.join(User_.payments);
//
//    //подзапрос
//    var subquery = criteria.subquery(Double.class);
//    var paymentSubquery = subquery.from(Payment.class);
//
//    criteria.select(
//        cb.tuple(
//          user,
//          cb.avg(payment.get(Payment_.amount))
//        )
//      )
//      .groupBy(user.get(User_.id))
//      .having(cb.gt(
//          cb.avg(payment.get(Payment_.amount)),
//        subquery.select(cb.avg(paymentSubquery.get(Payment_.amount)))
//        )
//      )
//      .orderBy(cb.asc(user.get(User_.personalInfo).get(PersonalInfo_.firstname)));
//
//    return session.createQuery(criteria).list();

    return new JPAQuery<Tuple>(session)
      .select(user, payment.amount.avg())
      .from(user)
      .join(user.payments, payment)
      .groupBy(user.id)
      .having(payment.amount.avg().gt(
        new JPAQuery<Double>(session)
          .select(payment.amount.avg())
          .from(payment))
      )
      .orderBy(user.personalInfo.firstname.asc())
      .fetch();
  }

  public static UserDao getInstance() {
    return INSTANCE;
  }

}
