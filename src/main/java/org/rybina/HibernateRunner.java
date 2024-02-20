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
import java.sql.SQLException;
import java.time.LocalDate;


public class HibernateRunner {

    @Transactional
    public static void main(String[] args) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            var session = (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(), new Class[]{Session.class},
                    (proxy, method, args1) -> method.invoke(sessionFactory.getCurrentSession(), args1));
//            session.beginTransaction();

            var companyRepository = new CompanyRepository(session);

            var companyReadMapper = new CompanyReadMapper();
            var userReadMapper = new UserReadMapper(companyReadMapper);
            var userCreateMapper = new UserCreateMapper(companyRepository);

            var userRepository = new UserRepository(session);
            var paymentRepository = new PaymentRepository(session);
//            var userService = new UserService(userRepository, userReadMapper, userCreateMapper);
            var transactionInterceptor = new TransactionalInterceptor(sessionFactory);

            var userService = new ByteBuddy()
                    .subclass(UserService.class)
                    .method(ElementMatchers.any())
                    .intercept(MethodDelegation.to(transactionInterceptor))
                    .make()
                    .load(UserService.class.getClassLoader())
                    .getLoaded()
                    .getDeclaredConstructor(UserRepository.class, UserReadMapper.class, UserCreateMapper.class)
                    .newInstance(userRepository, userReadMapper, userCreateMapper);

            userService.findById(1).ifPresent(System.out::println);

            UserCreateDto userCreateDto = new UserCreateDto(
                    PersonalInfo.builder()
                            .firstname(null)
                            .lastName("Stepanova")
                            .birthday(LocalDate.now())
                            .build(),
                    "liza2@gmail.com",
                    null,
                    1
            );
            userService.create(userCreateDto);
        }
    }
}

