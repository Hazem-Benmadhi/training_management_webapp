package com.greenbuilding.demo.dao;

import com.greenbuilding.demo.api.BaseDAO;
import com.greenbuilding.demo.entity.Formation;

import java.util.Collections;
import java.util.List;

public class FormationDAO extends BaseDAO<Formation, Integer> {

    public FormationDAO() {
        super(Formation.class);
    }
//    private EntityManager entityManager;
//    public FormationDAO() {
//        entityManager = Persistence.createEntityManagerFactory("dbPU").createEntityManager();
//    }
//
//    public void saveOrUpdate(Formation formation) {
//        entityManager.getTransaction().begin();
//        if (formation.getId() == null) {
//            entityManager.persist(formation);
//        } else {
//            entityManager.merge(formation);
//        }
//        entityManager.getTransaction().commit();
//    }
//
//
//    public Formation findById(int id) {
//        return entityManager.find(Formation.class, id);
//    }
//
//    public List<Formation> findAll() {
//       return entityManager.createQuery("select f from Formation f", Formation.class).getResultList();
//    }
//
//    public void delete(int id) {
//        Formation formation = entityManager.find(Formation.class, id);
//        if(formation!=null){
//            entityManager.getTransaction().begin();
//            entityManager.remove(formation);
//            entityManager.getTransaction().commit();
//        }
//    }

    public List<Formation> getFormationsByIdParticipant(int IdParticipant) {
    try {
        return entityManager.createQuery(
                        "SELECT f FROM Formation f JOIN f.participants p WHERE p.id = :IdParticipant",
                        Formation.class)
                .setParameter("IdParticipant", IdParticipant)
                .getResultList();
    } catch (Exception e) {
        e.printStackTrace(); // Optional: for debugging
        return Collections.emptyList(); // Safer than null
    }
}

}
