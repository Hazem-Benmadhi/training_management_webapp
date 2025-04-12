package com.greenbuilding.demo.controller;

import com.greenbuilding.demo.dao.EmployerDAO;
import com.greenbuilding.demo.entity.Employer;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/employers")
public class EmployerServlet extends HttpServlet {
    private EmployerDAO employerDAO;

    @Override
    public void init() {
        employerDAO = new EmployerDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "list" : request.getParameter("action");

        try {
            switch (action) {
                case "new":
                    showNewForm(request, response);
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        addOrUpdateEmployer(request, response);
    }

    private void listEmployers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Employer> employers = employerDAO.findAll();
        request.setAttribute("employers", employers);
        request.getRequestDispatcher("employers.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("employer", new Employer());
        request.getRequestDispatcher("addEmployer.jsp").forward(request, response);
    }

    private void addOrUpdateEmployer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idParam = request.getParameter("id");
        Employer employer;

        if (idParam != null && !idParam.isEmpty()) {
            int id = Integer.parseInt(idParam);
            employer = employerDAO.findById(id);
        } else {
            employer = new Employer();
        }

        employer.setNameEmployer(request.getParameter("nameEmployer"));

        employerDAO.saveOrUpdate(employer);
        response.sendRedirect("employers?action=list");
    }

    private void deleteEmployer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        employerDAO.delete(id);
        response.sendRedirect("employers?action=list");
    }
}
