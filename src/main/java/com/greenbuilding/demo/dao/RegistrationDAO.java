package com.greenbuilding.demo.dao;

import com.greenbuilding.demo.entity.Formation;
import com.greenbuilding.demo.entity.Participant;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Persistence;

import java.util.List;


public class RegistrationDAO {

    private final EntityManager entityManager;
    public RegistrationDAO() {
        entityManager = Persistence.createEntityManagerFactory("dbPU").createEntityManager();
    }

    public boolean isRegistered(int idFormation, int idParticipant) {
        Participant participant = entityManager.find(Participant.class, idParticipant);
        Formation formation = entityManager.find(Formation.class, idFormation);

        return participant != null && formation != null && participant.getFormations().contains(formation);
    }


    public List<Formation> getAvailableFormations() {
        return entityManager.createQuery("SELECT f FROM Formation f", Formation.class).getResultList();
    }

    public void registerParticipant(int idParticipant, int idFormation) {
        entityManager.getTransaction().begin();

        Participant participant = entityManager.find(Participant.class, idParticipant);
        Formation formation = entityManager.find(Formation.class, idFormation);

        if (participant == null || formation == null) {
            entityManager.getTransaction().rollback();
        }

        // Check if participant not registered to the given formation
        if (!participant.getFormations().contains(formation)) {
            participant.getFormations().add(formation);
            entityManager.merge(participant);
        }

        entityManager.getTransaction().commit();
    }

    public void unregisterParticipant(int idParticipant, int idFormation) {
        entityManager.getTransaction().begin();

        Participant participant = entityManager.find(Participant.class, idParticipant);
        Formation formation = entityManager.find(Formation.class, idFormation);

        if (participant != null && formation != null) {
            participant.getFormations().remove(formation);
//            formation.getParticipants().remove(participant);
            entityManager.merge(participant);
//            entityManager.merge(formation);
        }

        entityManager.getTransaction().commit();
    }

}
