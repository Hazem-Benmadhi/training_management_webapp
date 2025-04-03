package com.greenbuilding.demo.controller;

import com.greenbuilding.demo.dao.ParticipantDAO;
import com.greenbuilding.demo.dao.RegistrationDAO;
import com.greenbuilding.demo.entity.Formation;
import com.greenbuilding.demo.entity.Participant;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Objects;


@WebServlet("/participants")
public class ParticipantServlet extends HttpServlet {
    private ParticipantDAO participantDAO;
    private RegistrationDAO registrationDAO;

    @Override
    public void init(){
        participantDAO = new ParticipantDAO();
        registrationDAO = new RegistrationDAO();
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }


    @Override
    protected void doGet(@org.jetbrains.annotations.NotNull HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "list" : request.getParameter("action");
        try {
            switch (action) {
                case "register":
                    registerToFormation(request, response);
                    break;
                case "confirmRegister":
                    confirmRegister(request, response);
                    break;
                case "new":
                    showNewForm(request, response);
                    break;
                case "add":
                    addEditParticipant(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteFormation(request, response);
                    break;
                default:
                    listParticipants(request, response);
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    private void listParticipants(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Participant> participants = participantDAO.findAll();
        for (Participant participant : participants) {
            boolean isRegistered = registrationDAO.isRegistered(participant.getId());
            request.setAttribute("participant_" + participant.getId(), isRegistered);
        }
        request.setAttribute("participants", participants);
        request.getRequestDispatcher("participant.jsp").forward(request, response);
    }

    public void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("addParticipant.jsp").forward(request,response);
    }

    public void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        request.setAttribute("participant", participantDAO.findById(id));
        request.getRequestDispatcher("addParticipant.jsp").forward(request,response);
    }

    public void addEditParticipant(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Participant participant;
        String idP = request.getParameter("id");
        if(idP!=null && !idP.isEmpty()){
            int id = Integer.parseInt(idP);
            participant = participantDAO.findById(id);
        }
        else {
            participant = new Participant();
        }
        participant.setFirstName(request.getParameter("firstName"));
        participant.setLastName(request.getParameter("lastName"));
        participant.setEmail(request.getParameter("email"));
        participant.setTel(request.getParameter("tel"));
        
        System.out.println("Saving participant: " + participant);

        participantDAO.saveOrUpdate(participant);
        response.sendRedirect("participants?action=list");
    }

    public void registerToFormation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int participantId = Integer.parseInt(request.getParameter("id"));

        // Retrieve available formations
        List<Formation> formations = registrationDAO.getAvailableFormations();
        request.setAttribute("participantId", participantId);
        request.setAttribute("formations", formations);
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    private void confirmRegister(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int participantId = Integer.parseInt(request.getParameter("participantId"));
        int formationId = Integer.parseInt(request.getParameter("formationId"));

        boolean registered = registrationDAO.registerParticipant(participantId, formationId);

        if (registered) {
            request.setAttribute("message", "Participant registered successfully!");
        } else {
            request.setAttribute("message", "Failed to register. The participant may already be registered.");
        }

        listParticipants(request, response);
    }

    public void deleteFormation(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        participantDAO.delete(id);
        response.sendRedirect("participants?action=list");
    }
}
