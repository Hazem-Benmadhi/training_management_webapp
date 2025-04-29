package com.greenbuilding.demo.controller;

import com.google.gson.Gson;
import com.greenbuilding.demo.dao.AppUserDAO;
import com.greenbuilding.demo.entity.AppUser;
import com.greenbuilding.demo.entity.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet("/users")
public class UserServlet extends HttpServlet {
    private AppUserDAO appUserDAO;
    private Gson gson;


    @Override
    public void init() {
        appUserDAO = new AppUserDAO();
        gson = new Gson();
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
            req.setAttribute("users", appUserDAO.findAll());
            String xRequestedWith = req.getHeader("X-Requested-With");
            boolean isAjaxRequest = "XMLHttpRequest".equals(xRequestedWith);
            if (isAjaxRequest) {
                req.getRequestDispatcher("users/list.jsp").forward(req, resp);
            } else {
                req.setAttribute("pageContent", "users/list.jsp");
                req.getRequestDispatcher("layout.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("create".equals(action)) {
            createUser(req, resp);
        } else if ("update".equals(action)) {
            updateUser(req, resp);
        } else if ("delete".equals(action)) {
            deleteUser(req, resp);
        }
    }
    private void createUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        try {
            String username = req.getParameter("login");
            String password = req.getParameter("password");
            String roleStr = req.getParameter("role");

            if (username == null || username.trim().isEmpty() ||
                    password == null || password.trim().isEmpty() ||
                    roleStr == null) {
                throw new IllegalArgumentException("All fields are required");
            }
            Role role;
            try {
                role = Role.valueOf(roleStr);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid role specified");
            }
            AppUser newUser = appUserDAO.createUser(username, password, role);
            out.print(gson.toJson(Map.of(
                    "success", true,
                    "message", "User created successfully",
                    "userId", newUser.getId()
            )));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(gson.toJson(Map.of(
                    "success", false,
                    "message", "Failed to create user: " + e.getMessage()
            )));
        }
    }

    private void updateUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        try {
            String userIdStr = req.getParameter("userId");
            String newRoleStr = req.getParameter("role").toLowerCase();
            if (userIdStr == null || newRoleStr == null) {
                throw new IllegalArgumentException("User ID and role are required");
            }

            int userId = Integer.parseInt(userIdStr);
            Role oldRole = appUserDAO.findById(userId).getRole();

            Role newRole;
            try {
                newRole = Role.valueOf(newRoleStr);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid role specified");
            }

            int currentUserId = getCurrentUserId(req);
            if ((currentUserId == userId && newRole != Role.valueOf("admin")) || oldRole == Role.valueOf("admin")) {
                throw new IllegalStateException("You cannot remove your own or any admin privileges");
            }
            appUserDAO.updateUserRole(userId, newRole);
            out.print(gson.toJson(Map.of(
                    "success", true,
                    "message", "User role updated successfully",
                    "role", newRole.name().toUpperCase()
            )));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(gson.toJson(Map.of(
                    "success", false,
                    "message", "Failed to update user: " + e.getMessage()
            )));
        }
    }

    private void deleteUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        try {
            String userIdStr = req.getParameter("userId");
            if (userIdStr == null) {
                throw new IllegalArgumentException("User ID is required");
            }

            int userId = Integer.parseInt(userIdStr);
            Role userRole = appUserDAO.findById(userId).getRole();

            int currentUserId = getCurrentUserId(req);
            if (currentUserId == userId || userRole == Role.valueOf("admin")) {
                throw new IllegalStateException("You cannot delete your own or any admin privileges");
            }
            appUserDAO.delete(userId);
            out.print(gson.toJson(Map.of(
                    "success", true,
                    "message", "User deleted successfully"
            )));
        } catch (Exception e) {
            int status = e instanceof IllegalStateException ?
                    HttpServletResponse.SC_FORBIDDEN :
                    HttpServletResponse.SC_BAD_REQUEST;

            resp.setStatus(status);
            out.print(gson.toJson(Map.of(
                    "success", false,
                    "message", "Failed to delete user: " + e.getMessage()
            )));
        }
    }

    private int getCurrentUserId(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session != null ? ((AppUser) session.getAttribute("user")).getId() : null;
    }
}
