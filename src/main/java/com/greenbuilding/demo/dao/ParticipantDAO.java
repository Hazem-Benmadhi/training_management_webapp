package com.greenbuilding.demo.dao;

import com.greenbuilding.demo.api.BaseDAO;
import com.greenbuilding.demo.entity.Participant;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParticipantDAO extends BaseDAO<Participant, Integer> {

    public ParticipantDAO() {
        super(Participant.class);
    }

//    public List<Participant> getParticipantsByFormationId(int IdFormation) {
//        try {
//            return entityManager.createQuery(
//                            "SELECT p FROM Participant p JOIN p.formations f WHERE f.id = :IdFormation",
//                            Participant.class)
//                    .setParameter("IdFormation", IdFormation)
//                    .getResultList();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Collections.emptyList();
//        }
//    }
//
//    public List<Participant> listAll() {
//        try {
//            TypedQuery<Participant> query = entityManager.createQuery(
//                    "SELECT DISTINCT p FROM Participant p LEFT JOIN FETCH p.formations",
//                    Participant.class
//            );
//            return query.getResultList();
//        } catch (Exception e) {
//            System.err.println("Error listing participants: " + e.getMessage());
//            return Collections.emptyList();
//        }
//    }

    @Override
    public void delete(Integer id) {
        try {
            Participant participant = entityManager.find(Participant.class, id);
            if (participant != null) {
                entityManager.getTransaction().begin();
                participant.getFormations().forEach(formation -> formation.getParticipants().remove(participant));
                entityManager.remove(participant);
                entityManager.getTransaction().commit();
            }
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }

    }

    public Map<Integer, Integer> getFormationCounts() {
        Map<Integer, Integer> counts = new HashMap<>();
        List<Participant> participants = findAll();
        for (Participant participant : participants) {
            entityManager.refresh(participant);
            counts.put(participant.getId(), participant.countFormations());
        }
        return counts;
    }
}
