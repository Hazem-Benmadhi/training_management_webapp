package com.greenbuilding.demo.dao;

import com.greenbuilding.demo.api.BaseDAO;
import com.greenbuilding.demo.entity.DefaultProfiles;
import com.greenbuilding.demo.entity.Profil;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;


public class ProfileDAO extends BaseDAO<Profil, Integer> {
    public ProfileDAO() {
        super(Profil.class);
        initializeDefaultProfiles();
    }

    // Initialize default profiles if they don't exist
    private void initializeDefaultProfiles() {
        for (DefaultProfiles defaultProfile : DefaultProfiles.values()) {
            if (!profileExists(defaultProfile.name())) {
                Profil profile = new Profil();
                profile.setLibelle(defaultProfile.name());
                createProfile(profile);
            }
        }
    }

    public Profil findByLibelle(String libelle) {
        TypedQuery<Profil> query = entityManager.createQuery("SELECT p FROM Profil p WHERE p.libelle = :libelle", Profil.class);
        query.setParameter("libelle", libelle);
        try {
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    // Check if profile exists
    public boolean profileExists(String libelle) {
        Query query = entityManager.createQuery(
                "SELECT COUNT(p) FROM Profil p WHERE p.libelle = :libelle"
        );
        query.setParameter("libelle", libelle);
        return (Long) query.getSingleResult() > 0;
    }

    // Create a new profile
    public Profil createProfile(Profil profile) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(profile);
            entityManager.getTransaction().commit();
            return profile;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        }
    }

    // Update profile
    public Profil updateProfile(int id, String newLibelle) {
        Profil profile = entityManager.find(Profil.class, id);
        if (profile == null) {
            throw new IllegalArgumentException("Profile not found with ID: " + id);
        }
        if (newLibelle == null || newLibelle.trim().isEmpty()) {
            throw new IllegalArgumentException("Profile name cannot be empty");
        }
        profile.setLibelle(newLibelle);
        return entityManager.merge(profile);
    }
}
