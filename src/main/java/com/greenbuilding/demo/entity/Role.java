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
@Table(name = "role")
public class Role {
    @Id
    @ColumnDefault("nextval('role_id_seq')")
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

}