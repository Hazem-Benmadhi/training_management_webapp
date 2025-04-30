package com.greenbuilding.demo.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.greenbuilding.demo.entity.AppUser;
import com.greenbuilding.demo.service.DashboardService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.*;

@WebServlet("/dashboard/*")
public class DashboardServlet extends HttpServlet {
    private static final String DEFAULT_PERIOD = "all";
    private EntityManagerFactory emf;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void init() throws ServletException {
        emf = Persistence.createEntityManagerFactory("dbPU");
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
        boolean isResponsible = "responsible".equals(user.getRole().toString());

        if (!isAdmin && !isResponsible) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied: Admin or Responsible role required");
            return;
        }
        super.service(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            DashboardService dashboardService = new DashboardService(em);

            // Handle AJAX request for enrollment trend data
            if ("enrollmentTrend".equals(request.getParameter("action"))){
                handleAjaxRequest(request, response, dashboardService);
                return;
            }

            // handel all Training pagination modal
            if ("allTrainings".equals(request.getParameter("action"))) {
                handleAllTrainings(request, response, dashboardService);
                return;
            }

            // Load full dashboard data
            loadDashboardData(request, dashboardService);
            request.getRequestDispatcher("pages/dashboard.jsp").forward(request, response);
        } catch (Exception e) {
//            handleError(request, response, e);
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    private boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

    private void handleAllTrainings(HttpServletRequest request, HttpServletResponse response, DashboardService dashboardService) throws ServletException, IOException {
        try {
            int page = 1;
            String pageParam = request.getParameter("page");
            if (pageParam != null && pageParam.matches("\\d+")) {
                page = Integer.parseInt(pageParam); // get the current page number
                if (page < 1) page = 1;
            }

            int pageSize = 10;
            int totalCount = dashboardService.getTotalTrainingCount();
            int totalPages = (int) Math.ceil((double) totalCount / pageSize);

            if (page > totalPages && totalPages > 0) page = totalPages;
            List<Map<String, Object>> trainings = dashboardService.getAllTrainings(page, pageSize);

            request.setAttribute("allTrainings", trainings);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);

            if (isAjax(request)) {
                request.getRequestDispatcher("/components/topTrainingPagination.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/pages/dashboard.jsp").forward(request, response);
            }

        } catch (Exception e) {
            request.setAttribute("error", "Failed to load trainings: " + e.getMessage());
        }
    }

    // still not working perfectly
    private void handleAjaxRequest(HttpServletRequest request, HttpServletResponse response, DashboardService dashboardService) throws IOException {
        try {
            String period = request.getParameter("period");
            System.out.println("Received AJAX request for period: " + period);

            if (period == null || !Arrays.asList("all", "year", "6months", "month").contains(period)) {
                period = DEFAULT_PERIOD;
            }

            Map<String, Object> trendData = dashboardService.getEnrollmentTrendData(period);

            // Convert to JSON with pretty printing for debugging
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonResponse = gson.toJson(trendData);
            System.out.println("Sending response: " + jsonResponse);

            response.getWriter().write(jsonResponse);

        } catch (Exception e) {
            System.err.println("Error handling enrollment trend request: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(Map.of(
                    "error", "Failed to load trend data",
                    "message", e.getMessage()
            )));
        }
    }

    private void loadDashboardData(HttpServletRequest request, DashboardService dashboardService) {
        // Get statistics card data
        request.setAttribute("statistics", dashboardService.getStatisticsCardData());

        // Get enrollment trend data with default period
        request.setAttribute("enrollmentTrend", dashboardService.getEnrollmentTrendData(DEFAULT_PERIOD));

        // Get domain distribution data
        request.setAttribute("domainDistribution", dashboardService.getDomainDistributionData());

        // Get top trainings data
        request.setAttribute("topTrainings", dashboardService.getTopTrainings(5));

        // Get participant profile distribution
        request.setAttribute("profileDistribution", dashboardService.getParticipantProfileDistribution());

        // Get budget overview data
        request.setAttribute("budgetData", dashboardService.getBudgetOverviewData());

        // Set JSP components
        setJspComponents(request);
    }

    private void setJspComponents(HttpServletRequest request) {
        request.setAttribute("statsCards", "/components/statsCards.jsp");
        request.setAttribute("enrollTrends", "/components/enrollmentTrends.jsp");
        request.setAttribute("trainingCom", "/components/topTrainings.jsp");
        request.setAttribute("participantProfile", "/components/profileChart.jsp");
        request.setAttribute("budgetOver", "/components/budgetOverview.jsp");
    }

    @Override
    public void destroy() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

}