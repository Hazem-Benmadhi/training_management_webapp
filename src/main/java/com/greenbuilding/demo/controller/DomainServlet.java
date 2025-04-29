package com.greenbuilding.demo.controller;

import com.greenbuilding.demo.dao.DomainDAO;
import com.greenbuilding.demo.entity.AppUser;
import com.greenbuilding.demo.entity.Domain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;


@WebServlet("/domains")
public class DomainServlet extends HttpServlet {
    private DomainDAO domainDAO;

    @Override
    public void init(){
        domainDAO = new DomainDAO();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        AppUser user = (AppUser) session.getAttribute("user");
        boolean isAdmin = "admin".equals(user.getRole().toString());

        if (!isAdmin) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied: Admin role required");
            return;
        }
        super.service(request, response);
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
        request.setAttribute("pageContent", "domain/list.jsp");
        request.setAttribute("pageTitle", "Domain Management");
        request.getRequestDispatcher("layout.jsp").forward(request, response);
    }

    public void addDomain(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String libelle = request.getParameter("libelle");
        Domain domain = new Domain();
        domain.setLibelle(libelle.trim());
        domainDAO.save(domain);
        response.sendRedirect("domains?action=list");
    }

    public void deleteDomain(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            domainDAO.delete(id);
            response.sendRedirect("domains?action=list");
        } catch(Exception e) {
            request.getSession().setAttribute("errorMessage", e.getMessage() + ": you have to remove the assigned training first" );
            response.sendRedirect("domains?action=list");
        }
    }

}
