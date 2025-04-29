package com.greenbuilding.demo.dao;

import com.greenbuilding.demo.api.BaseDAO;
import com.greenbuilding.demo.entity.AppUser;
import com.greenbuilding.demo.entity.Role;

public class AppUserDAO extends BaseDAO<AppUser, Integer> {
    public AppUserDAO() {
        super(AppUser.class);
    }

    public AppUser createUser(String username, String password, Role role) {
        // Check if username already exists
        Long count = entityManager.createQuery(
                        "SELECT COUNT(u) FROM AppUser u WHERE u.login = :login", Long.class)
                .setParameter("login", username)
                .getSingleResult();

        if (count > 0) {
            throw new IllegalArgumentException("Username already exists");
        }

        AppUser user = new AppUser();
        user.setLogin(username);
        user.setPassword(password);
        user.setRole(role);

        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.getTransaction().commit();

        return user;
    }

    public void updateUserRole(int userId, Role newRole) {
        entityManager.getTransaction().begin();
        AppUser user = entityManager.find(AppUser.class, userId);
        if (user != null) {
            user.setRole(newRole);
            entityManager.merge(user);
        }
        entityManager.getTransaction().commit();
    }

}
