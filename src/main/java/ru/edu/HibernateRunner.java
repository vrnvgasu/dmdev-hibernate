package ru.edu;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.annotations.QueryHints;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.graph.RootGraph;
import org.hibernate.graph.SubGraph;
import org.hibernate.jdbc.Work;
import ru.edu.dao.CompanyRepository;
import ru.edu.dao.PaymentRepository;
import ru.edu.dao.UserRepository;
import ru.edu.dto.UserCreateDto;
import ru.edu.entity.Payment;
import ru.edu.entity.PersonalInfo;
import ru.edu.entity.Role;
import ru.edu.entity.User;
import ru.edu.entity.UserChat;
import ru.edu.interceptor.GlobalInterceptor;
import ru.edu.interceptor.TransactionInterceptor;
import ru.edu.mapper.CompanyReadMapper;
import ru.edu.mapper.UserCreateMapper;
import ru.edu.mapper.UserReadMapper;
import ru.edu.service.UserService;
import ru.edu.util.HibernateUtil;
import ru.edu.util.TestDataImporter;

@Slf4j// генерит строку private static final Logger log
public class HibernateRunner {

  public static void main(String[] args)
    throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
    try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
//      TestDataImporter.importData(sessionFactory);

      // делаем вместо обычной сессии прокси, чтобы работала многопоточность
      var session = (Session) Proxy.newProxyInstance(
        SessionFactory.class.getClassLoader(),
        new Class[]{Session.class},
        (proxy, method, args1) -> method.invoke(sessionFactory.getCurrentSession(), args1)
      );

      // транзакция явно не нужна при @Transactional
//      session.beginTransaction();

      UserRepository userRepository = new UserRepository(session);
      CompanyReadMapper companyReadMapper = new CompanyReadMapper();
      UserReadMapper userReadMapper = new UserReadMapper(companyReadMapper);
      CompanyRepository companyRepository = new CompanyRepository(session);
      UserCreateMapper userCreateMapper = new UserCreateMapper(companyRepository);
//      UserService userService = new UserService(userRepository, userReadMapper, userCreateMapper);
      TransactionInterceptor transactionInterceptor = new TransactionInterceptor(sessionFactory);

      // Делаем прокси UserService
      // Своя костыльная реализация, чтобы работала аннотация @Transactional в сервисе
      UserService userService = new ByteBuddy()
        // проверяем все методы класса UserService
        .subclass(UserService.class)
        .method(ElementMatchers.any())
        // все методы проверяем объектом TransactionInterceptor
        // этот объект смотрит на аннотацию @Transactional и открывает/закрывает транзакции
        .intercept(MethodDelegation.to(transactionInterceptor))
        // создает на ходу новый класс
        .make()
        // загружаем новый класс в jvm
        .load(UserService.class.getClassLoader())
        .getLoaded()
        // вызываем конструктор и создаем объект
        .getDeclaredConstructor(UserRepository.class, UserReadMapper.class, UserCreateMapper.class)
        .newInstance(userRepository, userReadMapper, userCreateMapper);

      UserCreateDto userCreateDto = new UserCreateDto(PersonalInfo.builder()
        .personalLastname("last4")
        .firstname("first4")
//        .birthDate(LocalDate.parse("2021-01-01"))
        .build(), "username9", null, null, 1);
      userService.create(userCreateDto);

      // транзакция явно не нужна при @Transactional
//      session.getTransaction().commit();
    }
  }

}
