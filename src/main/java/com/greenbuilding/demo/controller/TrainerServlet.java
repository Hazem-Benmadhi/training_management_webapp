package com.greenbuilding.demo.controller;

import com.greenbuilding.demo.dao.EmployerDAO;
import com.greenbuilding.demo.dao.TrainerDAO;
import com.greenbuilding.demo.entity.Trainer;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

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
        List<Trainer> trainers = trainerDAO.findAll();
        request.setAttribute("trainers", trainers);
        System.out.println("list trainers" + trainers);
        request.getRequestDispatcher("trainers.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        request.setAttribute("employers", employerDAO.findAll());
        request.getRequestDispatcher("addTrainer.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idT = Integer.parseInt(request.getParameter("id"));
        request.setAttribute("trainer", trainerDAO.findById(idT));
//        request.setAttribute("employers", employerDAO.findAll());
        request.getRequestDispatcher("addTrainer.jsp").forward(request, response);
    }

    private void addEditTrainer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Trainer trainer;
        String idTrainer = request.getParameter("id");

        if (idTrainer != null && !idTrainer.isEmpty()) {
            int id = Integer.parseInt(idTrainer);
            trainer = trainerDAO.findById(id);
        } else {
            trainer = new Trainer();
        }

        trainer.setFirstName(request.getParameter("firstName"));
        trainer.setLastName(request.getParameter("lastName"));
        trainer.setEmail(request.getParameter("email"));
        trainer.setTel(request.getParameter("tel"));

        trainerDAO.saveOrUpdate(trainer);
        response.sendRedirect("trainers?action=list");
    }

    private void deleteTrainer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        trainerDAO.delete(id);
        response.sendRedirect("trainers?action=list");
    }
}