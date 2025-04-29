package com.greenbuilding.demo.dao;

import com.greenbuilding.demo.api.BaseDAO;
import com.greenbuilding.demo.entity.AppUser;

import java.util.Optional;

public class UserDAO extends BaseDAO <AppUser, Integer> {
    public UserDAO() {
        super(AppUser.class);
    }

    public Optional<AppUser> findByLogin(String login) {
        try {
            AppUser user = entityManager
                    .createQuery("SELECT u FROM AppUser u WHERE u.login = :login", AppUser.class)
                    .setParameter("login", login)
                    .getSingleResult();
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }


    public void save(AppUser user) {
        if (user.getId() == null) {
            entityManager.persist(user);
        } else {
            entityManager.merge(user);
        }
    }
}
