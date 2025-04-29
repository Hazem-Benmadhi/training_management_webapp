package com.greenbuilding.demo.dao;

import com.greenbuilding.demo.api.BaseDAO;
import com.greenbuilding.demo.entity.Domain;
import jakarta.persistence.Query;

public class DomainDAO extends BaseDAO<Domain, Integer> {

    public DomainDAO() {
        super(Domain.class);
    }

    public void save(Domain domain) {
        entityManager.getTransaction().begin();
        entityManager.persist(domain);
        entityManager.getTransaction().commit();
    }
    public boolean domainExist(String libelle) {
        Query query = entityManager.createQuery(
                "SELECT COUNT(d) FROM Domain d WHERE d.libelle = :libelle"
        );
        query.setParameter("libelle", libelle);
        return (Long) query.getSingleResult() > 0;
    }
}
