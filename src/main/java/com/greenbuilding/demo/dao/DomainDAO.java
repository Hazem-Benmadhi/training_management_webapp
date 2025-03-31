package com.greenbuilding.demo.dao;

import com.greenbuilding.demo.entity.Domain;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

import java.util.List;

public class DomainDAO {

    private EntityManager entityManager;
    public DomainDAO() {
        entityManager = Persistence.createEntityManagerFactory("dbPU").createEntityManager();
    }

    public void saveOrUpdate(Domain domain) {
        entityManager.getTransaction().begin();
        if (domain.getId() == null) {
            entityManager.persist(domain);
        } else {
            entityManager.merge(domain);
        }
        entityManager.getTransaction().commit();
    }

    public Domain findById(int id) {
        return entityManager.find(Domain.class, id);
    }

    public List<Domain> findAll() {
        return entityManager.createQuery("select d from Domain d", Domain.class).getResultList();
    }

    public void delete(int id) {
        Domain domain = entityManager.find(Domain.class, id);
        if(domain!=null){
            entityManager.getTransaction().begin();
            entityManager.remove(domain);
            entityManager.getTransaction().commit();
        }
    }

}
