package com.greenbuilding.demo.dao;

import com.greenbuilding.demo.api.BaseDAO;
import com.greenbuilding.demo.entity.Profil;
import jakarta.persistence.TypedQuery;

public class ProfilDAO extends BaseDAO<Profil, Integer> {
    public ProfilDAO() {
        super(Profil.class);
    }
    public Profil findByLibelle(String libelle) {
        TypedQuery<Profil> query = entityManager.createQuery("SELECT p FROM Profil p WHERE p.libelle = :libelle", Profil.class);
        query.setParameter("libelle", libelle);

        try {
            return query.getSingleResult();  // Will return the Profil if found, or throw an exception if not found
        } catch (Exception e) {
            return null;  // Return null if no Profil is found with that libelle
        }
    }
}
