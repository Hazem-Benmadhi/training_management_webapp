package com.greenbuilding.demo.dao;

import com.greenbuilding.demo.api.BaseDAO;
import com.greenbuilding.demo.entity.Participant;

import java.util.Collections;
import java.util.List;

public class ParticipantDAO extends BaseDAO<Participant, Integer> {

    public ParticipantDAO() {
        super(Participant.class);
    }

    public List<Participant> getParticipantsByFormationId(int IdFormation) {
        try {
            return entityManager.createQuery(
                            "SELECT p FROM Participant p JOIN p.formations f WHERE f.id = :IdFormation",
                            Participant.class)
                    .setParameter("IdFormation", IdFormation)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace(); // Optional: for debugging
            return Collections.emptyList(); // Better than returning null
        }
    }

    public void delete(Integer id) {
        try {
            Participant participant = entityManager.find(Participant.class, id);
            if (participant != null) {
                entityManager.getTransaction().begin();
                // Remove from all formations
                participant.getFormations().forEach(formation -> formation.getParticipants().remove(participant));
                entityManager.remove(participant);
                entityManager.getTransaction().commit();
            }
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }

    }

}
