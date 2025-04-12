package com.greenbuilding.demo.dao;

import com.greenbuilding.demo.api.BaseDAO;
import com.greenbuilding.demo.entity.Domain;
import com.greenbuilding.demo.entity.Employer;

public class EmployerDAO extends BaseDAO<Employer, Integer> {

    public EmployerDAO() {
        super(Employer.class);
    }
}
