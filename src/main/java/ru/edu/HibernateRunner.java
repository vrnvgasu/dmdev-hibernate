package ru.edu;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
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

@Slf4j// генерит строку private static final Logger log
public class HibernateRunner {

  @Transactional
  // Transactional - из JPA. Тут реализация должна открыть сессию, обернуть транзакцию в try
  // откатить транзакцию при ошибке. НО... hibernate это не реализует.
  // НО... в спринге это реализовано (такой класс с Transactional
  //   просто оборачивается в прокси с нужными методами)
  public static void main(String[] args) {
    try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
      Session session = sessionFactory.openSession()) {
      session.doWork(new Work() {
        @Override
        public void execute(Connection connection) throws SQLException {
          System.out.println(connection.getTransactionIsolation());
        }
      });
      // тоже через lambda
      session.doWork(connection -> System.out.println(connection.getTransactionIsolation()));

      Transaction transaction = session.beginTransaction();
      try {
//        var payment = session.find(Payment.class, 1L);

        transaction.commit();
      } catch (Exception e) {
        // надо проверить состояние сессии. Если в ней уже был rollback, то словим ошибку
        transaction.rollback();
        throw e;
      }
    }
  }

}
