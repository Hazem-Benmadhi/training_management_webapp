package com.greenbuilding.demo.controller;

import com.greenbuilding.demo.dao.DomainDAO;
import com.greenbuilding.demo.dao.FormationDAO;
import com.greenbuilding.demo.dao.ParticipantDAO;
import com.greenbuilding.demo.dao.TrainerDAO;
import com.greenbuilding.demo.entity.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/formations")
public class FormationServlet extends HttpServlet {

    private FormationDAO formationDAO;
    private DomainDAO domainDAO;
    private TrainerDAO trainerDAO;
    private ParticipantDAO participantDAO;

    @Override
    public void init(){
        formationDAO = new FormationDAO();
        domainDAO = new DomainDAO();
        trainerDAO = new TrainerDAO();
        participantDAO = new ParticipantDAO();
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "list" : request.getParameter("action");
        try {
            switch (action) {
                case "add":
                    addEditFormation(request, response);
                    break;
                case "edit":
                    getFormationJson(request, response);
                    break;
                case "delete":
                    deleteFormation(request, response);
                    break;
                default:
                    listFormations(request, response);
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }

    }

    private void listFormations(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("formations", formationDAO.findAll());
        request.setAttribute("domains", domainDAO.findAll());
        request.setAttribute("trainers", trainerDAO.findAll());
        request.setAttribute("pageContent", "formation/list.jsp");
        request.setAttribute("pageTitle", "Formation Management");
        request.getRequestDispatcher("layout.jsp").forward(request, response);
    }

    public void addEditFormation(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = request.getParameter("id") != null && !request.getParameter("id").isEmpty()
                ? Integer.parseInt(request.getParameter("id")) : 0;

        Formation formation = (id != 0) ? formationDAO.findById(id) : new Formation();

        Domain domain = domainDAO.findById(Integer.parseInt(request.getParameter("idDomain")));
        Trainer trainer = trainerDAO.findById(Integer.parseInt(request.getParameter("idTrainer")));

        formation.setTitle(request.getParameter("title"));
        formation.setDate(LocalDate.parse(request.getParameter("date")));
        formation.setDuration(Integer.parseInt(request.getParameter("duration")));
        formation.setBudget(Double.parseDouble(request.getParameter("budget")));
        formation.setDomain(domain);
        formation.setTrainer(trainer);

        formationDAO.saveOrUpdate(formation);
        response.sendRedirect("formations?action=list");
    }

    public void deleteFormation(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try{
            int id= Integer.parseInt(request.getParameter("id"));
            Formation formation = formationDAO.findById(id);
            for (Participant participant : formation.getParticipants()) {
                participant.getFormations().remove(formation);
                participantDAO.saveOrUpdate(participant);
            }

            formationDAO.delete(id);
            response.sendRedirect("formations?action=list");
        } catch(Exception e) {
            request.getSession().setAttribute("errorMessage", "delete failed: " + e.getMessage());
            response.sendRedirect("formations?action=list");
        }
    }

    private void getFormationJson(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Formation formation = formationDAO.findById(id);

            if (formation == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\":\"Formation not found\"}");
                return;
            }

            String json = String.format(
                    "{\"id\":%d,\"title\":\"%s\",\"date\":\"%s\",\"duration\":%d," +
                            "\"budget\":%.2f,\"domain\":{\"id\":%d,\"libelle\":\"%s\"}," +
                            "\"trainer\":{\"id\":%d,\"firstName\":\"%s\",\"lastName\":\"%s\"}}",
                    formation.getId(),
                    escapeJson(formation.getTitle()),
                    formation.getDate(),
                    formation.getDuration(),
                    formation.getBudget(),
                    formation.getDomain().getId(),
                    escapeJson(formation.getDomain().getLibelle()),
                    formation.getTrainer().getId(),
                    escapeJson(formation.getTrainer().getFirstName()),
                    escapeJson(formation.getTrainer().getLastName())
            );

            response.getWriter().write(json);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Invalid formation ID\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"" + escapeJson(e.getMessage()) + "\"}");
        }
    }
    private String escapeJson(String input) {
        if (input == null) return "";
        return input.replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}
