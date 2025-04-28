package com.greenbuilding.demo.dao;

import com.greenbuilding.demo.api.BaseDAO;
import com.greenbuilding.demo.entity.Formation;
import jakarta.persistence.TypedQuery;

import java.util.Collections;
import java.util.List;

public class FormationDAO extends BaseDAO<Formation, Integer> {

    public FormationDAO() {
        super(Formation.class);
    }

    public List<Formation> getFormationsByParticipantId(int IdParticipant) {
        try {
            return entityManager.createQuery("SELECT f FROM Formation f JOIN f.participants p WHERE p.id = :IdParticipant", Formation.class)
                    .setParameter("IdParticipant", IdParticipant)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace(); // Optional: for debugging
            return Collections.emptyList(); // Safer than null
        }
    }

    public List<Formation> listAll() {
        try {
            TypedQuery<Formation> query = entityManager.createQuery(
                    "SELECT DISTINCT f FROM Formation f LEFT JOIN FETCH f.participants",
                    Formation.class
            );
            return query.getResultList();
        } catch (Exception e) {
            // Log the error
            System.err.println("Error listing formations: " + e.getMessage());
            return Collections.emptyList();
        }
    }

}
