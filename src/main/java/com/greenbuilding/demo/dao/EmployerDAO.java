package com.greenbuilding.demo.dao;

import com.greenbuilding.demo.api.BaseDAO;
import com.greenbuilding.demo.entity.Employer;

public class EmployerDAO extends BaseDAO<Employer, Integer> {

    public EmployerDAO() {
        super(Employer.class);
    }

    public void save(Employer employer) {
        try{
            entityManager.getTransaction().begin();
            entityManager.persist(employer);
            entityManager.getTransaction().commit();
        }catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException(e);
        }
    }
}
