package com.greenbuilding.demo.dao;

import com.greenbuilding.demo.api.BaseDAO;
import com.greenbuilding.demo.entity.Participant;
import java.util.List;

public class ParticipantDAO extends BaseDAO<Participant, Integer> {

    public ParticipantDAO() {
        super(Participant.class);
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
}
