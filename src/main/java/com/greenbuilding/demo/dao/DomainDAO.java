package com.greenbuilding.demo.dao;

import com.greenbuilding.demo.api.BaseDAO;
import com.greenbuilding.demo.entity.Domain;

public class DomainDAO extends BaseDAO<Domain, Integer> {

    public DomainDAO() {
        super(Domain.class);
    }
//    private EntityManager entityManager;
//    public DomainDAO() {
//        entityManager = Persistence.createEntityManagerFactory("dbPU").createEntityManager();
//    }
//
    public void save(Domain domain) {
        entityManager.getTransaction().begin();
        entityManager.persist(domain);
        entityManager.getTransaction().commit();
    }
//
//    public Domain findById(int id) {
//        return entityManager.find(Domain.class, id);
//    }
//
//    public List<Domain> findAll() {
//        return entityManager.createQuery("select d from Domain d", Domain.class).getResultList();
//    }
//
//    public void delete(int id) {
//        Domain domain = entityManager.find(Domain.class, id);
//        if(domain!=null){
//            entityManager.getTransaction().begin();
//            entityManager.remove(domain);
//            entityManager.getTransaction().commit();
//        }
//    }
}
