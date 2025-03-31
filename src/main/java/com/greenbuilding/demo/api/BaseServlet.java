package com.greenbuilding.demo.api;

import jakarta.servlet.http.HttpServlet;

public abstract class BaseServlet<T> extends HttpServlet {

    protected BaseDAO<T, Integer> baseDAO;
    public BaseServlet(BaseDAO<T, Integer> baseDAO) {
        this.baseDAO = baseDAO;
    }
}


