package org.rybina.dao;

import org.hibernate.SessionFactory;
import org.rybina.entity.Payment;

public class PaymentRepository extends RepositoryBase<Integer, Payment> {

    public PaymentRepository(SessionFactory sessionFactory) {
        super(sessionFactory, Payment.class);
    }
}
