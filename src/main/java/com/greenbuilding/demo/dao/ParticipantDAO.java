package com.greenbuilding.demo.dao;

import com.greenbuilding.demo.entity.Participant;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;


public class ParticipantDAO {

    private EntityManager entityManager;
    public ParticipantDAO() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("dbPU");
        entityManager = emf.createEntityManager();
    }

    public void saveOrUpdate(Participant participant) {
        if (participant.getId() == null) {
            entityManager.persist(participant);
        } else {
            entityManager.merge(participant);
        }
    }

    public List<Participant> getParticipantsByFormationId(int IdFormation) {
        try {
            return entityManager.createQuery("select p from Participant p join FormationParticipant fp on p.id = fp.idparticipant.id where fp.idformation.id =: IdFormation", Participant.class)
                    .setParameter("IdFormation", IdFormation)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public Participant findById(int id) {
        return entityManager.find(Participant.class, id);
    }

    public List<Participant> findAll() {
        return entityManager.createQuery("select p from Participant p", Participant.class).getResultList();
    }

    public void delete(int id) {
        Participant participant = entityManager.find(Participant.class, id);
        if(participant!=null){
            entityManager.remove(participant);
        }
    }
}
