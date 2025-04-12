package com.greenbuilding.demo.entity;

//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotNull;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//@Entity
//@Data @NoArgsConstructor @AllArgsConstructor
//@Table(name = "structure")
//public class Structure {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id", nullable = false)
//    private Integer id;
//
//    @Enumerated(EnumType.STRING)
//    @NotNull
//    @Column(name = "libelle", nullable = false)
//    private StructureType libelle;
//
//    public void setLibelle(String libelle) {
//        this.libelle = StructureType.valueOf(libelle);
//    }
//}

public enum Structure {
    CENTRALE, REGIONALE
}