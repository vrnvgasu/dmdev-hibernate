package ru.edu;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.annotations.QueryHints;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.graph.RootGraph;
import org.hibernate.graph.SubGraph;
import org.hibernate.jdbc.Work;
import ru.edu.entity.Payment;
import ru.edu.entity.User;
import ru.edu.entity.UserChat;
import ru.edu.util.HibernateUtil;
import ru.edu.util.TestDataImporter;

@Slf4j// генерит строку private static final Logger log
public class HibernateRunner {

  public static void main(String[] args) {
    try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
      Session session = sessionFactory.openSession()) {

      //ReadOnly для всего
//      session.setDefaultReadOnly(true);

      session.beginTransaction();

      // бросит ошибку при измении бд
      session.createNativeQuery("SET TRANSACTION READ ONLY").executeUpdate();

      // ReadOnly для запроса
//      session.createQuery("select p from Payment p", Payment.class)
//        .setLockMode(LockModeType.PESSIMISTIC_FORCE_INCREMENT)
//        .setReadOnly(true)
//        // ИЛИ
////        .setHint(QueryHints.READ_ONLY, true)
//        .list();

      var payment = session.find(Payment.class, 1L);
      // ReadOnly для объекта payment
//      session.setReadOnly(payment, true);
      // не сделает изменения при завершении транзакции, т.к. ReadOnly выше
      payment.setAmount(payment.getAmount() + 10);


      session.getTransaction().commit();
    }
  }

}
