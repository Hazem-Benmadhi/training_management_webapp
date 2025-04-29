<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
  <title><c:out value="${pageTitle != null ? pageTitle : 'Green Building App'}" /></title>
  <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.2/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
  <link rel="stylesheet" href="css/styles.css">
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
        <a class="nav-link ${pageContext.request.servletPath == '/calendar' ? 'active' : ''}" href="#">
          <i class="fas fa-calendar-alt"></i> <span>Calendar</span>
        </a>
      </li>
    </c:if>

    <c:if test="${user.role == 'admin' || user.role == 'responsible'}">
      <li class="nav-item">
        <a class="nav-link ${pageContext.request.servletPath == '/reports' ? 'active' : ''}" href="exportReport">
          <i class="fas fa-chart-bar"></i> <span>Reports</span>
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
  <jsp:include page="${pageContent}" />
</div>

<!-- Footer -->
<div class="footer">
  &copy; 2025 - Green Building App
</div>

<!-- Bootstrap JS Bundle with Popper -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.2/js/bootstrap.bundle.min.js"></script>
<script>
  // Add active class to current menu item
  document.addEventListener('DOMContentLoaded', function() {
    // Get current path
    var path = window.location.pathname;
    var page = path.split("/").pop();

    // Loop through all nav links
    var navLinks = document.querySelectorAll('.sidebar .nav-link');
    navLinks.forEach(function(link) {
      if (link.getAttribute('href') === page) {
        link.classList.add('active');
      }
    });
  });
</script>
</body>
</html>