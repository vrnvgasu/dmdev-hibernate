package ru.edu.util;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.edu.converter.BirthdateConverter;

@UtilityClass
public class HibernateUtil {

  public static SessionFactory buildSessionFactory() {
    Configuration configuration = buildConfiguration();

    // подтягивает данные из hibernate.cfg.xml
    configuration.configure();

    return configuration.buildSessionFactory();
  }

  public static Configuration buildConfiguration() {
    Configuration configuration = new Configuration();
    configuration.addAttributeConverter(new BirthdateConverter());
    configuration.registerTypeOverride(new JsonBinaryType());

    return configuration;
  }

}
