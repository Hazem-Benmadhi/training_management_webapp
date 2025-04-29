package com.greenbuilding.demo.controller;

import com.google.gson.GsonBuilder;
import com.greenbuilding.demo.dao.ParticipantDAO;
import com.greenbuilding.demo.dao.ProfileDAO;
import com.greenbuilding.demo.entity.AppUser;
import com.greenbuilding.demo.entity.Profil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpSession;

@WebServlet("/profiles")
public class ProfileServlet extends HttpServlet {
    private ProfileDAO profileDAO;
    private ParticipantDAO participantDAO;
    private Gson gson;

    @Override
    public void init() {
        profileDAO = new ProfileDAO();
        participantDAO = new ParticipantDAO();
        gson = new GsonBuilder().create();
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("profiles", profileDAO.findAll());
            req.setAttribute("pageContent", "profile/list.jsp");
            req.getRequestDispatcher("layout.jsp").forward(req, resp);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try {
            String libelle = req.getParameter("libelle");
            if (libelle == null || libelle.trim().isEmpty()) {
                throw new IllegalArgumentException("Profile name cannot be empty");
            }
            if (profileDAO.profileExists(libelle.trim().toLowerCase())) {
                throw new IllegalArgumentException("Profile already exists");
            }
            Profil newProfile = new Profil();
            newProfile.setLibelle(libelle.trim().toUpperCase());
            profileDAO.createProfile(newProfile);

            out.print(gson.toJson(Map.of(
                    "success", true,
                    "message", "Profile created successfully"
            )));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(gson.toJson(Map.of(
                    "success", false,
                    "error", e.getMessage()
            )));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try {
            int id = Integer.parseInt(req.getParameter("id"));
            participantDAO.findAll().forEach(participant -> {
                if (participant.getProfil().getId() == id) {
                    throw new IllegalStateException("Cannot delete profile - there are participants assigned to it");
                }
            });

            profileDAO.delete(id);
            out.print(gson.toJson(Map.of(
                    "success", true,
                    "message", "Profile deleted successfully"
            )));
        } catch (IllegalStateException e) {
            // Specific error for participants existing
            resp.setStatus(HttpServletResponse.SC_CONFLICT); // 409 Conflict
            out.print(gson.toJson(Map.of(
                    "success", false,
                    "error", e.getMessage()
            )));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(gson.toJson(Map.of(
                    "success", false,
                    "error", e.getMessage()
            )));
        }
    }
}