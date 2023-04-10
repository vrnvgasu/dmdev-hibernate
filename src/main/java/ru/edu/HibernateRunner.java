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
      Session session = sessionFactory.openSession();
      Session session1 = sessionFactory.openSession()) {
      session.beginTransaction();
      session1.beginTransaction();

      // вместо LockModeType.OPTIMISTIC можно использовать @OptimisticLocking у класса
      // OPTIMISTIC изменяет поле версии при изменении записи
      // OPTIMISTIC_FORCE_INCREMENT - изменяет поле версии при любом запросе (даже при select)

      // в двух сессиях берем одну запись и пытаемся ее изменить
      var payment = session.find(Payment.class, 1L);
      payment.setAmount(payment.getAmount() + 10);

      var theSamePayment = session1.find(Payment.class, 1L);
      theSamePayment.setAmount(theSamePayment.getAmount() + 20);

      session.getTransaction().commit(); // первый коммит победит
      session1.getTransaction().commit(); // второй коммит бросит OptimisticLockException
    }
  }

}
