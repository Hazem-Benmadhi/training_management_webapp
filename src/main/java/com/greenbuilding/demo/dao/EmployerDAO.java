package com.greenbuilding.demo.dao;

import com.greenbuilding.demo.entity.Domain;
import com.greenbuilding.demo.entity.Employer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

import java.util.List;

public class EmployerDAO {
    private EntityManager entityManager;
    public EmployerDAO() {
        entityManager = Persistence.createEntityManagerFactory("dbPU").createEntityManager();
    }

    public void save(Domain domain) {
        entityManager.getTransaction().begin();
        entityManager.persist(domain);
        entityManager.getTransaction().commit();
    }

    public Employer findById(int id) {
        return entityManager.find(Employer.class, id);
    }

    public List<Employer> findAll() {
        return entityManager.createQuery("select e from Employer e", Employer.class).getResultList();
    }

    public void delete(int id) {
        Employer employer = entityManager.find(Employer.class, id);
        if(employer!=null){
            entityManager.getTransaction().begin();
            entityManager.remove(employer);
            entityManager.getTransaction().commit();
        }
    }
}
