<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="card">
    <div class="card-header">
        <span>Budget Overview</span>
    </div>
    <div class="card-body">
        <div class="chart-container">
            <canvas id="budgetChart"></canvas>
        </div>
        <hr>
        <div>
            <c:forEach var="domain" items="${budgetData.domainBudgets}" varStatus="status">
                <c:if test="${status.index < 3}">
                    <div class="d-flex justify-content-between mb-2">
                        <span><c:out value="${domain.key}" /></span>
                        <span><fmt:formatNumber value="${domain.value}" type="currency" currencySymbol="$" maxFractionDigits="0"/></span>
                    </div>
                    <div class="progress mb-3">
                        <div class="progress-bar
                             <c:choose>
                                 <c:when test="${status.index == 0}">bg-primary</c:when>
                                 <c:when test="${status.index == 1}">bg-info</c:when>
                                 <c:when test="${status.index == 2}">bg-warning</c:when>
                                 <c:otherwise>bg-success</c:otherwise>
                             </c:choose>"
                             role="progressbar"
                             style="width: <c:out value="${(domain.value / budgetData.totalBudget) * 100}" />%"
                             aria-valuenow="<c:out value="${(domain.value / budgetData.totalBudget) * 100}" />"
                             aria-valuemin="0"
                             aria-valuemax="100">
                        </div>
                    </div>
                </c:if>
            </c:forEach>
        </div>
    </div>
</div>


<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Budget Chart
        const budgetCtx = document.getElementById('budgetChart').getContext('2d');

        const budgetLabels = [
            <c:forEach items="${budgetData.domainBudgets}" var="domain" varStatus="status">
            '${domain.key}'<c:if test="${!status.last}">,</c:if>
            </c:forEach>
        ];

        const budgetData = [
            <c:forEach items="${budgetData.domainBudgets}" var="domain" varStatus="status">
            ${domain.value}<c:if test="${!status.last}">,</c:if>
            </c:forEach>
        ];

        const budgetChart = new Chart(budgetCtx, {
            type: 'pie',
            data: {
                labels: budgetLabels,
                datasets: [{
                    data: budgetData,
                    backgroundColor: [
                        '#3498db',
                        '#9b59b6',
                        '#f39c12',
                        '#2ecc71',
                        '#e74c3c'
                    ]
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
    });
</script>