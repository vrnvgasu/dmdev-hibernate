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
//        session1.beginTransaction();
//
//        var payment = session1.find(Payment.class, 1L);
//        payment.setAmount(payment.getAmount() + 10);
//
//        session1.getTransaction().commit();
      }
      try (var session2 = sessionFactory.openSession()) {
        session2.beginTransaction();

        // AuditReader - позволяет общаться к сущностям envers (типа payment_aud)
        AuditReader auditReader = AuditReaderFactory.get(session2);
        // можем искать сущности по revinfo.rev или revtstmp.revtstmp
        Payment oldPayment = auditReader.find(Payment.class, 2L, 1L);
//        Payment oldPayment = auditReader.find(Payment.class, 1L, new Date(1681508157598L));
        System.out.println(oldPayment); // получили Payment со значениями для этой транзакции
        // перезапишем текущее значение старым из транзакции
        session2.replicate(oldPayment, ReplicationMode.OVERWRITE);

//         все записи payment на момент транзакции ближайшей к 400L
        var payments = auditReader.createQuery()
          // ищет по ревизии <= 400L
          .forEntitiesAtRevision(Payment.class, 400L)
          // поддерживает criteria api
          .add(AuditEntity.property("amount").ge(450))
          .add(AuditEntity.property("id").ge(5L))
          // вернуть только поле amount и id
          .addProjection(AuditEntity.property("amount"))
          .addProjection(AuditEntity.property("id"))
          .getResultList();
        System.out.println(payments);

        session2.getTransaction().commit();
      }

    }
  }

}
