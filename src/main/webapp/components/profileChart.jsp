<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="card">
  <div class="card-header">
    <span>Participant Profiles</span>
  </div>
  <div class="card-body">
    <div class="chart-container">
      <canvas id="profileChart"></canvas>
    </div>
  </div>
</div>

<script>
  // When the document is ready
  document.addEventListener('DOMContentLoaded', function() {
    // Profile Chart
    const profileCtx = document.getElementById('profileChart').getContext('2d');
    const profileLabels = [
      <c:forEach items="${profileDistribution}" var="profile" varStatus="status">
      '${profile.key}'<c:if test="${!status.last}">,</c:if>
      </c:forEach>
    ];
    const profileData = [
      <c:forEach items="${profileDistribution}" var="profile" varStatus="status">
      ${profile.value}<c:if test="${!status.last}">,</c:if>
      </c:forEach>
    ];

    const profileChart = new Chart(profileCtx, {
      type: 'bar',
      data: {
        labels: profileLabels,
        datasets: [{
          label: 'Participants by Profile',
          data: profileData,
          backgroundColor: [
            'rgba(52, 152, 219, 0.7)',
            'rgba(155, 89, 182, 0.7)',
            'rgba(243, 156, 18, 0.7)',
            'rgba(46, 204, 113, 0.7)',
            'rgba(231, 76, 60, 0.7)',
            'rgba(52, 73, 94, 0.7)'
          ]
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            display: false
          }
        },
        scales: {
          y: {
            beginAtZero: true
          }
        }
      }
    });
  });
</script>