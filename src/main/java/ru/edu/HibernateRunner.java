package ru.edu;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.graph.RootGraph;
import org.hibernate.graph.SubGraph;
import ru.edu.entity.User;
import ru.edu.entity.UserChat;
import ru.edu.util.HibernateUtil;

@Slf4j// генерит строку private static final Logger log
public class HibernateRunner {

  // выбираем org.slf4j.Logger
  // HibernateRunner.class будет передаваться в %c в конфиг логгера
  // можно заменить аннотацией @Slf4j
//  private static final Logger log = LoggerFactory.getLogger(HibernateRunner.class);

  public static void main(String[] args) {
    try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
      Session session = sessionFactory.openSession()) {
      session.beginTransaction();

//      session.enableFetchProfile("withCompanyAndPayment");

      // написали по быстрому граф вместо огромной аннотации @NamedEntityGraph
      RootGraph<User> userGraph = session.createEntityGraph(User.class);
      userGraph.addAttributeNodes("company", "userChats");
      SubGraph<UserChat> userChatSubGraph = userGraph.addSubgraph("userChats", UserChat.class);
      userChatSubGraph.addAttributeNodes("chat");

//      RootGraph<?> graph = session.getEntityGraph("WithCompanyAndChat");

      //var user = session.get(User.class, 1L); get не подходит для графа. Вместо него find
      Map<String, Object> properties = Map.of(GraphSemantic.LOAD.getJpaHintName(), userGraph);
      var user = session.find(User.class, 1L, properties);
      System.out.println(user.getUserChats().size());
      System.out.println(user.getCompany().getName());

      var users = session.createQuery("select u from User u", User.class)
        .setHint(GraphSemantic.LOAD.getJpaHintName(), userGraph)
        .list();
      users.forEach(u -> System.out.println(u.getCompany()));
      users.forEach(u -> System.out.println(u.getUserChats()));

      session.getTransaction().commit();
    }
  }

}
