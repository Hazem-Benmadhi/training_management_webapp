package com.greenbuilding.demo.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class DashboardService {
    private EntityManager entityManager;

    private Map<String, Object> cache = new HashMap<>();
    private long cacheTimestamp = 0;
    private static final long CACHE_TIMEOUT = 5 * 60 * 1000; // 5 minutes

    public DashboardService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /* Simple caching mechanism */
    private boolean isCacheValid() {
        return System.currentTimeMillis() - cacheTimestamp < CACHE_TIMEOUT;
    }

    /**
     * Get statistics for the dashboard cards section
     * @return Map containing counts and sums for main statistics
     */
    public Map<String, Object> getStatisticsCardData() {
        if (isCacheValid() && cache.containsKey("stats")) {
            return (Map<String, Object>) cache.get("stats");
        }
        // Combine queries into a single native query for better performance
        String nativeQuery = """
        SELECT 
            (SELECT COUNT(*) FROM participant) AS total_participants,
            (SELECT COUNT(*) FROM formation WHERE date >= CURRENT_DATE) AS active_trainings,
            (SELECT COUNT(*) FROM trainer) AS total_trainers,
            (SELECT COALESCE(SUM(budget), 0) FROM formation) AS total_budget
        """;

        Object[] result = (Object[]) entityManager.createNativeQuery(nativeQuery)
                .getSingleResult();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalParticipants", ((Number)result[0]).intValue());
        stats.put("activeTrainings", ((Number)result[1]).intValue());
        stats.put("totalTrainers", ((Number)result[2]).intValue());
        stats.put("totalBudget", ((Number)result[3]).doubleValue());

        cache.put("stats", stats);
        cacheTimestamp = System.currentTimeMillis();
        return stats;
    }

    /**
     * Get enrollment trend data for the line chart
     * @param period "year", "6months", or "month"
     * @return Map with labels and data for chart
     */
    public Map<String, Object> getEnrollmentTrendData(String period) {
        System.out.println("Generating trend data for period: " + period); // Debug log

        Map<String, Object> result = new HashMap<>();
        List<String> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>();

        try {
            LocalDate endDate = LocalDate.now();
            LocalDate startDate = calculateStartDate(period, endDate);
            System.out.println("startDate: " + startDate);

            String jpql = buildEnrollmentQuery(period);
            System.out.println("Executing query: " + jpql);

            Query query = entityManager.createQuery(jpql)
                    .setParameter("startDate", startDate)
                    .setParameter("endDate", endDate);

            List<Object[]> results = query.getResultList();

            if (results.isEmpty()) {
                System.out.println("No results found - generating sample data");
//                generateSampleData(period, labels, data);
            } else {
                System.out.println("Found " + results.size() + " records");
                for (Object[] row : results) {
                    labels.add((String) row[0]);
                    data.add((Long) row[1]);
                }
            }

            result.put("labels", formatLabels(labels, period));
            result.put("data", data);

        } catch (Exception e) {
            System.err.println("Error in getEnrollmentTrendData: " + e.getMessage());
            result.put("error", "Failed to generate trend data");
        }

        System.out.println("Returning result: " + result);
        return result;

    }

    private LocalDate calculateStartDate(String period, LocalDate endDate) {
        return switch (period) {
            case "month" -> endDate.minusMonths(1);
            case "6months" -> endDate.minusMonths(6);
            case "year" -> endDate.minusYears(1);
            default -> endDate.minusYears(10);
        };
    }

    private String buildEnrollmentQuery(String period) {
        switch (period.toLowerCase()) {
            case "month":
                return "SELECT FUNCTION('TO_CHAR', f.date, 'YYYY-MM-DD'), COUNT(p) " +
                        "FROM Formation f, Participant p " +
                        "WHERE f.date BETWEEN :startDate AND :endDate " +
                        "GROUP BY FUNCTION('TO_CHAR', f.date, 'YYYY-MM-DD') " +
                        "ORDER BY FUNCTION('TO_CHAR', f.date, 'YYYY-MM-DD')";
            case "6months":
                return "SELECT FUNCTION('TO_CHAR', f.date, 'YYYY-MM'), COUNT(p) " +
                        "FROM Formation f JOIN f.participants p " +
                        "WHERE f.date BETWEEN :startDate AND :endDate " +
                        "GROUP BY FUNCTION('TO_CHAR', f.date, 'YYYY-MM') " +
                        "ORDER BY FUNCTION('TO_CHAR', f.date, 'YYYY-MM')";
            case "year":
                return "SELECT FUNCTION('TO_CHAR', f.date, 'YYYY-MM-DD'), COUNT(p) " +
                        "FROM Formation f, Participant p " +
                        "WHERE f.date BETWEEN :startDate AND :endDate " +
                        "GROUP BY FUNCTION('TO_CHAR', f.date, 'YYYY-MM-DD') " +
                        "ORDER BY FUNCTION('TO_CHAR', f.date, 'YYYY-MM-DD')";

            default:
                return "SELECT FUNCTION('TO_CHAR', f.date, 'YYYY-MM'), COUNT(p) " +
                        "FROM Formation f JOIN f.participants p " +
                        "WHERE f.date BETWEEN :startDate AND :endDate " +
                        "GROUP BY FUNCTION('TO_CHAR', f.date, 'YYYY-MM') " +
                        "ORDER BY FUNCTION('TO_CHAR', f.date, 'YYYY-MM')";
        }
    }

    private List<String> formatLabels(List<String> labels, String period) {
        if ("month".equals(period)) {
            return labels.stream()
                    .map(label -> LocalDate.parse(label).format(DateTimeFormatter.ofPattern("MMM dd")))
                    .collect(Collectors.toList());
        }
        return labels;
    }

    private void generateSampleData(String period, List<String> labels, List<Long> data) {
        int items = "month".equals(period) ? 30 : "6months".equals(period) ? 6 : 12;
        Random random = new Random();

        for (int i = 0; i < items; i++) {
            if ("month".equals(period)) {
                labels.add(LocalDate.now().minusDays(items - i).format(DateTimeFormatter.ISO_DATE));
            } else {
                labels.add(LocalDate.now().minusMonths(items - i).format(DateTimeFormatter.ofPattern("yyyy-MM")));
            }
            data.add(random.nextInt(50) + 10L);
        }
    }

    /**
     * Get domain distribution data for pie/doughnut chart
     * @return Map with domain names and counts
     */
    public Map<String, Object> getDomainDistributionData() {
        Map<String, Object> result = new HashMap<>();
        List<String> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>();
        List<String> colors = List.of(
                "#3498db", "#9b59b6", "#f39c12", "#2ecc71", "#e74c3c",
                "#1abc9c", "#34495e", "#d35400", "#16a085", "#c0392b"
        );

        String jpql = " SELECT d.libelle, COUNT(p) " +
                " FROM Formation f " +
                "JOIN f.domain d " +
                "JOIN f.participants p " +
                "GROUP BY d.libelle " +
                "ORDER BY COUNT(p) DESC";

        List<Object[]> results = entityManager.createQuery(jpql).getResultList();
        if (!results.isEmpty()) {
            for (Object[] row : results) {
                labels.add((String) row[0]);
                data.add((Long) row[1]);
            }
        }

        result.put("labels", labels);
        result.put("data", data);
        result.put("colors", colors.subList(0, Math.min(colors.size(), labels.size())));
        return result;
    }

    /**
     * Get data for top trainings by participant count
     * @return List of trainings with participant counts
     */
    public List<Map<String, Object>> getTopTrainings(int limit) {
        List<Map<String, Object>> topTrainings = new ArrayList<>();

        // Correct JPQL using the mapped relationship
        String jpql = "SELECT f.id, f.title, COUNT(p.id) as participantCount " +
                "FROM Formation f JOIN f.participants p " +
                "GROUP BY f.id, f.title " +
                "ORDER BY COUNT(p.id) DESC";

        Query query = entityManager.createQuery(jpql);
        query.setMaxResults(limit);

        List<Object[]> results = query.getResultList();

        for (Object[] result : results) {
            Map<String, Object> training = new HashMap<>();
            System.out.println(training);
            training.put("id", result[0]);
            training.put("title", result[1]);
            training.put("participantCount", result[2]);
            topTrainings.add(training);
        }

        return topTrainings;
    }

    /**
     * Get all trainings with pagination
     * @param page Current page number (1-based)
     * @param pageSize Number of items per page
     * @return List of trainings with participant counts
     */
    public List<Map<String, Object>> getAllTrainings(int page, int pageSize) {
        int firstResult = (page - 1) * pageSize;

        String jpql = "SELECT f.id, f.title, COUNT(p.id) as participantCount " +
                "FROM Formation f LEFT JOIN f.participants p " +
                "GROUP BY f.id, f.title " +
                "ORDER BY COUNT(p.id) DESC";

        Query query = entityManager.createQuery(jpql)
                .setFirstResult(firstResult)
                .setMaxResults(pageSize);

        List<Object[]> results = query.getResultList();

        List<Map<String, Object>> trainings = new ArrayList<>();
        for (Object[] result : results) {
            Map<String, Object> training = new HashMap<>();
            training.put("id", result[0]);
            training.put("title", result[1]);
            training.put("participantCount", result[2]);
            trainings.add(training);
        }

        return trainings;
    }

    /**
     * Get total count of all trainings
     * @return Total number of trainings
     */
    public int getTotalTrainingCount() {
        Long count = (Long) entityManager.createQuery("SELECT COUNT(DISTINCT f) FROM Formation f")
                .getSingleResult();
        return count != null ? count.intValue() : 0;
    }

    /**
     * Get participant profile distribution data
     * @return Map with profile types as keys and counts as values
     */
    public Map<String, Long> getParticipantProfileDistribution() {
        Map<String, Long> profileDistribution = new LinkedHashMap<>();

        // Query to get count of participants by profile
        String jpql = "SELECT pr.libelle, COUNT(p) " +
                "FROM Participant p JOIN Profil pr ON p.profil.id = pr.id " +
                "GROUP BY pr.libelle " +
                "ORDER BY COUNT(p) DESC";

        List<Object[]> results = entityManager.createQuery(jpql).getResultList();

        for (Object[] result : results) {
            String profileName = (String) result[0];
            Long count = (Long) result[1];
            profileDistribution.put(profileName, count);
        }

        return profileDistribution;
    }

    /**
     * Get training schedule for calendar view
     * @param year The year to get trainings for
     * @param month The month to get trainings for (1-12)
     * @return List of trainings with dates and details
     */
    public List<Map<String, Object>> getTrainingCalendarData(int year, int month) {
        List<Map<String, Object>> calendarEvents = new ArrayList<>();

        // Convert year and month to LocalDate range
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        // Query to get trainings in the specified month
        String jpql = "SELECT f.id, f.title, f.date, d.libelle " +
                "FROM Formation f " +
                "JOIN f.domain d " +
                "WHERE f.date BETWEEN :startDate AND :endDate " +
                "ORDER BY f.date";

        Query query = entityManager.createQuery(jpql);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);

        List<Object[]> results = query.getResultList();

        for (Object[] result : results) {
            Map<String, Object> event = new HashMap<>();
            event.put("id", result[0]);
            event.put("title", result[1]);
            event.put("date", result[2]);
            event.put("domain", result[3]);

            calendarEvents.add(event);
        }

        return calendarEvents;
    }

    /**
     * Get budget data by domain for budget overview
     * @return Map with budget allocation by domain
     */
    public Map<String, Object> getBudgetOverviewData() {
        Map<String, Object> budgetData = new HashMap<>();

        // Get total budget
        Query totalQuery = entityManager.createQuery("SELECT SUM(f.budget) FROM Formation f");
        Double totalBudget = (Double) totalQuery.getSingleResult();
        if (totalBudget == null) totalBudget = 0.0;
        budgetData.put("totalBudget", totalBudget);

        // Get budget by domain
        String jpql = "SELECT d.libelle, SUM(f.budget) as domainBudget " +
                "FROM Formation f JOIN Domain d ON f.domain.id = d.id " +
                "GROUP BY d.libelle " +
                "ORDER BY domainBudget DESC";

        Query query = entityManager.createQuery(jpql);
        List<Object[]> results = query.getResultList();

        Map<String, Double> domainBudgets = new LinkedHashMap<>();
        for (Object[] result : results) {
            String domainName = (String) result[0];
            Double budget = (Double) result[1];
            if (budget == null) budget = 0.0;
            domainBudgets.put(domainName, budget);
        }

        budgetData.put("domainBudgets", domainBudgets);

        return budgetData;
    }
}