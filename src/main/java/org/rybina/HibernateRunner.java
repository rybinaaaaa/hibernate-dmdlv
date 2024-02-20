package org.rybina;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.rybina.dao.CompanyRepository;
import org.rybina.dao.PaymentRepository;
import org.rybina.dao.UserRepository;
import org.rybina.dto.UserCreateDto;
import org.rybina.entity.PersonalInfo;
import org.rybina.entity.User;
import org.rybina.interceptor.TransactionalInterceptor;
import org.rybina.mapper.CompanyReadMapper;
import org.rybina.mapper.UserCreateMapper;
import org.rybina.mapper.UserReadMapper;
import org.rybina.service.UserService;
import org.rybina.util.HibernateUtil;

import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.time.LocalDate;


public class HibernateRunner {

    @Transactional
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            User user = null;
            Session session = (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(), new Class[]{Session.class},
                    (proxy, method, args1) -> method.invoke(sessionFactory.getCurrentSession(), args1));

            session.beginTransaction();


            CompanyRepository companyRepository = new CompanyRepository(session);
            UserRepository userRepository = new UserRepository(session);
            PaymentRepository paymentRepository = new PaymentRepository(session);


            CompanyReadMapper companyReadMapper = new CompanyReadMapper();
            UserReadMapper userReadMapper = new UserReadMapper(companyReadMapper);
            UserCreateMapper userCreateMapper = new UserCreateMapper(companyRepository);


            UserService userService = new UserService(userRepository, userReadMapper, userCreateMapper);

            UserCreateDto userCreateDto = new UserCreateDto(PersonalInfo.builder()
                    .firstname("Liza").lastName("test").birthday(LocalDate.now()).build(),
                    "test liza", null, 1);

            userService.create(userCreateDto);
            userService.findById(1).ifPresent(System.out::println);

            session.getTransaction().commit();
        }
    }
}

