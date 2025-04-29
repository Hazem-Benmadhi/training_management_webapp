package com.greenbuilding.demo.controller;

import com.greenbuilding.demo.dao.UserDAO;
import com.greenbuilding.demo.entity.AppUser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;


@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect("dashboard");
            return;
        }
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        try{
            Optional<AppUser> user = userDAO.findByLogin(login);
            if (user.isPresent() && verifyPassword(password, user.get().getPassword())) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user.get());
                session.setAttribute("role", user.get().getRole());

                switch (user.get().getRole().toString()) {
                    case "admin", "responsible":
                        response.sendRedirect("dashboard");
                        break;
                    case "simple":
                        response.sendRedirect("trainers");
                        break;
                }
            } else {
                request.setAttribute("error", "Invalid credentials");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        }catch (Exception e) {
            request.setAttribute("error", "Login failed: " + e.getMessage());
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    private boolean verifyPassword(String input, String stored) {
        return input.equals(stored);
    }

}