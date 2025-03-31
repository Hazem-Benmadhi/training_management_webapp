package com.greenbuilding.demo.dao;

import com.greenbuilding.demo.entity.Formation;
import com.greenbuilding.demo.entity.FormationParticipant;
import com.greenbuilding.demo.entity.Participant;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class RegistrationDAO {

    @PersistenceContext(unitName = "dbPU")
    private EntityManager entityManager;

    boolean isRegistered(int IdFormation, int IdParticipant) {
        return entityManager.createQuery(
                        "select count(fp) from FormationParticipant fp where fp.idformation.id =:IdFormation and fp.idparticipant.id =:IdParticipant",
                        Integer.class)
                .setParameter("IdFormation", IdFormation)
                .setParameter("IdParticipant", IdParticipant)
                .getSingleResult() > 0;
    }

    // Handle registration of a participant for a formation
    public boolean registerParticipant(int IdParticipant, int IdFormation, Participant newParticipant) {

        Formation formation = entityManager.find(Formation.class, IdFormation); // Search for the formation using the IdFormation
        Participant participant = entityManager.find(Participant.class, IdParticipant); // Search for the participant using the IdParticipant

        // Reject registration if formation not exist
        if (formation == null) {
            return false;
        }

        // Creat and Save participant if it's not exist
        // Can be a separate function
        if (participant == null) {
            entityManager.persist(newParticipant);
            participant = newParticipant;
        }

        // Check if the participant is already registered for the formation
        if (isRegistered(IdFormation, IdParticipant)) {
            return false;   // Already registered
        }

        // Register participant to the formation
        FormationParticipant fp = new FormationParticipant();
        fp.setIdformation(formation);
        fp.setIdparticipant(participant);

        entityManager.persist(fp);
        return true;
    }
}
