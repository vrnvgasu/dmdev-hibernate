package ru.edu;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import lombok.Cleanup;
import org.hibernate.FlushMode;
import org.hibernate.Hibernate;
import org.hibernate.annotations.QueryHints;
import org.hibernate.query.Query;
import org.junit.jupiter.api.Test;
import ru.edu.entity.Company;
import ru.edu.entity.Language;
import ru.edu.entity.Manager;
import ru.edu.entity.Profile;
import ru.edu.entity.Programmer;
import ru.edu.entity.User;
import ru.edu.util.HibernateTestUtil;
import ru.edu.util.HibernateUtil;

public class HibernateRunnerTest {

  @Test
  void checkHql() {
    try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
      var session = sessionFactory.openSession()) {
      session.beginTransaction();

      // не рекомендуется через ?
//      Query<User> query = session.createQuery("select u from User u "
//        + "where u.personalInfo.firstname = ?1", User.class)
//        .setParameter(1, "Ivan");
      // рекомендуется
      Query<User> query = session.createQuery("select u from User u "
          + "join u.company c "
          + "where u.personalInfo.firstname = :firstName and c.name = :companyName", User.class)
        .setParameter("firstName", "Ivan")
        .setParameter("companyName", "Google");

      // неявный join
      Query<User> query1 = session.createQuery("select u from User u "
          + "where u.personalInfo.firstname = :firstName and u.company.name = :companyName", User.class)
        .setParameter("firstName", "Ivan")
        .setParameter("companyName", "Google");

      Query<User> namedQuery = session.createNamedQuery("findUserByName", User.class)
        .setParameter("firstName", "Ivan")
        .setParameter("companyName", "Google")
        .setFlushMode(FlushMode.AUTO)
        .setHint(QueryHints.FETCH_SIZE, "50")
        .setHint(QueryHints.FLUSH_MODE, "auto");

      int countUpdatedRows = session.createQuery("update User u "
          + "set u.role = 'ADMIN'").executeUpdate();

      Query<User> nativeQuery = session.createNativeQuery("select u.* from users u where u.firstname = 'IVAN'", User.class);

      List<User> users = namedQuery.getResultList();

      session.getTransaction().commit();
    }
  }

//  @Test
//  void checkH2() {
//    try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
//      var session = sessionFactory.openSession()) {
//      session.beginTransaction();
//
//      // Сразу получаем ошибоку
//      //Cannot use identity column key generation with <union-subclass> mapping for: ru.edu.entity.User
//
//      var google = Company.builder()
//        .name("google")
//        .build();
//      session.save(google);
//
//      var programmer = Programmer.builder()
//        .username("programmer@programmer.com")
//        .language(Language.JAVA)
//        .company(google)
//        .build();
//      session.save(programmer);
//      var manager = Manager.builder()
//        .username("manager@manager.com")
//        .projectName("new")
//        .company(google)
//        .build();
//      session.save(manager);
//
//      session.flush();
//      session.clear();
//
//      // Получается, что у нас 2 таблицы. В первую сохранили с id=1
//      // во вторую с id=2
//      var programmer1 = session.get(Programmer.class, 1L);
//      // тут вообще прикол. User - общая сущность для программистов и менеджеров
//      // hibernate не знает, где конкретно запись с id=2 и делает select unit на 2 таблицы
//      var manager1 = session.get(User.class, 2L);
//
//      session.getTransaction().commit();
//    }
//  }

  @Test
  void localeInfo() {
    try (var sessionFactory = HibernateUtil.buildSessionFactory();
      var session = sessionFactory.openSession()) {
      session.beginTransaction();

      var company = session.get(Company.class, 1);
//      company.getUsers().forEach((k, v) -> System.out.println(v));
//      company.getLocaleDescriptions().forEach((k, v) -> System.out.println(v));

      session.getTransaction().commit();
    }
  }

  @Test
  void checkManyToMany() {
    try (var sessionFactory = HibernateTestUtil.buildSessionFactory();
      var session = sessionFactory.openSession()) {
      session.beginTransaction();

//      var user = User.builder()
//        .username("user1")
//        .build();
//      var chat = Chat.builder()
//        .name("chat1")
//        .build();
//      session.save(user);
//      session.save(chat);
//
//      UserChat userChat = UserChat.builder()
//        .createdAt(Instant.now())
//        .createdBy(user.getUsername())
//        .build();
//      userChat.setUser(user);
//      userChat.setChat(chat);

//      session.save(userChat);
//
//      System.out.println(session.get(UserChat.class, userChat.getId()));

      session.getTransaction().commit();
    }
  }

  @Test
  void checkOneToOne() {
    try (var sessionFactory = HibernateUtil.buildSessionFactory();
      var session = sessionFactory.openSession()) {

      session.beginTransaction();

//      var user = session.get(User.class, 8L);
      var profile = session.get(Profile.class, 1L);
      System.out.println();

//      var user = User.builder()
//        .username("checkOneToOne2@gmail.com")
//        .build();
//      var profile = Profile.builder()
//        .language("ru2")
//        .street("some street2")
//        .build();
//      // можем сразу задавать user в profile, т.к. это не id в profile, а отдельное поле
//      profile.setUser(user);
//
//      session.save(user);
//      // можем не сохранять profile явно, если поставить у User CascadeType.ALL

      session.getTransaction().commit();
    }
  }

  @Test
  void checkOrhanRemoval() {
    try (var sessionFactory = HibernateUtil.buildSessionFactory();
      var session = sessionFactory.openSession()) {

      session.beginTransaction();

      Company company = session.getReference(Company.class, 1);
      // удалит пользователя при удалении из коллекции,
      // если есть  @OneToMany(mappedBy = "company", orphanRemoval = true)
//      company.getUsers().removeIf(user -> user.getId().equals(4L));

      session.getTransaction().commit();
    }
  }

  @Test
  void checkLazyInitialization() {
    Company company = null;
    try (var sessionFactory = HibernateUtil.buildSessionFactory();
      var session = sessionFactory.openSession()) {

      session.beginTransaction();

      // Получили объект из сессии
      // getReference - возвращает объект прокси
      // вызов прокси не сделает запрос в БД, пока прокси не проинициализиется
      company = session.getReference(Company.class, 1);

      session.getTransaction().commit();
    }

//    var users = company.getUsers();
    // словим LazyInitializationException
    // в обертке persistence коллекции есть сессия, которая сделает запрос к БД,
    // но сессия уже закрыта выше
//    System.out.println(users.size());
  }

  @Test
  void addUserToNewCompany() {
    @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
    @Cleanup var session = sessionFactory.openSession();

    session.beginTransaction();

    Company company = session.get(Company.class, 3);
    // можем просто подтянуть коллекцию отношение, но на практике странно выглядит
    Hibernate.initialize(company.getUsers());

    session.delete(company);

//    Company company = Company.builder()
//      .name("New Company")
//      .build();
//    User user = User.builder()
//      .username("sveta@gmail.com")
//      .build();
//    company.addUser(user);
//
//    // у компании cascade = CascadeType.ALL
//    // сохранит и пользователя
//    session.save(company);

    session.getTransaction().commit();
  }

  @Test
  void oneToMany() {
    @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
    @Cleanup var session = sessionFactory.openSession();

    session.beginTransaction();

    Company company = session.get(Company.class, 1);
    System.out.println(company);

    session.getTransaction().commit();
  }

  @Test
    // Тест не рабочий. Это совсем упрощенный пример, как работает hibernate
    // session.get(User.class, "test5@test.ru")
  void checkGetReflectionAPI()
    throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
    PreparedStatement statement = null;
    ResultSet resultSet = statement.executeQuery();

    Class<User> userClass = User.class;

    // создали конструктор и из него новый объект
    Constructor<User> constructor = userClass.getConstructor();
    User user = constructor.newInstance();
    // заполнили одно из полей данными из запроса к БД
    Field userNameField = userClass.getDeclaredField("username");
    userNameField.setAccessible(true);
    userNameField.set(user, resultSet.getString("username"));
  }

//  @Test
//    // Пример, как работает Hibernate через рефлексию
//  void checkReflectionAPI() {
//    User user = User.builder()
//      .username("test6@test.ru")
//      .personalInfo(PersonalInfo.builder()
//        .firstname("ivan")
//        .personalLastname("ivanov")
//        .birthDate(new Birthday(LocalDate.of(2000, 1, 1)))
//        .build())
//      .build();
//
//    String sql = """
//      insert
//          into
//          %s
//          (%s)
//          values
//          (%s)
//      """;
//    // через рефлексию из аннотации Table получаем название вида public.users (если указывали)
//    // или User (если не указывали)
//    String tableName = Optional.ofNullable(user.getClass().getAnnotation(Table.class))
//      .map(tableAnnotation -> tableAnnotation.schema() + "." + tableAnnotation.name())
//      .orElse(user.getClass().getName());
//
//    Field[] declaredFields = user.getClass().getDeclaredFields();
//
//    // берем все поля объекта через рефлексию.
//    // у каждого поля берем содержимое аннотации Column или просто название поля.
//    // джойним все названия в стрингу через запятую
//    String columnNames = Arrays.stream(declaredFields)
//      .map(field -> Optional.ofNullable(field.getAnnotation(Column.class))
//        .map(Column::name)
//        .orElse(field.getName()))
//      .collect(Collectors.joining(", "));
//
//    // стога со знаками вопросов по кол-ву полей
//    String columnValues = Arrays.stream(declaredFields)
//      .map(field -> "?")
//      .collect(Collectors.joining(", "));
//
//    sql = sql.formatted(tableName, columnNames, columnValues);
//    /* получили такой замечательный запрос
//    insert
//      into
//    public.users
//      (username, firstname, lastname, birth_date, age)
//    values
//      (?, ?, ?, ?, ?)*/
//    System.out.println(sql);
//
//    /////////// выполняем запрос ///////////
//
//    try (Connection connection = DriverManager
//      .getConnection("jdbc:postgresql://localhost:5438/db_dmdev_hibernate", "user", "qwerty")) {
//
//      PreparedStatement preparedStatement = connection.prepareStatement(sql);
//      int i = 1;
//      for (Field declaredField : declaredFields) {
//        declaredField.setAccessible(true); // чтобы получить доступ к значению
//        preparedStatement.setObject(i, declaredField.get(user));
//        i++;
//      }
//
//      preparedStatement.execute();
//
//    } catch (Throwable e) {
//      System.out.println(e.getMessage());
//    }
//  }

}
