package com.greenbuilding.demo.service;

import com.greenbuilding.demo.dao.FormationDAO;
import com.greenbuilding.demo.entity.Formation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarService {
    private final FormationDAO formationDAO;

    public CalendarService() {
        this.formationDAO = new FormationDAO();
    }

//    public Map<Integer, List<TrainingEvent>> getTrainingsForMonth(int month, int year) {
//        LocalDate startDate = LocalDate.of(year, month, 1);
//        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
//
//        List<Formation> formations = formationDAO.getFormationsInDateRange(startDate, endDate);
//        Map<Integer, List<TrainingEvent>> trainingsByDay = new HashMap<>();
//
//        for (Formation formation : formations) {
//            LocalDate date = formation.getDate();
//            int day = date.getDayOfMonth();
//
//            if (!trainingsByDay.containsKey(day)) {
//                trainingsByDay.put(day, new ArrayList<>());
//            }
//
//            TrainingEvent event = new TrainingEvent();
//            event.setId(formation.getId());
//            event.setTitle(formation.getTitle());
//            event.setShortTitle(formatShortTitle(formation.getTitle()));
//            event.setTime(formatTime(formation)); // Format time based on your needs
//            event.setColorClass(getColorClassForDomain(formation.getDomain().getLibelle()));
//
//            trainingsByDay.get(day).add(event);
//        }
//
//        return trainingsByDay;
//    }
//
//    private String formatShortTitle(String title) {
//        if (title.length() <= 10) return title;
//        return title.substring(0, 8) + "..";
//    }
//
//    private String formatTime(Formation formation) {
//        // You would need to add a time field to your Formation entity
//        // For now, returning a placeholder
//        return "9:00";
//    }
//
//    private String getColorClassForDomain(String domainName) {
//        // Map domain names to color classes
//        Map<String, String> colorMap = new HashMap<>();
//        colorMap.put("Web Development", "primary");
//        colorMap.put("Data Science", "info");
//        colorMap.put("Project Management", "warning");
//        colorMap.put("Digital Marketing", "success");
//        colorMap.put("UX/UI Design", "danger");
//
//        return colorMap.getOrDefault(domainName, "secondary");
//    }
}
