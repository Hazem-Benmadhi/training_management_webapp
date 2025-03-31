package com.greenbuilding.demo.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
@Table(name = "formation_participant")
public class FormationParticipant {
    @EmbeddedId
    private FormationParticipantId id;

    @MapsId("idformation")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "idformation", nullable = false)
    private Formation idformation;

    @MapsId("idparticipant")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "idparticipant", nullable = false)
    private com.greenbuilding.demo.entity.Participant idparticipant;

}