package com.greenbuilding.demo.dao;

import com.greenbuilding.demo.entity.Trainer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

import java.util.List;

public class TrainerDAO {

    private EntityManager entityManager;
    public TrainerDAO() {
        entityManager = Persistence.createEntityManagerFactory("dbPU").createEntityManager();
    }

    public void saveOrUpdate(Trainer trainer) {
        entityManager.getTransaction().begin();
        if (trainer.getId() == null) {
            entityManager.persist(trainer);
        } else {
            entityManager.merge(trainer);
        }
        entityManager.getTransaction().commit();
    }

    public Trainer findById(int id) {
        return entityManager.find(Trainer.class, id);
    }

    public List<Trainer> findAll() {
        return entityManager.createQuery("select t from Trainer t", Trainer.class).getResultList();
    }

    public void delete(int id) {
        Trainer trainer = entityManager.find(Trainer.class, id);
        if(trainer!=null){
            entityManager.getTransaction().begin();
            entityManager.remove(trainer);
            entityManager.getTransaction().commit();
        }
    }
}
