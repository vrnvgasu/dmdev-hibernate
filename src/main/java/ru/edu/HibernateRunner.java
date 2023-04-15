package ru.edu;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
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
import ru.edu.entity.Payment;
import ru.edu.entity.User;
import ru.edu.entity.UserChat;
import ru.edu.interceptor.GlobalInterceptor;
import ru.edu.util.HibernateUtil;
import ru.edu.util.TestDataImporter;

@Slf4j// генерит строку private static final Logger log
public class HibernateRunner {

  public static void main(String[] args) {
    try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
//      TestDataImporter.importData(sessionFactory);

      try (var session1 = sessionFactory.openSession()) {
        session1.beginTransaction();

        var payment = session1.find(Payment.class, 1L);
        payment.setAmount(payment.getAmount() + 10);

        session1.getTransaction().commit();
      }

    }
  }

}
