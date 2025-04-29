<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="row">
    <div class="col-md-8">
        <div class="card">
            <div class="card-header">
                <span>Enrollment Trends</span>
                <div class="dropdown">
                    <button class="btn btn-sm btn-outline-secondary dropdown-toggle" type="button" id="enrollmentPeriodDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                        All
                    </button>
                    <ul class="dropdown-menu" aria-labelledby="enrollmentPeriodDropdown">
                        <li><a class="dropdown-item enrollment-period" id="all-date" href="#" data-period="all">All</a></li>
                        <li><a class="dropdown-item enrollment-period" href="#" data-period="year">This Year</a></li>
                        <li><a class="dropdown-item enrollment-period" href="#" data-period="6months">Last 6 Months</a></li>
                        <li><a class="dropdown-item enrollment-period" href="#" data-period="month">Last Month</a></li>
                    </ul>
                </div>
            </div>
            <div class="card-body">
                <div class="chart-container">
                    <canvas id="enrollmentChart"></canvas>
                </div>
            </div>
        </div>
    </div>
    <div class="col-md-4">
        <div class="card">
            <div class="card-header">
                <span>Training Domains</span>
            </div>
            <div class="card-body">
                <div class="chart-container">
                    <canvas id="domainChart"></canvas>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    // Prepare the enrollment chart data
    const enrollmentLabels = [
        <c:forEach items="${enrollmentTrend.labels}" var="label" varStatus="status">
        "${label}"<c:if test="${!status.last}">,</c:if>
        </c:forEach>
    ];

    const enrollmentData = [
        <c:forEach items="${enrollmentTrend.data}" var="value" varStatus="status">
        ${value}<c:if test="${!status.last}">,</c:if>
        </c:forEach>
    ];

    // Prepare domain chart data
    const domainLabels = [
        <c:forEach items="${domainDistribution.labels}" var="label" varStatus="status">
        "${label}"<c:if test="${!status.last}">,</c:if>
        </c:forEach>
    ];

    const domainData = [
        <c:forEach items="${domainDistribution.data}" var="value" varStatus="status">
        ${value}<c:if test="${!status.last}">,</c:if>
        </c:forEach>
    ];

    const domainColors = [
        <c:forEach items="${domainDistribution.colors}" var="color" varStatus="status">
        "${color}"<c:if test="${!status.last}">,</c:if>
        </c:forEach>
    ];

    // Domain Chart
    const domainCtx = document.getElementById('domainChart').getContext('2d');
    const domainChart = new Chart(domainCtx, {
        type: 'doughnut',
        data: {
            labels: domainLabels,
            datasets: [{
                data: domainData,
                backgroundColor: domainColors
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'bottom'
                }
            }
        }
    });

    // Enrollment Chart
    let enrollmentChart = null;
    document.addEventListener('DOMContentLoaded', function() {
        initializeEnrollmentChart();
        setupPeriodDropdown();
        document.getElementById("all-date").click();


    });

    function initializeEnrollmentChart() {
        const ctx = document.getElementById('enrollmentChart').getContext('2d');

        // Destroy previous chart if exists
        if (enrollmentChart) {
            enrollmentChart.destroy();
        }

        enrollmentChart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: window.enrollmentLabels || [],
                datasets: [{
                    label: 'Enrollments',
                    data: window.enrollmentData || [],
                    borderColor: '#3498db',
                    backgroundColor: 'rgba(52, 152, 219, 0.1)',
                    borderWidth: 2,
                    fill: true
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: { legend: { display: false } },
                scales: {
                    y: { beginAtZero: true },
                    x: {
                        ticks: {
                            maxRotation: 45,
                            minRotation: 45
                        }
                    }
                }
            }
        });
    }

    // Period selection handling
    function setupPeriodDropdown() {
        document.querySelectorAll('.enrollment-period').forEach(item => {
            item.addEventListener('click', function(event) {
                event.preventDefault();
                const period = this.getAttribute('data-period');
                const dropdownText = this.textContent;

                // Update UI
                const dropdown = document.getElementById('enrollmentPeriodDropdown');
                dropdown.innerHTML = '<span class="spinner-border spinner-border-sm"></span> Loading...';

                // Make AJAX call
                fetch("dashboard?action=enrollmentTrend&period=" + period)
                    .then(response => {
                        if (!response.ok) throw new Error('Network response was not ok');
                        return response.json();
                    })
                    .then(data => {
                        console.log('Received data:', data);

                        // Update chart data
                        enrollmentChart.data.labels = data.labels;
                        enrollmentChart.data.datasets[0].data = data.data;
                        enrollmentChart.update();

                        // Restore dropdown text
                        dropdown.textContent = dropdownText;
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        dropdown.textContent = 'Error - Try Again';
                        setTimeout(() => {
                            dropdown.textContent = dropdownText;
                        }, 2000);
                    });
            });
        });
    }
</script>

<script>
    // Debug initial data
    console.log("Initial chart data:", {
        labels: [<c:forEach items="${enrollmentTrend.labels}" var="label">"${label}",</c:forEach>],
        data: [<c:forEach items="${enrollmentTrend.data}" var="value">${value},</c:forEach>]
    });

    // Verify elements exist
    console.log("Chart canvas exists:", document.getElementById('enrollmentChart') !== null);
    console.log("Dropdown exists:", document.getElementById('enrollmentPeriodDropdown') !== null);
</script>