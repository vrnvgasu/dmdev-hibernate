package ru.edu;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Table;
import lombok.Cleanup;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import ru.edu.entity.Birthday;
import ru.edu.entity.Company;
import ru.edu.entity.PersonalInfo;
import ru.edu.entity.User;
import ru.edu.util.HibernateUtil;

public class HibernateRunnerTest {

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

  @Test
  // Пример, как работает Hibernate через рефлексию
  void checkReflectionAPI() {
    User user = User.builder()
      .username("test6@test.ru")
      .personalInfo(PersonalInfo.builder()
        .firstname("ivan")
        .personalLastname("ivanov")
        .birthDate(new Birthday(LocalDate.of(2000, 1, 1)))
        .build())
      .build();

    String sql = """
      insert
          into
          %s
          (%s)
          values
          (%s)
      """;
    // через рефлексию из аннотации Table получаем название вида public.users (если указывали)
    // или User (если не указывали)
    String tableName = Optional.ofNullable(user.getClass().getAnnotation(Table.class))
      .map(tableAnnotation -> tableAnnotation.schema() + "." + tableAnnotation.name())
      .orElse(user.getClass().getName());

    Field[] declaredFields = user.getClass().getDeclaredFields();

    // берем все поля объекта через рефлексию.
    // у каждого поля берем содержимое аннотации Column или просто название поля.
    // джойним все названия в стрингу через запятую
    String columnNames = Arrays.stream(declaredFields)
      .map(field -> Optional.ofNullable(field.getAnnotation(Column.class))
        .map(Column::name)
        .orElse(field.getName()))
      .collect(Collectors.joining(", "));

    // стога со знаками вопросов по кол-ву полей
    String columnValues = Arrays.stream(declaredFields)
      .map(field -> "?")
      .collect(Collectors.joining(", "));

    sql = sql.formatted(tableName, columnNames, columnValues);
    /* получили такой замечательный запрос
    insert
      into
    public.users
      (username, firstname, lastname, birth_date, age)
    values
      (?, ?, ?, ?, ?)*/
    System.out.println(sql);

    /////////// выполняем запрос ///////////

    try (Connection connection = DriverManager
      .getConnection("jdbc:postgresql://localhost:5438/db_dmdev_hibernate", "user", "qwerty")) {

      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      int i = 1;
      for (Field declaredField : declaredFields) {
        declaredField.setAccessible(true); // чтобы получить доступ к значению
        preparedStatement.setObject(i, declaredField.get(user));
        i++;
      }

      preparedStatement.execute();

    } catch (Throwable e) {
      System.out.println(e.getMessage());
    }
  }

}
