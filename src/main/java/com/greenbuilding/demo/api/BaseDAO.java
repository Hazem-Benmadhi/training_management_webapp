package com.greenbuilding.demo.api;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import java.lang.reflect.Method;
import java.util.List;


public abstract class BaseDAO<T, ID> {

    protected EntityManager entityManager;
    private final Class<T> entityClass;

    protected BaseDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.entityManager = Persistence.createEntityManagerFactory("dbPU").createEntityManager();
    }

    public void saveOrUpdate(T entity) {
        try {
            Method getIdMethod = entityClass.getMethod("getId");
            Object id = getIdMethod.invoke(entity);
            System.out.println("Entity ID: " + id);

            entityManager.getTransaction().begin();
            if (id == null) {
                entityManager.persist(entity);
            } else {
                entityManager.merge(entity);
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException("Error saving or updating " + entityClass.getSimpleName(), e);
        }
    }

    public T findById(ID id) {
        return entityManager.find(entityClass, id);
    }

    public List<T> findAll() {
        return entityManager.createQuery("select e from " + entityClass.getSimpleName() + " e", entityClass).getResultList();
    }

    public void delete(ID id) {
        try {
            T entity = entityManager.find(entityClass, id);
            if (entity != null) {
                entityManager.getTransaction().begin();
                entityManager.remove(entity);
                entityManager.getTransaction().commit();
            }
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException("Error deleting " + entityClass.getSimpleName(), e);
        }
    }
}
