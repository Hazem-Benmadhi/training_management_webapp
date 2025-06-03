<%@ page import="com.greenbuilding.demo.entity.AppUser" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    AppUser user = (AppUser) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Training Center Dashboard</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/styles.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/chartjs-adapter-date-fns/dist/chartjs-adapter-date-fns.bundle.min.js"></script>
</head>
<body>
<!-- Sidebar -->
<div class="sidebar">
    <div class="logo">
        <div class="logo-icon">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 100 100" fill="white">
                <path d="M50 15 L20 35 L20 85 L80 85 L80 35 Z" stroke="white" stroke-width="2" fill="none"/>
                <path d="M35 35 L50 20 L65 35" stroke="white" stroke-width="2" fill="none"/>
                <circle cx="50" cy="30" r="5" fill="#8bc34a"/>
            </svg>
        </div>
        <h4>Green Building</h4>
    </div>
    <ul class="nav flex-column">
        <c:if test="${user.role == 'admin' || user.role == 'responsible'}">
            <li class="nav-item">
                <a class="nav-link ${pageContext.request.servletPath == '/dashboard' ? 'active' : ''}" href="dashboard">
                    <i class="fas fa-tachometer-alt"></i> <span>Dashboard</span>
                </a>
            </li>
        </c:if>

        <c:if test="${user.role == 'admin' || user.role == 'simple'}">
            <li class="nav-item">
                <a class="nav-link ${pageContext.request.servletPath == '/trainers' ? 'active' : ''}" href="trainers">
                    <i class="fas fa-chalkboard-teacher"></i> <span>Trainers</span>
                </a>
            </li>
        </c:if>

        <c:if test="${user.role == 'admin' || user.role == 'simple'}">
            <li class="nav-item">
                <a class="nav-link ${pageContext.request.servletPath == '/formations' ? 'active' : ''}" href="formations">
                    <i class="fas fa-book"></i> <span>Trainings</span>
                </a>
            </li>
        </c:if>

        <c:if test="${user.role == 'admin' || user.role == 'simple'}">
            <li class="nav-item">
                <a class="nav-link ${pageContext.request.servletPath == '/participants' ? 'active' : ''}" href="participants">
                    <i class="fas fa-user-graduate"></i> <span>Participants</span>
                </a>
            </li>
        </c:if>

        <c:if test="${user.role == 'admin'}">
            <li class="nav-item">
                <a class="nav-link ${pageContext.request.servletPath == '/domains' ? 'active' : ''}" href="domains">
                    <i class="fas fa-sitemap"></i> <span>Domains</span>
                </a>
            </li>
        </c:if>

        <c:if test="${user.role == 'admin'}">
            <li class="nav-item">
                <a class="nav-link ${pageContext.request.servletPath == '/employers' ? 'active' : ''}" href="employers">
                    <i class="fas fa-building"></i> <span>Employers</span>
                </a>
            </li>
        </c:if>

        <c:if test="${user.role == 'admin'}">
            <li class="nav-item">
                <a class="nav-link ${pageContext.request.servletPath == '/profiles' ? 'active' : ''}" href="profiles">
                    <i class="fas fa-id-card"></i> <span>Profiles</span>
                </a>
            </li>
        </c:if>

        <c:if test="${user.role == 'admin'}">
            <li class="nav-item">
                <a class="nav-link ${pageContext.request.servletPath == '/settings' ? 'active' : ''}" href="users">
                    <i class="fas fa-users-cog"></i><span>User Management</span>
                </a>
            </li>
        </c:if>

        <c:if test="${user.role == 'admin' || user.role == 'responsible'}">
            <li class="nav-item">
                <a class="nav-link ${pageContext.request.servletPath == '/reports' ? 'active' : ''}" href="exportReport">
                    <i class="fas fa-chart-bar"></i> <span>Download Reports</span>
                </a>
            </li>
        </c:if>

        <li class="nav-item mt-auto logout-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/logout">
                <i class="fas fa-sign-out-alt"></i> <span>Logout</span>
            </a>
        </li>
    </ul>
</div>

<!-- Main Content -->
<div class="main-content">
    <!-- Top bar -->
    <div class="topbar">
        <h4>Dashboard</h4>
        <div class="user-info">
            <div class="avatar">${user.name.charAt(0)}</div> <!-- Show first letter of name -->
            <div>${user.name}</div>
        </div>
    </div>

    <!-- Dashboard Content -->
    <div class="dashboard-content p-3">
        <!-- Main Content Section -->
        <div class="main-component">
            <jsp:include page="${mainContent}"/>
        </div>

        <!-- Stats Cards Row -->
        <div class="row g-3 mb-4">
            <div class="col-12">
                <jsp:include page="${statsCards}"/>
            </div>
        </div>

        <!-- First Row - Enrollment Trends (Full Width) -->
        <div class="row g-3 mb-4">
            <div class="col-12">
                <div class="card">
                    <div class="card-body">
                        <jsp:include page="${enrollTrends}"/>
                    </div>
                </div>
            </div>
        </div>

        <!-- Second Row - Two Equal Components -->
        <div class="row">
            <div class="col-md-6">
                <%-- Top Training --%>
                <jsp:include page="${trainingCom}"/>
            </div>
            <div class="col-md-6">
                <%-- Participant profile --%>
                <jsp:include page="${participantProfile}"/>
            </div>
        </div>

        <!-- Third Row - Budget Overview -->
        <div class="row">
            <div class="col-md-4">
                <%--Budget Overview--%>
                <jsp:include page="${budgetOver}"/>
            </div>
        </div>
    </div>
</div>

<!-- Footer -->
<div class="footer">
    <div class="d-flex justify-content-between align-items-center">
        <div>&copy; 2025 - Green Building App</div>
        <div>Version 1.0.3</div>
    </div>
</div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/3.9.1/chart.min.js"></script>
</body>
</html>