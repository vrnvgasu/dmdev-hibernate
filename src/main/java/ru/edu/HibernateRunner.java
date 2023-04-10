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

      // PESSIMISTIC_FORCE_INCREMENT 0 плохая практика.
      // Для списка будет сделан запрос на каждую запись, чтобы обновить версионность
      session.createQuery("select p from Payment p", Payment.class)
        .setLockMode(LockModeType.PESSIMISTIC_FORCE_INCREMENT)
        // timeout 5 сек на блокировку на запрос, потом блокировка будет снята
        .setHint("javax.persistence.lock.timeout", 5000)
        .list();

      // в двух сессиях берем одну запись и пытаемся ее изменить
      var payment = session.find(Payment.class, 1L, LockModeType.PESSIMISTIC_FORCE_INCREMENT);
      payment.setAmount(payment.getAmount() + 10);

      var theSamePayment = session1.find(Payment.class, 1L);
      theSamePayment.setAmount(theSamePayment.getAmount() + 20);

      // session1 зависнет, т.к. не может сделать коммит
      // посколько session заблокировала доступ через PESSIMISTIC_READ - for share
      // PESSIMISTIC_WRITE - for update
      // PESSIMISTIC_FORCE_INCREMENT - for update nowait
      session1.getTransaction().commit();
      session.getTransaction().commit();
    }
  }

}
