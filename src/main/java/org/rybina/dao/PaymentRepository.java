package org.rybina.dao;

import org.hibernate.SessionFactory;
import org.rybina.entity.Payment;

import javax.persistence.EntityManager;

public class PaymentRepository extends RepositoryBase<Integer, Payment> {

    public PaymentRepository(EntityManager entityManager) {
        super(entityManager, Payment.class);
    }
}
