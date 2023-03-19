package ru.edu.util;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.edu.converter.BirthdateConverter;

@UtilityClass
public class HibernateUtil {

  public static SessionFactory buildSessionFactory() {
    Configuration configuration = new Configuration();
    configuration.configure();
    configuration.addAttributeConverter(new BirthdateConverter());
    configuration.registerTypeOverride(new JsonBinaryType());

    return configuration.buildSessionFactory();
  }

}
