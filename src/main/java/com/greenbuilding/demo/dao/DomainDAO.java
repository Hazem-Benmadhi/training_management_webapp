package com.greenbuilding.demo.dao;

import com.greenbuilding.demo.api.BaseDAO;
import com.greenbuilding.demo.entity.Domain;

import java.util.List;

public class DomainDAO extends BaseDAO<Domain, Integer> {

    public DomainDAO() {
        super(Domain.class);
    }

    public void save(Domain domain) {
        entityManager.getTransaction().begin();
        entityManager.persist(domain);
        entityManager.getTransaction().commit();
    }

//    public List<Object[]> getDomainDistribution() {
//        return entityManager.createQuery(
//                        "SELECT d.libelle, COUNT(p) " +
//                                "FROM Domain d " +
//                                "JOIN Formation f ON f.domain.id = d.id " +
//                                "JOIN Participant p ON p.formations.id = f.id " +
//                                "GROUP BY d.libelle " +
//                                "ORDER BY COUNT(p) DESC", Object[].class)
//                .getResultList();
//    }

}
