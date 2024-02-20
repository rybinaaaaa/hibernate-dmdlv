package org.rybina.dao;

import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.rybina.entity.BaseEntity;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class RepositoryBase<K extends Serializable, E extends BaseEntity<K>> implements Repository<K, E> {
    private final EntityManager entityManager;
    private final Class<E> clazz;

    @Override
    public E save(E entity) {
//         Session session = sessionFactory.getCurrentSession();
//        session.save(entity);
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public void delete(K id) {
//         Session session = sessionFactory.getCurrentSession();
//        session.delete(id);
//        session.flush();
        entityManager.remove(id);
    }

    @Override
    public void update(E entity) {
//         Session session = sessionFactory.getCurrentSession();
//        session.merge(entity);
        entityManager.merge(entity);
    }

    @Override
    public Optional<E> findById(K id) {
//         Session session = sessionFactory.getCurrentSession();
//        return Optional.ofNullable(session.find(clazz, id));
        return Optional.ofNullable(entityManager.find(clazz, id));
    }

    @Override
    public List<E> findAll() {
//         Session session = sessionFactory.getCurrentSession();
        CriteriaQuery<E> criteria = entityManager.getCriteriaBuilder().createQuery(clazz);
        criteria.from(clazz);

        return entityManager.createQuery(criteria).getResultList();
    }
}
