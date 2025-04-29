package com.greenbuilding.demo.controller;

import com.greenbuilding.demo.service.CalendarService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Calendar;

@WebServlet("/calendar")
public class CalendarServlet extends HttpServlet {
    private CalendarService calendarService;

    @Override
    public void init() {
        this.calendarService = new CalendarService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get month and year from request, default to current month/year if not specified
        String monthParam = request.getParameter("month");
        String yearParam = request.getParameter("year");

        int month, year;
        Calendar cal = Calendar.getInstance();

        if (monthParam != null && yearParam != null) {
            month = Integer.parseInt(monthParam);
            year = Integer.parseInt(yearParam);
        } else {
            month = cal.get(Calendar.MONTH);
            year = cal.get(Calendar.YEAR);
        }

        // Calculate previous and next month
        Calendar prevCal = Calendar.getInstance();
        prevCal.set(year, month, 1);
        prevCal.add(Calendar.MONTH, -1);

        Calendar nextCal = Calendar.getInstance();
        nextCal.set(year, month, 1);
        nextCal.add(Calendar.MONTH, 1);

        // Get calendar data
        cal.set(year, month, 1);
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int firstDay = cal.get(Calendar.DAY_OF_WEEK) - 1; // 0 = Sunday
        int lastDay = 7 - ((firstDay + daysInMonth) % 7);
        if (lastDay == 7) lastDay = 0;

        // Get trainings for the month
//        Map<Integer, List<TrainingEvent>> trainingsByDay =
//                calendarService.getTrainingsForMonth(month + 1, year);

        // Set attributes
//        request.setAttribute("currentDate", cal.getTime());
//        request.setAttribute("daysInMonth", daysInMonth);
//        request.setAttribute("firstDay", firstDay);
//        request.setAttribute("lastDay", lastDay);
//        request.setAttribute("prevMonth", prevCal.get(Calendar.MONTH));
//        request.setAttribute("prevYear", prevCal.get(Calendar.YEAR));
//        request.setAttribute("nextMonth", nextCal.get(Calendar.MONTH));
//        request.setAttribute("nextYear", nextCal.get(Calendar.YEAR));
//        request.setAttribute("trainingsByDay", trainingsByDay);

        // Forward to the calendar page
        request.getRequestDispatcher("/pages/calendar.jsp").forward(request, response);
    }
}