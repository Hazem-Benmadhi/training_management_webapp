package com.greenbuilding.demo.dao;

import com.greenbuilding.demo.entity.Formation;
import com.greenbuilding.demo.entity.Participant;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import java.sql.SQLException;
import java.util.List;


public class RegistrationDAO {

    private final EntityManager entityManager;
    public RegistrationDAO() {
        entityManager = Persistence.createEntityManagerFactory("dbPU").createEntityManager();
    }

    public List<Formation> getAvailableFormations() {
        return entityManager.createQuery("SELECT f FROM Formation f", Formation.class).getResultList();
    }

    public boolean isRegistered(int idFormation, int idParticipant) {
        try {
            Participant participant = entityManager.find(Participant.class, idParticipant);
            Formation formation = entityManager.find(Formation.class, idFormation);

            if (participant == null || formation == null) {
                return false;
            }

            entityManager.refresh(participant);
            return participant.getFormations().contains(formation);
        } catch (Exception e) {
            return false;
        }
    }

    public void registerParticipant(int idParticipant, int idFormation) {
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            Participant participant = entityManager.find(Participant.class, idParticipant);
            Formation formation = entityManager.find(Formation.class, idFormation);

            if (participant == null || formation == null) {
                throw new IllegalArgumentException("Participant or Formation not found");
            }

            entityManager.refresh(participant);

            if (!participant.getFormations().contains(formation)) {
                participant.addFormation(formation);
                entityManager.merge(participant);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Registration failed", e);
        }
    }

//    public void unregisterParticipant(int idParticipant, int idFormation) {
//        entityManager.getTransaction().begin();
//
//        Participant participant = entityManager.find(Participant.class, idParticipant);
//        Formation formation = entityManager.find(Formation.class, idFormation);
//
//        if (participant != null && formation != null) {
//            participant.getFormations().remove(formation);
//            entityManager.merge(participant);
//        }
//
//        entityManager.getTransaction().commit();
//    }

    public boolean removeParticipantFromFormation(Participant participant, Formation formation) {
        try {
            entityManager.getTransaction().begin();
            formation.getParticipants().remove(participant);
            participant.getFormations().remove(formation);
            entityManager.merge(participant);
            entityManager.merge(formation);
            entityManager.getTransaction().commit();
            return true;
        }catch (Exception e) {
            return false;
        }
    }

}
