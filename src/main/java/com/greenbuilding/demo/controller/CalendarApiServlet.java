package com.greenbuilding.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenbuilding.demo.service.DashboardService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/api/calendarEvents")
public class CalendarApiServlet extends HttpServlet {

    private EntityManagerFactory emf;

    @Override
    public void init() throws ServletException {
        emf = Persistence.createEntityManagerFactory("dbPU");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        EntityManager em = emf.createEntityManager();
        DashboardService dashboardService = new DashboardService(em);

        int year = Integer.parseInt(request.getParameter("year"));
        int month = Integer.parseInt(request.getParameter("month"));


        List<Map<String, Object>> events = dashboardService.getTrainingCalendarData(year, month);
        System.out.println("events : " + events);

        List<Map<String, Object>> formattedEvents = events.stream().map(event -> {
            Map<String, Object> e = new HashMap<>();
            e.put("id", event.get("id"));
            e.put("text", event.get("title"));
            LocalDate date = ((Date) event.get("date")).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            e.put("start_date", date.atTime(10, 0).toString());
            e.put("end_date", date.atTime(12, 0).toString());
            e.put("domain", event.get("domain"));
            return e;
        }).collect(Collectors.toList());

        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getWriter(), formattedEvents);
    }
}
