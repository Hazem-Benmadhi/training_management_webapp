package com.greenbuilding.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import java.util.Objects;


@Embeddable
@Data @NoArgsConstructor @AllArgsConstructor

public class FormationParticipantId implements java.io.Serializable {
    private static final long serialVersionUID = -7351622528367609239L;
    @NotNull
    @Column(name = "idformation", nullable = false)
    private Integer idformation;

    @NotNull
    @Column(name = "idparticipant", nullable = false)
    private Integer idparticipant;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FormationParticipantId entity = (FormationParticipantId) o;
        return Objects.equals(this.idparticipant, entity.idparticipant) &&
                Objects.equals(this.idformation, entity.idformation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idparticipant, idformation);
    }

}