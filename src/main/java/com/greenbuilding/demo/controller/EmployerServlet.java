package com.greenbuilding.demo.controller;

import com.greenbuilding.demo.dao.EmployerDAO;
import com.greenbuilding.demo.entity.AppUser;
import com.greenbuilding.demo.entity.Employer;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/employers")
public class EmployerServlet extends HttpServlet {
    private EmployerDAO employerDAO;

    @Override
    public void init() {
        employerDAO = new EmployerDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "list" : request.getParameter("action");
        try {
            switch (action) {
                case "add":
                    addEmployer(request, response);
                    break;
                case "delete":
                    deleteEmployer(request, response);
                    break;
                case "list":
                default:
                    listEmployers(request, response);
                    break;
            }
        } catch (Exception ex) {
            throw new ServletException("Error processing action in EmployerServlet", ex);
        }
    }

    private void listEmployers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("employers", employerDAO.findAll());
        request.setAttribute("pageContent", "employer/list.jsp");
        request.setAttribute("pageTitle", "Employer Management");
        request.getRequestDispatcher("layout.jsp").forward(request, response);
    }


    private void addEmployer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Employer employer = new Employer();
        employer.setNameEmployer(request.getParameter("employerName"));

        employerDAO.save(employer);
        response.sendRedirect("employers?action=list");
    }

    private void deleteEmployer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        employerDAO.delete(id);
        response.sendRedirect("employers?action=list");
    }
}
