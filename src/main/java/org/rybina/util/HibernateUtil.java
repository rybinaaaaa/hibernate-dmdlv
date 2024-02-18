package org.rybina.util;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.rybina.entity.User;
import org.rybina.intercepror.GlobalInterceptor;
import org.rybina.listener.AuditTableListener;

@UtilityClass
public class HibernateUtil {

    public static SessionFactory buildSessionFactory() {

        Configuration configuration = buildConfiguration();
        configuration.configure();

        //        registerListeners(sessionFactory);

        return configuration.buildSessionFactory();
    }

    private static void registerListeners(SessionFactory sessionFactory) {
        SessionFactoryImpl sessionFactoryImpl = sessionFactory.unwrap(SessionFactoryImpl.class);

        EventListenerRegistry eventListenerRegistry = sessionFactoryImpl.getServiceRegistry().getService(EventListenerRegistry.class);

        AuditTableListener auditTableListener = new AuditTableListener();

        eventListenerRegistry.appendListeners(EventType.PRE_INSERT, auditTableListener);
        eventListenerRegistry.appendListeners(EventType.PRE_DELETE, auditTableListener);
    }

    public static Configuration buildConfiguration() {
        Configuration configuration = new Configuration();
//        hibernate.cfg.xml by default
//        !!!
        configuration.addAnnotatedClass(User.class);
//        configuration.addAttributeConverter(new BirthdayConvertor(), true);
        configuration.registerTypeOverride(new JsonBinaryType());
        configuration.setInterceptor(new GlobalInterceptor());
        return configuration;
    }
}
