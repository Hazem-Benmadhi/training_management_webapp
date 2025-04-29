package com.greenbuilding.demo.controller;


import com.greenbuilding.demo.dao.FormationDAO;
import com.greenbuilding.demo.dao.RapportDAO;
import com.greenbuilding.demo.entity.AppUser;
import com.greenbuilding.demo.entity.Formation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.IOException;
import java.util.List;

@WebServlet("/exportReport")
public class RapportServlet extends HttpServlet {
    private final FormationDAO formationDAO = new FormationDAO();
    private final RapportDAO rapportService = new RapportDAO();

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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Formation> formations = formationDAO.findAll();
        ByteArrayOutputStream excelStream = rapportService.generateFormationReport(formations);

        resp.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        resp.setHeader("Content-Disposition", "attachment; filename=formation-report.xlsx");
        resp.getOutputStream().write(excelStream.toByteArray());
    }
}
