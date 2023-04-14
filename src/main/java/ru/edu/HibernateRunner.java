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
      TestDataImporter.importData(sessionFactory);

      //ReadOnly для всего
//      session.setDefaultReadOnly(true);

      session.beginTransaction();



      var payment = session.find(Payment.class, 1L);
      // ReadOnly для объекта payment
//      session.setReadOnly(payment, true);
      // не сделает изменения при завершении транзакции, т.к. ReadOnly выше
      payment.setAmount(payment.getAmount() + 10);

      session.save(payment);


      session.getTransaction().commit();
    }
  }

}
