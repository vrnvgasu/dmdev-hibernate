package ru.edu.util;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import ru.edu.converter.BirthdateConverter;
import ru.edu.entity.Audit;
import ru.edu.interceptor.GlobalInterceptor;
import ru.edu.listener.AuditTableListener;

@UtilityClass
public class HibernateUtil {

  public static SessionFactory buildSessionFactory() {
    Configuration configuration = buildConfiguration();

    // подтягивает данные из hibernate.cfg.xml
    configuration.configure();
    var sessionFactory = configuration.buildSessionFactory();

//    registerListeners(sessionFactory);

    return sessionFactory;
  }

  private static void registerListeners(SessionFactory sessionFactory) {
    // надо зарегистрировать листрнер.
    // У интерфейса EntityManagerFactory нет такого метода,
    // а у реализации SessionFactoryImpl есть. Приводим явно к SessionFactoryImpl
    // По сути unwrap тоже, что (SessionFactoryImpl) sessionFactory
    SessionFactoryImpl sessionFactoryImp = sessionFactory.unwrap(SessionFactoryImpl.class);

    EventListenerRegistry eventListenerRegistry = sessionFactoryImp.getServiceRegistry().getService(EventListenerRegistry.class);

    var auditTableListener = new AuditTableListener();
    eventListenerRegistry.appendListeners(EventType.PRE_INSERT, auditTableListener);
    eventListenerRegistry.appendListeners(EventType.PRE_DELETE, auditTableListener);
  }

  public static Configuration buildConfiguration() {
    Configuration configuration = new Configuration();

    // вместо <mapping class="ru.edu.entity.Audit"/>
    configuration.addAnnotatedClass(Audit.class);

    configuration.addAttributeConverter(new BirthdateConverter());
    configuration.registerTypeOverride(new JsonBinaryType());

    // подключаем interceptor глобально для всех сессий
//    configuration.setInterceptor(new GlobalInterceptor());

    return configuration;
  }

}
