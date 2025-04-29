<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Green Building - Login</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.2/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/login.css">
</head>
<body>
<div class="login-container">
    <div class="row g-0">
        <div class="col-6">
            <div class="left-panel d-flex flex-column justify-content-center align-items-center px-4 h-100">
                <div class="pattern"></div>
                <div class="leaf"></div>
                <div class="leaf"></div>
                <div class="leaf"></div>

                <div class="logo-container">
                    <div class="logo">
                        <svg xmlns="http://www.w3.org/2000/svg" width="60" height="60" viewBox="0 0 100 100" fill="white">
                            <path d="M50 15 L20 35 L20 85 L80 85 L80 35 Z" stroke="white" stroke-width="2" fill="none"/>
                            <path d="M30 45 L70 45" stroke="white" stroke-width="2"/>
                            <path d="M30 60 L70 60" stroke="white" stroke-width="2"/>
                            <path d="M30 75 L70 75" stroke="white" stroke-width="2"/>
                            <path d="M35 35 L50 20 L65 35" stroke="white" stroke-width="2" fill="none"/>
                            <circle cx="50" cy="30" r="8" fill="#8bc34a"/>
                            <path d="M42 30 C42 25, 58 25, 58 30" stroke="#8bc34a" stroke-width="2" fill="none"/>
                            <path d="M45 75 L45 60 L55 60 L55 75" stroke="white" stroke-width="2" fill="none"/>
                        </svg>
                    </div>
                </div>
                <h2 class="mb-2 text-center fw-bold">Green Building</h2>
<%--                <p class="text-center fw-light mb-0">Training Center Management System</p>--%>
<%--                <p class="text-center small text-white-50 mt-3">ECO-FRIENDLY • SUSTAINABLE • INNOVATIVE</p>--%>
            </div>
        </div>
        <div class="col-6">
            <div class="right-panel p-4 d-flex flex-column justify-content-center h-100">
                <div class="login-header mb-4">
                    <h3>Welcome Back</h3>
                    <p class="text-muted small">Sign in to your account</p>
                </div>

                <c:if test="${not empty errorMessage}">
                    <div class="error-message">
                        <i class="fas fa-exclamation-circle"></i>
                        <span>${errorMessage}</span>
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/login" method="post">
                    <div class="mb-4">
                        <label for="username" class="form-label small fw-medium text-muted">USERNAME</label>
                        <div class="input-icon">
                            <input type="text" name="login" class="form-control" id="username" placeholder="Enter your username" required>
                            <i class="fas fa-user"></i>
                        </div>
                    </div>
                    <div class="mb-4">
                        <label for="password" class="form-label small fw-medium text-muted">PASSWORD</label>
                        <div class="input-icon">
                            <input type="password" name="password" class="form-control" id="password" placeholder="Enter your password" required>
                            <i class="fas fa-lock"></i>
                        </div>
                    </div>
                    <button type="submit" class="btn login-btn text-white w-100 mt-2">SIGN IN</button>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap JS Bundle with Popper -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.2/js/bootstrap.bundle.min.js"></script>
</body>
</html>