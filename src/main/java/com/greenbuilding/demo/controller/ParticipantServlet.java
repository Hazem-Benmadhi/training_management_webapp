package com.greenbuilding.demo.controller;

import com.greenbuilding.demo.dao.FormationDAO;
import com.greenbuilding.demo.dao.ParticipantDAO;
import com.greenbuilding.demo.dao.ProfilDAO;
import com.greenbuilding.demo.dao.RegistrationDAO;
import com.greenbuilding.demo.entity.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;


@WebServlet("/participants")
public class ParticipantServlet extends HttpServlet {

    private ParticipantDAO participantDAO;
    private RegistrationDAO registrationDAO;
    private FormationDAO formationDAO;
    private ProfilDAO profilDAO;

    @Override
    public void init(){
        participantDAO = new ParticipantDAO();
        registrationDAO = new RegistrationDAO();
        formationDAO = new FormationDAO();
        profilDAO = new ProfilDAO();
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
                case "details":
                    showParticipantDetails(request, response);
                    break;
//                case "register":
//                    registerToFormation(request, response);
//                    break;
                case "unregister":
                    unregisterFromFormation(request, response);
                    break;
                case "confirmRegister":
                    confirmRegister(request, response);
                    break;
//                case "new":
//                    showNewForm(request, response);
//                    break;
                case "add":
                    addEditParticipant(request, response);
                    break;
                case "edit":
                    if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                        getParticipantJson(request, response);
                    } else {
                        // Fallback for non-AJAX requests
                        showEditForm(request, response);
                    }
                    break;
                case "delete":
                    deleteParticipant(request, response);
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
        List<Formation> formations = formationDAO.findAll();
        request.setAttribute("defaultProfiles", DefaultProfiles.values());
        request.setAttribute("participants", participants);
        request.setAttribute("formations", formations);
        request.setAttribute("pageContent", "participant/list.jsp");
        request.setAttribute("pageTitle", "Participant Management");
        request.getRequestDispatcher("layout.jsp").forward(request, response);
    }

//    public void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        request.setAttribute("profils", profilDAO.findAll());
//        request.setAttribute("defaultProfiles", DefaultProfiles.values());
//        request.getRequestDispatcher("participant/form.jsp").forward(request,response);
//    }

    public void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        request.setAttribute("participant", participantDAO.findById(id));
        request.setAttribute("profils", profilDAO.findAll());
        request.getRequestDispatcher("participant/form.jsp").forward(request,response);
    }

    private void showParticipantDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Participant participants = participantDAO.findById(Integer.parseInt(request.getParameter("id")));
        request.setAttribute("participants", participants);
        request.getRequestDispatcher("participant/details.jsp").forward(request, response);
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

        // Set structure from enum
        String structureStr = request.getParameter("structure");
        if (structureStr != null && !structureStr.isEmpty()) {
            participant.setStructure(Structure.valueOf(structureStr));
        }

        String profilLibelle = request.getParameter("profilLibelle");
        String customProfil = request.getParameter("customProfil");

        String finalLibelle = profilLibelle.equals("OTHER") ? customProfil : profilLibelle;

        // Check if profil exists already
        Profil profil = profilDAO.findByLibelle(finalLibelle);

        // If not, create it
        if (profil == null) {
            profil = new Profil();
            profil.setLibelle(finalLibelle);
            profilDAO.saveOrUpdate(profil);
        }

        participant.setProfil(profil);
        participantDAO.saveOrUpdate(participant);
        response.sendRedirect("participants?action=list");
    }

    // display register form
//    public void registerToFormation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        request.setAttribute("participantId", Integer.parseInt(request.getParameter("id")));
//        request.setAttribute("formations", registrationDAO.getAvailableFormations());   // Retrieve available formations
//        request.getRequestDispatcher("register.jsp").forward(request, response);
//    }

    private void unregisterFromFormation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int participantId = Integer.parseInt(request.getParameter("participantId"));
        int formationId = Integer.parseInt(request.getParameter("formationId"));

        registrationDAO.unregisterParticipant(participantId, formationId);
        response.sendRedirect("participants?action=details");
    }

//    private void confirmRegister(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        try {
//            int participantId = Integer.parseInt(request.getParameter("participantId"));
//            int formationId = Integer.parseInt(request.getParameter("formationId"));
//
//            registrationDAO.registerParticipant(participantId, formationId);
//
//            // For AJAX requests, you might want to return JSON
//            if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
//                response.setContentType("application/json");
//                response.getWriter().write("{\"status\":\"success\"}");
//            } else {
//                response.sendRedirect("participants?action=list");
//            }
//        } catch (Exception e) {
//            if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
//                response.setContentType("application/json");
//                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//                response.getWriter().write("{\"status\":\"error\", \"message\":\"" + e.getMessage() + "\"}");
//            } else {
//                throw new ServletException(e);
//            }
//        }
//    }

    private void confirmRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int participantId = Integer.parseInt(request.getParameter("participantId"));
            int formationId = Integer.parseInt(request.getParameter("formationId"));

            registrationDAO.registerParticipant(participantId, formationId);

            // Check if it's an AJAX request
            if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"success\": true, \"message\": \"Registration successful\"}");
                return;
            }

            // Regular request handling
            response.sendRedirect("participants?action=list");

        } catch (Exception e) {
            // AJAX error response
            if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
                return;
            }

            // Regular error handling
            throw new ServletException(e);
        }
    }

    public void deleteParticipant(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        participantDAO.delete(id);
        response.sendRedirect("participants?action=list");
    }


    private void getParticipantJson(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Participant participant = participantDAO.findById(id);

        // Convert participant to JSON
        String json = String.format(
                "{\"id\":%d,\"firstName\":\"%s\",\"lastName\":\"%s\",\"email\":\"%s\"," +
                        "\"tel\":\"%s\",\"structure\":\"%s\",\"profil\":{\"libelle\":\"%s\"}}",
                participant.getId(),
                participant.getFirstName(),
                participant.getLastName(),
                participant.getEmail(),
                participant.getTel(),
                participant.getStructure().name(),
                participant.getProfil().getLibelle()
        );

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

}
