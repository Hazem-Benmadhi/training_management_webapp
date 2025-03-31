package com.greenbuilding.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;


@Entity
@Data @NoArgsConstructor @AllArgsConstructor
@Table(name = "profil")
public class Profil {
    @Id
    @ColumnDefault("nextval('profil_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "libelle", nullable = false)
    private String libelle;

}