package com.greenbuilding.demo.api;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

import java.util.List;

public abstract class BaseDAO<T, ID> {

    protected EntityManager entityManager;
    private final Class<T> entityClass;

    protected BaseDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.entityManager = Persistence.createEntityManagerFactory("dbPU").createEntityManager();
    }

    public void saveOrUpdate(T entity, ID id) {
        entityManager.getTransaction().begin();
        if (id == null) {
            entityManager.persist(entity);
        } else {
            entityManager.merge(entity);
        }
        entityManager.getTransaction().commit();
    }

    public T findById(ID id) {
        return entityManager.find(entityClass, id);
    }

    public List<T> findAll() {
        return entityManager.createQuery("select e from " + entityClass.getSimpleName() + " e", entityClass).getResultList();
    }

    public void delete(ID id) {
        T entity = entityManager.find(entityClass, id);
        if (entity != null) {
            entityManager.getTransaction().begin();
            entityManager.remove(entity);
            entityManager.getTransaction().commit();
        }
    }
}
