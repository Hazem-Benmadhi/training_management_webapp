<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${empty sessionScope.user}">
    <c:redirect url="${pageContext.request.contextPath}/login" />
</c:if>

<html>
<head>
    <title>Domains</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="css/pageContent.css">
</head>
<body>
<c:if test="${user.role eq 'admin'}">
    <div class="domains-container">
        <div class="page-header">
            <h1 class="page-title">Training Domains</h1>
            <button type="button" class="add-btn" data-bs-toggle="modal" data-bs-target="#domainModal" onclick="resetDomainForm()">
                <i class="fas fa-plus"></i> Add New Domain
            </button>
        </div>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-custom alert-danger alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-circle"></i>
                    ${errorMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <c:remove var="errorMessage" scope="session"/>
        </c:if>

        <c:choose>
            <c:when test="${not empty domains}">
                <div class="domains-grid">
                    <c:forEach var="domain" items="${domains}">
                        <div class="domain-card">
                            <div class="domain-header">
                                <h3 class="domain-title">${domain.libelle}</h3>
                            </div>
                            <div class="domain-content">
                                <ul class="domain-info">
                                    <li>
                                        <i class="fas fa-id-card"></i>
                                        <span>Domain ID: ${domain.id}</span>
                                    </li>
                                    <li>
                                        <i class="fas fa-sitemap"></i>
                                        <span>Category: Training Domain</span>
                                    </li>
                                </ul>
                                <div class="domain-badge">
                                    <i class="fas fa-tag"></i> Domain
                                </div>
                            </div>
                            <div class="domain-footer">
                                <a href="domains?action=delete&id=${domain.id}" class="action-btn delete-btn">
                                    <i class="fas fa-trash-alt"></i>
                                </a>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <i class="fas fa-sitemap"></i>
                    <h4>No Domains Yet</h4>
                    <p>There are no training domains available in the system. Click the button below to create your first domain.</p>
                    <button type="button" class="add-btn" data-bs-toggle="modal" data-bs-target="#domainModal" onclick="resetDomainForm()">
                        <i class="fas fa-plus"></i> Add New Domain
                    </button>
                </div>
            </c:otherwise>
        </c:choose>

        <!-- Domain Modal -->
        <div class="modal fade" id="domainModal" tabindex="-1" aria-labelledby="domainModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="domainModalLabel">Add Domain</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form id="domainForm" action="domains" method="post">
                            <input type="hidden" name="action" value="add">
                            <div class="mb-3">
                                <label for="libelle" class="form-label">Domain Name</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="fas fa-sitemap"></i></span>
                                    <input type="text" class="form-control" name="libelle" id="libelle" placeholder="Enter domain name" required>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" form="domainForm" class="btn save-btn">Save Domain</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</c:if>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function resetDomainForm() {
        document.getElementById("domainModalLabel").innerText = "Add Domain";
        document.getElementById("domainForm").reset();
    }
</script>
</body>
</html>