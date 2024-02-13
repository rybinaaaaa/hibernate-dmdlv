package org.rybina.util;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.rybina.entity.User;

@UtilityClass
public class HibernateUtil {

    public static SessionFactory buildSessionFactory() {

        Configuration configuration = buildConfiguration();
        configuration.configure();

        return configuration.buildSessionFactory();
    }

    public static Configuration buildConfiguration() {
        Configuration configuration = new Configuration();
//        hibernate.cfg.xml by default
//        !!!
        configuration.addAnnotatedClass(User.class);
//        configuration.addAttributeConverter(new BirthdayConvertor(), true);
        configuration.registerTypeOverride(new JsonBinaryType());
        return configuration;
    }
}
