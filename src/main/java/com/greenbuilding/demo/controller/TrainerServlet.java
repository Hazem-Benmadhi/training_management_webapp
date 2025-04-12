package com.greenbuilding.demo.controller;

import com.greenbuilding.demo.dao.EmployerDAO;
import com.greenbuilding.demo.dao.TrainerDAO;
import com.greenbuilding.demo.entity.Employer;
import com.greenbuilding.demo.entity.Trainer;
import com.greenbuilding.demo.entity.TrainerType;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/trainers")
public class TrainerServlet extends HttpServlet {

    private TrainerDAO trainerDAO;
    private EmployerDAO employerDAO;

    @Override
    public void init() {
        trainerDAO = new TrainerDAO();
        employerDAO = new EmployerDAO();
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
                    addEditTrainer(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteTrainer(request, response);
                    break;
                default:
                    listTrainers(request, response);
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    private void listTrainers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("trainers", trainerDAO.findAll());
        request.setAttribute("pageContent", "trainer/list.jsp");
        request.setAttribute("pageTitle", "Trainer Management");
        request.getRequestDispatcher("layout.jsp").forward(request, response);

    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("employers", employerDAO.findAll());
        request.setAttribute("trainerType", TrainerType.values());
        request.getRequestDispatcher("trainer/form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idT = Integer.parseInt(request.getParameter("id"));
        request.setAttribute("trainer", trainerDAO.findById(idT));
        request.setAttribute("employers", employerDAO.findAll());
        request.setAttribute("trainerType", TrainerType.values());
        request.getRequestDispatcher("trainer/form.jsp").forward(request, response);
    }

    private void addEditTrainer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = request.getParameter("id") != null && !request.getParameter("id").isEmpty()
                ? Integer.parseInt(request.getParameter("id")) : 0;

        Trainer trainer = (id != 0) ? trainerDAO.findById(id) : new Trainer();

        trainer.setFirstName(request.getParameter("firstName"));
        trainer.setLastName(request.getParameter("lastName"));
        trainer.setEmail(request.getParameter("email"));
        trainer.setTel(request.getParameter("tel"));

        String typeParam = request.getParameter("type");
        try {
            trainer.setType(typeParam);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("trainer", trainer);
            request.getRequestDispatcher("trainer/form.jsp").forward(request, response);
            return;
        }

        // Manage employer uniquely if type = EXTERNE
        if ("EXTERNE".equalsIgnoreCase(typeParam)) {
            String employerIdParam = request.getParameter("employerId");
            if (employerIdParam != null && !employerIdParam.isEmpty()) {
                try {
                    Employer employer = employerDAO.findById(Integer.parseInt(employerIdParam));
                    if (employer != null) {
                        trainer.setEmployer(employer);
                    } else {
                        request.setAttribute("error", "Employer not found.");
                        request.setAttribute("trainer", trainer);
                        request.getRequestDispatcher("trainer/form.jsp").forward(request, response);
                        return;
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "ID employer invalid.");
                    request.setAttribute("trainer", trainer);
                    request.getRequestDispatcher("trainer/form.jsp").forward(request, response);
                    return;
                }
            } else {
                request.setAttribute("error", "Employer required for EXTERNAL trainer.");
                request.setAttribute("trainer", trainer);
                request.getRequestDispatcher("trainer/form.jsp").forward(request, response);
                return;
            }
        } else {
            // If INTERNE, employer no more keep it
            trainer.setEmployer(null);
        }

        trainerDAO.saveOrUpdate(trainer);
        response.sendRedirect("trainers?action=list");
    }

    private void deleteTrainer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        trainerDAO.delete(id);
        response.sendRedirect("trainers?action=list");
    }
}