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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

            String path = request.getPathInfo();

            // handle Calendar event
            if ("/api/calendarEvents".equals(path)) {
                handleCalendarEvents(request, response, dashboardService);
                return;
            }

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


    private void handleCalendarEvents(HttpServletRequest request, HttpServletResponse response, DashboardService dashboardService) throws IOException {
        int year, month;

        try {
            year = Integer.parseInt(request.getParameter("year"));
            month = Integer.parseInt(request.getParameter("month"));
        } catch (NumberFormatException e) {
            // Default to current year/month
            LocalDate now = LocalDate.now();
            year = now.getYear();
            month = now.getMonthValue();
        }

        List<Map<String, Object>> events = dashboardService.getTrainingCalendarData(year, month);

        // Convert to Webix format (id, text, start_date, end_date)
        List<Map<String, Object>> formattedEvents = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (Map<String, Object> event : events) {
            LocalDate date = (LocalDate) event.get("date");

            Map<String, Object> formatted = new HashMap<>();
            formatted.put("id", event.get("id"));
            formatted.put("text", event.get("title"));
            formatted.put("start_date", date.atTime(9, 0).format(formatter));  // example: 9AM
            formatted.put("end_date", date.atTime(11, 0).format(formatter));   // example: 11AM
            formattedEvents.add(formatted);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new Gson().toJson(formattedEvents));
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

        // Get calendar data
        CalendarData calendarData = getCalendarData(request);
        request.setAttribute("calendarDate", calendarData.date);
        request.setAttribute("currentYear", calendarData.year);
        request.setAttribute("currentMonth", calendarData.month);
        request.setAttribute("daysInMonth", calendarData.daysInMonth);
        request.setAttribute("firstDayOfWeek", calendarData.firstDayOfWeek);
        request.setAttribute("prevYear", calendarData.prevYear);
        request.setAttribute("prevMonth", calendarData.prevMonth);
        request.setAttribute("nextYear", calendarData.nextYear);
        request.setAttribute("nextMonth", calendarData.nextMonth);

        // Get training events for the calendar
        request.setAttribute("calendarEvents", dashboardService.getTrainingCalendarData(calendarData.year, calendarData.month));

        // Get budget overview data
        request.setAttribute("budgetData", dashboardService.getBudgetOverviewData());

        // Set JSP components
        setJspComponents(request);
    }
    private void handleError(HttpServletRequest request, HttpServletResponse response, Exception e)
            throws IOException, ServletException {
        getServletContext().log("Dashboard Error", e);
        request.setAttribute("error", "An error occurred. Please try again.");
        response.sendRedirect(request.getContextPath() + "/error.jsp");
    }

    private CalendarData getCalendarData(HttpServletRequest request) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1; // Calendar month is 0-based

        // Allow overriding year and month from request parameters
        String yearParam = request.getParameter("year");
        String monthParam = request.getParameter("month");

        if (yearParam != null && !yearParam.isEmpty()) {
            year = Integer.parseInt(yearParam);
        }
        if (monthParam != null && !monthParam.isEmpty()) {
            month = Integer.parseInt(monthParam);
        }

        cal.set(year, month - 1, 1);
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

        return new CalendarData(
                cal.getTime(),
                year,
                month,
                daysInMonth,
                firstDayOfWeek,
                month == 1 ? year - 1 : year,
                month == 1 ? 12 : month - 1,
                month == 12 ? year + 1 : year,
                month == 12 ? 1 : month + 1
        );
    }

    private void setJspComponents(HttpServletRequest request) {
        request.setAttribute("statsCards", "/components/statsCards.jsp");
        request.setAttribute("enrollTrends", "/components/enrollmentTrends.jsp");
        request.setAttribute("trainingCom", "/components/topTrainings.jsp");
        request.setAttribute("participantProfile", "/components/profileChart.jsp");
        request.setAttribute("trainingCal", "/components/trainingCalendar.jsp");
        request.setAttribute("budgetOver", "/components/budgetOverview.jsp");
    }

    private void handleError(HttpServletRequest request, Exception e) {
        request.setAttribute("error", "Failed to load dashboard data: " + e.getMessage());
        getServletContext().log("Dashboard error", e);
    }

    @Override
    public void destroy() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    // Helper class for calendar data
        private record CalendarData(Object date, int year, int month, int daysInMonth, int firstDayOfWeek, int prevYear,
                                    int prevMonth, int nextYear, int nextMonth) {
    }
}