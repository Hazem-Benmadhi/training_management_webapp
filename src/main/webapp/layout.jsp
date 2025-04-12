<!-- layout.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title><c:out value="${pageTitle != null ? pageTitle : 'App'}" /></title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <style>
    body {
      padding-top: 70px;
      padding-bottom: 60px;
    }
    .footer {
      position: fixed;
      bottom: 0;
      height: 60px;
      width: 100%;
      background-color: #f8f9fa;
      text-align: center;
      padding-top: 20px;
    }
  </style>
</head>
<body>

<!-- Fixed Navbar -->
<nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
  <div class="container-fluid">
    <a class="navbar-brand" href="#">MyApp</a>
    <div class="collapse navbar-collapse">
      <ul class="navbar-nav ms-auto">
        <li class="nav-item"><a class="nav-link" href="formations?action=list">Formations</a></li>
        <li class="nav-item"><a class="nav-link" href="participants?action=list">Participants</a></li>
        <li class="nav-item"><a class="nav-link" href="trainers?action=list">Trainers</a></li>
        <li class="nav-item"><a class="nav-link" href="domains?action=list">Domain</a></li>

      </ul>
    </div>
  </div>
</nav>

<!-- Main Content -->
<div class="container">
  <jsp:include page="${pageContent}" />
</div>

<!-- Footer -->
<div class="footer">
  &copy; 2025 - Green Building App
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
