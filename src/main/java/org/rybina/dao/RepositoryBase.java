package org.rybina.dao;

import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.rybina.entity.BaseEntity;

import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class RepositoryBase<K extends Serializable, E extends BaseEntity<K>> implements Repository<K, E> {
    private final SessionFactory sessionFactory;
    private final Class<E> clazz;

    @Override
    public E save(E entity) {
        @Cleanup Session session = sessionFactory.openSession();
        session.save(entity);
        return entity;
    }

    @Override
    public void delete(K id) {
        @Cleanup Session session = sessionFactory.openSession();
        session.delete(id);
        session.flush();
    }

    @Override
    public void update(E entity) {
        @Cleanup Session session = sessionFactory.openSession();
        session.merge(entity);
    }

    @Override
    public Optional<E> findById(K id) {
        @Cleanup Session session = sessionFactory.openSession();
        return Optional.ofNullable(session.find(clazz, id));
    }

    @Override
    public List<E> findAll() {
        @Cleanup Session session = sessionFactory.openSession();
        CriteriaQuery<E> criteria = session.getCriteriaBuilder().createQuery(clazz);
        criteria.from(clazz);

        return session.createQuery(criteria).list();
    }
}
