package org.rybina.dao;

import org.rybina.entity.User;

import javax.persistence.EntityManager;

public class UserRepository extends RepositoryBase<Integer, User> {

    public UserRepository(EntityManager entityManager) {
        super(entityManager, User.class);
    }
}
