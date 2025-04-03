package com.greenbuilding.demo.dao;

import com.greenbuilding.demo.entity.Formation;
import com.greenbuilding.demo.entity.FormationParticipant;
import com.greenbuilding.demo.entity.FormationParticipantId;
import com.greenbuilding.demo.entity.Participant;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

import java.util.List;


public class RegistrationDAO {

    private EntityManager entityManager;
    public RegistrationDAO() {
        entityManager = Persistence.createEntityManagerFactory("dbPU").createEntityManager();
    }
//    public boolean isRegistered(int IdFormation, int IdParticipant) {
//        return entityManager.createQuery(
//                        "select count(fp) from FormationParticipant fp where fp.idformation.id =:IdFormation and fp.idparticipant.id =:IdParticipant",
//                        Integer.class)
//                .setParameter("IdFormation", IdFormation)
//                .setParameter("IdParticipant", IdParticipant)
//                .getSingleResult() > 0;
//    }
    public boolean isRegistered(int IdParticipant) {
        return entityManager.createQuery(
                        "select count(fp) from FormationParticipant fp where fp.idparticipant.id =:IdParticipant",
                        Integer.class)
                .setParameter("IdParticipant", IdParticipant)
                .getSingleResult() > 0;
    }

    public List<Formation> getAvailableFormations() {
        return entityManager.createQuery("SELECT f FROM Formation f", Formation.class).getResultList();
    }

    public boolean registerParticipant(int IdParticipant, int IdFormation) {

        Formation formation = entityManager.find(Formation.class, IdFormation); // Search for the formation using the IdFormation
        Participant participant = entityManager.find(Participant.class, IdParticipant); // Search for the participant using the IdParticipant

        // Reject registration if formation or participant not exist
        if (formation == null || participant == null) {
            return false;
        }

        // Check if the participant is already registered for the formation
        if (isRegistered(IdParticipant)) {
            return false;   // Already registered
        }

        // Register participant to the formation
        FormationParticipant fp = new FormationParticipant();
        fp.setId(new FormationParticipantId(IdFormation, IdParticipant));
        fp.setIdformation(formation);
        fp.setIdparticipant(participant);

        entityManager.getTransaction().begin();
        entityManager.persist(fp);
        entityManager.getTransaction().commit();
        return true;
    }

    // Handle registration of a participant for a formation
//    public boolean registerParticipant(int IdParticipant, int IdFormation, Participant newParticipant) {
//
//        Formation formation = entityManager.find(Formation.class, IdFormation); // Search for the formation using the IdFormation
//        Participant participant = entityManager.find(Participant.class, IdParticipant); // Search for the participant using the IdParticipant
//
//        // Reject registration if formation not exist
//        if (formation == null) {
//            return false;
//        }
//
//        // Creat and Save participant if it's not exist
//        // Can be a separate function
//        if (participant == null) {
//            entityManager.persist(newParticipant);
//            participant = newParticipant;
//        }
//
//        // Check if the participant is already registered for the formation
//        if (isRegistered(IdParticipant)) {
//            return false;   // Already registered
//        }
//
//        // Register participant to the formation
//        FormationParticipant fp = new FormationParticipant();
//        fp.setIdformation(formation);
//        fp.setIdparticipant(participant);
//
//        entityManager.persist(fp);
//        return true;
//    }
}
