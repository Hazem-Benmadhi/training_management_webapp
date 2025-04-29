package com.greenbuilding.demo.controller;

import com.greenbuilding.demo.dao.EmployerDAO;
import com.greenbuilding.demo.dao.TrainerDAO;
import com.greenbuilding.demo.entity.AppUser;
import com.greenbuilding.demo.entity.Employer;
import com.greenbuilding.demo.entity.Trainer;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

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
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        AppUser user = (AppUser) session.getAttribute("user");
        boolean isAdmin = "admin".equals(user.getRole().toString());
        boolean isSimple = "simple".equals(user.getRole().toString());

        if (!isAdmin && !isSimple) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied: Admin or Simple User role required");
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
                    addEditTrainer(request, response);
                    break;
                case "edit":
                    getTrainerJson(request, response);
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
        request.setAttribute("employers", employerDAO.findAll());
        request.setAttribute("pageContent", "trainer/list.jsp");
        request.setAttribute("pageTitle", "Trainer Management");
        request.getRequestDispatcher("layout.jsp").forward(request, response);

    }

    private void addEditTrainer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = request.getParameter("id") != null && !request.getParameter("id").isEmpty()
                ? Integer.parseInt(request.getParameter("id")) : 0;

        Trainer trainer = (id != 0) ? trainerDAO.findById(id) : new Trainer();

        trainer.setFirstName(request.getParameter("firstName"));
        trainer.setLastName(request.getParameter("lastName"));
        trainer.setEmail(request.getParameter("email"));
        trainer.setTel(request.getParameter("tel"));
        trainer.setType(request.getParameter("type"));

        // Manage employer uniquely if type = EXTERNE
        if ("EXTERNE".equalsIgnoreCase(String.valueOf(trainer.getType()))) {
            String employerId = request.getParameter("employerId");
            if (employerId != null && !employerId.isEmpty()) {
                Employer employer = employerDAO.findById(Integer.parseInt(employerId));
                trainer.setEmployer(employer);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Employer required for EXTERNAL trainer.");
                return;
            }
        } else {
            trainer.setEmployer(null);
        }

        trainerDAO.saveOrUpdate(trainer);
        response.sendRedirect("trainers?action=list");
    }

    private void deleteTrainer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            trainerDAO.delete(id);
            response.sendRedirect("trainers?action=list");
        } catch(Exception e) {
            request.getSession().setAttribute("errorMessage", e.getMessage() + ": you can't delete an assigned trainer to a training" );
            response.sendRedirect("trainers?action=list");
        }
    }


    private void getTrainerJson(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Trainer trainer = trainerDAO.findById(id);

            String json = String.format(
                    "{\"id\":%d,\"firstName\":\"%s\",\"lastName\":\"%s\",\"email\":\"%s\"," +
                            "\"tel\":\"%s\",\"type\":\"%s\",\"employer\":%s}",
                    trainer.getId(),
                    escapeJson(trainer.getFirstName()),
                    escapeJson(trainer.getLastName()),
                    escapeJson(trainer.getEmail()),
                    escapeJson(trainer.getTel()),
                    escapeJson(String.valueOf(trainer.getType())),
                    trainer.getEmployer() != null ?
                            String.format("{\"id\":%d,\"nameEmployer\":\"%s\"}",
                                    trainer.getEmployer().getId(),
                                    escapeJson(trainer.getEmployer().getNameEmployer())) :
                            "null"
            );

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading trainer data");
        }
    }

    private String escapeJson(String input) {
        if (input == null) return "";
        return input.replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}