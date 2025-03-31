package com.greenbuilding.demo.controller;

import com.greenbuilding.demo.dao.DomainDAO;
import com.greenbuilding.demo.entity.Domain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

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
                    addEditDomain(request, response);

                    break;
                case "edit":
                    showEditForm(request, response);

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
        RequestDispatcher dispatcher = request.getRequestDispatcher("domains.jsp");
        dispatcher.forward(request, response);
    }

    public void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("addDomain.jsp");
        dispatcher.forward(request,response);
    }

    public void addEditDomain(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Domain domain;
        String idDomain = request.getParameter("id");
        if(idDomain!=null && !idDomain.isEmpty()){
            int id = Integer.parseInt(idDomain);
            domain = domainDAO.findById(id);
        }
        else {
            domain= new Domain();
        }
        domain.setLibelle(request.getParameter("libelle"));

        domainDAO.saveOrUpdate(domain);
        response.sendRedirect("domains?action=list");
    }

    public void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idD = Integer.parseInt(request.getParameter("id"));
        Domain domain = domainDAO.findById(idD);
        request.setAttribute("domain",domain);
        request.getRequestDispatcher("addDomain.jsp").forward(request,response);
    }

    public void deleteDomain(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id= Integer.parseInt(request.getParameter("id"));
        domainDAO.delete(id);
        response.sendRedirect("Domains?action=list");
    }

}
