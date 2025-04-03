package com.greenbuilding.demo.controller;

import com.greenbuilding.demo.dao.DomainDAO;
import com.greenbuilding.demo.entity.Domain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/domains")
public class DomainServlet extends HttpServlet {

    private DomainDAO domainDAO;

    @Override
    public void init(){
        domainDAO = new DomainDAO();
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "list" : request.getParameter("action");
        try {
            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "add":
                    addDomain(request, response);
                    break;
                case "delete":
                    deleteDomain(request, response);
                    break;
                default:
                    listDomain(request, response);
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    private void listDomain(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("domains", domainDAO.findAll());
        request.getRequestDispatcher("domains.jsp").forward(request, response);
    }

    public void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("addDomain.jsp").forward(request,response);
    }

    public void addDomain(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Domain domain = new Domain();
        domain.setLibelle(request.getParameter("libelle"));

        domainDAO.save(domain);
        response.sendRedirect("domains?action=list");
    }

    public void deleteDomain(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        domainDAO.delete(id);
        response.sendRedirect("domains?action=list");
    }

}
