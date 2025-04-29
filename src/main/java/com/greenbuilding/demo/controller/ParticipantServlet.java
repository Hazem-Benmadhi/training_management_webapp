package com.greenbuilding.demo.controller;

import com.greenbuilding.demo.dao.FormationDAO;
import com.greenbuilding.demo.dao.ParticipantDAO;
import com.greenbuilding.demo.dao.ProfileDAO;
import com.greenbuilding.demo.dao.RegistrationDAO;
import com.greenbuilding.demo.entity.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/participants")
public class ParticipantServlet extends HttpServlet {

    private ParticipantDAO participantDAO;
    private RegistrationDAO registrationDAO;
    private FormationDAO formationDAO;
    private ProfileDAO profileDAO;

    @Override
    public void init(){
        participantDAO = new ParticipantDAO();
        registrationDAO = new RegistrationDAO();
        formationDAO = new FormationDAO();
        profileDAO = new ProfileDAO();
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(@org.jetbrains.annotations.NotNull HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

        String action = request.getParameter("action") == null ? "list" : request.getParameter("action");
        try {
            switch (action) {
                case "details":
                    showParticipantDetails(request, response);
                    break;
                case "unregister":
                    unregisterFromFormation(request, response);
                    break;
                case "confirmRegister":
                    confirmRegister(request, response);
                    break;
                case "add":
                    addEditParticipant(request, response);
                    break;
                case "edit":
                    getParticipantJson(request, response);
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

    private void listParticipants(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<String> profiles = new ArrayList<>();
        for (Profil prof : profileDAO.findAll()){
            profiles.add(prof.getLibelle());
        }

        request.setAttribute("formationCounts", participantDAO.getFormationCounts());
        request.setAttribute("existingProfiles", profiles);
        request.setAttribute("participants", participantDAO.findAll());
        request.setAttribute("formations", formationDAO.findAll());
        request.setAttribute("pageContent", "participant/list.jsp");
        request.setAttribute("pageTitle", "Participant Management");
        request.getRequestDispatcher("layout.jsp").forward(request, response);
    }

private void showParticipantDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
        int participantId = Integer.parseInt(request.getParameter("id"));

        // Get participant data
        Participant participant = participantDAO.findById(participantId);
        if (participant == null) {
            request.getSession().setAttribute("errorMessage", "Participant not found.");
            response.sendRedirect("participants");
            return;
        }

        // Get formations the participant is enrolled in
        List<Formation> enrolledFormations = formationDAO.getFormationsByParticipantId(participantId);

        request.setAttribute("participant", participant);
        request.setAttribute("enrolledFormations", enrolledFormations);

        // Forward to details page
        request.setAttribute("pageContent", "/participant/details.jsp");
        request.getRequestDispatcher("layout.jsp").forward(request, response);
    } catch (NumberFormatException e) {
        request.getSession().setAttribute("errorMessage", "Invalid participant ID.");
        response.sendRedirect("participants");
    } catch (Exception e) {
        request.getSession().setAttribute("errorMessage", "Error loading participant details: " + e.getMessage());
        response.sendRedirect("participants");
    }
}


    public void addEditParticipant(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = request.getParameter("id") != null && !request.getParameter("id").isEmpty()
                ? Integer.parseInt(request.getParameter("id")) : 0;

        Participant participant = (id != 0) ? participantDAO.findById(id) : new Participant();

        System.out.println("*****************************");
        System.out.println("participant11111" + participant);

        participant.setFirstName(request.getParameter("firstName"));
        participant.setLastName(request.getParameter("lastName"));
        participant.setEmail(request.getParameter("email"));
        participant.setTel(request.getParameter("tel"));

        // Set structure from enum
        String struct = request.getParameter("structure");
        if (struct != null && !struct.isEmpty()) {
            participant.setStructure(Structure.valueOf(struct));
        }

        String profilLibelle = request.getParameter("profilLibelle");
        String customProfil = request.getParameter("customProfil");

        String finalLibelle = profilLibelle.equals("OTHER") ? customProfil : profilLibelle;

        // Check if profil exists already
        Profil profil = profileDAO.findByLibelle(finalLibelle);

        // If not, create it
        if (profil == null) {
            profil = new Profil();
            profil.setLibelle(finalLibelle);
            profileDAO.saveOrUpdate(profil);
        }

        participant.setProfil(profil);

        System.out.println("participant22222" + participant);


        participantDAO.saveOrUpdate(participant);
        response.sendRedirect("participants?action=list");
    }

    private void unregisterFromFormation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int participantId = Integer.parseInt(request.getParameter("participantId"));
            int formationId = Integer.parseInt(request.getParameter("formationId"));


            // Get participant and formation to verify they exist
            Participant participant = participantDAO.findById(participantId);
            Formation formation = formationDAO.findById(formationId);

            if (participant == null || formation == null) {
                request.getSession().setAttribute("errorMessage", "Participant or formation not found.");
                response.sendRedirect("participants");
                return;
            }

            // Remove the registration
            boolean success = registrationDAO.removeParticipantFromFormation(participant, formation);

            if (success) {
                request.getSession().setAttribute("successMessage",
                        "Successfully unregistered " + participant.getFirstName() + " " +
                                participant.getLastName() + " from " + formation.getTitle());
            } else {
                request.getSession().setAttribute("errorMessage", "Failed to unregister from formation.");
            }

            // Redirect back to participant details
            response.sendRedirect("participants?action=details&id=" + participantId);

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMessage", "Invalid participant or formation ID.");
            response.sendRedirect("participants");
        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "Error unregistering participant: " + e.getMessage());
            response.sendRedirect("participants");
        }
    }

    private void confirmRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int participantId = Integer.parseInt(request.getParameter("participantId"));
            int formationId = Integer.parseInt(request.getParameter("formationId"));

            if (!registrationDAO.isRegistered(formationId, participantId)) {
                registrationDAO.registerParticipant(participantId, formationId);
                request.getSession().setAttribute("successMessage", "Registration successful");
            } else {
                request.getSession().setAttribute("errorMessage", "Participant is already registered for this formation");
            }

            response.sendRedirect("participants?action=list");
            return;

        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "Registration failed: " + e.getMessage());
            response.sendRedirect("participants?action=list");
        }
    }


    public void deleteParticipant(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        participantDAO.delete(id);
        response.sendRedirect("participants?action=list");
    }


    private void getParticipantJson(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        System.out.println("participantId: " + id);

        Participant participant = participantDAO.findById(id);
        System.out.println("participant: " + participant);

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

        System.out.println("json format" + json);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

}
