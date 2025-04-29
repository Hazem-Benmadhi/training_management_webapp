<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${empty sessionScope.user}">
    <c:redirect url="${pageContext.request.contextPath}/login" />
</c:if>

<html>
<head>
    <title>Participant Details</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" href="css/pageContent.css">
    <style>
        .details-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            gap: 25px;
            margin-top: 25px;
        }

        .participant-info-card {
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            overflow: hidden;
            margin-bottom: 30px;
            background: white;
        }

        .participant-header {
            background: #2e7d32;
            color: white;
            padding: 20px;
            position: relative;
        }

        .participant-name {
            font-size: 2rem;
            margin-bottom: 5px;
        }

        .participant-email {
            opacity: 0.8;
            font-size: 1rem;
        }

        .participant-badges {
            display: flex;
            gap: 10px;
            margin-top: 15px;
        }

        .participant-badge {
            background: rgba(255,255,255,0.2);
            padding: 5px 12px;
            border-radius: 20px;
            font-size: 0.9rem;
        }

        .participant-details {
            padding: 20px;
        }

        .contact-info {
            display: flex;
            gap: 20px;
            margin-bottom: 20px;
        }

        .contact-item {
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .contact-item i {
            color: #2e7d32;
            font-size: 1.2rem;
        }

        .formations-section {
            margin-top: 30px;
        }

        .formations-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 15px;
            padding-bottom: 10px;
            border-bottom: 1px solid #eee;
        }

        .formations-count {
            background: #2e7d32;
            color: white;
            padding: 5px 15px;
            border-radius: 20px;
            font-weight: bold;
        }

        .formation-card {
            height: 150px;
            width: 400px;
            background: #f9f9f9;
            border-radius: 8px;
            padding: 15px;
            margin-bottom: 15px;
            position: relative;
            border-left: 4px solid #2e7d32;
        }

        .formation-title {
            font-weight: bold;
            margin-bottom: 5px;
            font-size: 1.1rem;
        }

        .formation-dates {
            color: #666;
            font-size: 0.9rem;
            margin-bottom: 10px;
        }

        .formation-meta {
            display: flex;
            gap: 15px;
            margin-top: 10px;
            font-size: 0.9rem;
        }

        .formation-meta-item {
            display: flex;
            align-items: center;
            gap: 5px;
        }

        .formation-meta-item i {
            color: #2e7d32;
        }

        .unregister-btn {
            position: absolute;
            top: 15px;
            right: 15px;
            background: #ff3d71;
            color: white;
            border: none;
            border-radius: 5px;
            padding: 5px 10px;
            font-size: 0.8rem;
            cursor: pointer;
            transition: all 0.2s;
        }

        .unregister-btn:hover {
            background: #ff2160;
            transform: scale(1.05);
        }

        .back-btn {
            background: transparent;
            border: 1px solid #2e7d32;
            color: #2e7d32;
            padding: 8px 20px;
            border-radius: 5px;
            cursor: pointer;
            transition: all 0.2s;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 8px;
            margin-bottom: 20px;
        }

        .back-btn:hover {
            background: #f0f2ff;
        }

        .empty-formation {
            text-align: center;
            padding: 40px 20px;
            background: #f9f9f9;
            border-radius: 8px;
        }

        .empty-formation i {
            font-size: 3rem;
            color: #ccc;
            margin-bottom: 15px;
        }

        .empty-formation h4 {
            color: #666;
            margin-bottom: 10px;
        }

        .empty-formation p {
            color: #888;
            max-width: 400px;
            margin: 0 auto;
        }

        /* Confirmation modal styling */
        .modal-confirm .modal-header {
            border: none;
            padding-bottom: 0;
        }

        .modal-confirm .modal-body {
            text-align: center;
            padding: 30px 25px;
        }

        .modal-confirm .modal-body i {
            font-size: 50px;
            color: #ff3d71;
            margin-bottom: 20px;
            display: block;
        }

        .modal-confirm .btn-danger {
            background: #ff3d71;
            border-color: #ff3d71;
        }

        .modal-confirm .btn-danger:hover {
            background: #ff2160;
            border-color: #ff2160;
        }
    </style>
</head>
<body>
<c:if test="${user.role eq 'admin' || user.role eq 'simple'}">
    <div class="container mt-4">
        <a href="participants" class="back-btn">
            <i class="fas fa-arrow-left"></i> Back to Participants
        </a>

        <!-- Success and Error alerts -->
        <c:if test="${not empty successMessage}">
            <div class="alert alert-custom alert-success alert-dismissible fade show" role="alert">
                <i class="fas fa-check-circle"></i>
                    ${successMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <c:remove var="successMessage" scope="session"/>
        </c:if>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-custom alert-danger alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-circle"></i>
                    ${errorMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <c:remove var="errorMessage" scope="session"/>
        </c:if>

        <div class="participant-info-card">
            <div class="participant-header">
                <h1 class="participant-name">${participant.firstName} ${participant.lastName}</h1>
                <div class="participant-email">${participant.email}</div>
                <div class="participant-badges">
                    <div class="participant-badge">
                        <i class="fas fa-building"></i> ${participant.structure}
                    </div>
                    <div class="participant-badge">
                        <i class="fas fa-id-badge"></i> ${participant.profil.libelle}
                    </div>
                </div>
            </div>
            <div class="participant-details">
                <div class="contact-info">
                    <div class="contact-item">
                        <i class="fas fa-phone"></i>
                        <span>${participant.tel}</span>
                    </div>
                    <div class="contact-item">
                        <i class="fas fa-envelope"></i>
                        <span>${participant.email}</span>
                    </div>
                </div>

                <div class="formations-section">
                    <div class="formations-header">
                        <h2>Enrolled Formations</h2>
                        <div class="formations-count">
                            <i class="fas fa-book"></i> ${enrolledFormations.size()}
                        </div>
                    </div>
                    <div class="details-grid">
                        <c:choose>
                            <c:when test="${not empty enrolledFormations}">
                                <c:forEach var="formation" items="${enrolledFormations}">
                                    <div class="formation-card">
                                        <h3 class="formation-title">${formation.title}</h3>
                                        <div class="formation-dates">
                                            start : <i class="far fa-calendar-alt"></i> ${formation.date}<br>
                                            end : <i class="far fa-calendar-alt"></i>${formation.endDate}
                                        </div>
                                        <div class="formation-meta">
                                            <div class="formation-meta-item">
                                                <i class="fas fa-user-tie"></i>${formation.trainer.firstName}, ${formation.trainer.lastName}, ${formation.trainer.email}
                                            </div>
                                        </div>
                                        <button class="unregister-btn" onclick="showUnregisterConfirmation(${participant.id}, ${formation.id})">
                                            <i class="fas fa-user-minus"></i> Unregister
                                        </button>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div class="empty-formation">
                                    <i class="fas fa-book-open"></i>
                                    <h4>No Formations Yet</h4>
                                    <p>This participant is not enrolled in any training formation yet.</p>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>

                </div>
            </div>
        </div>
    </div>

    <!-- Unregister Confirmation Modal -->
    <div class="modal fade modal-confirm" id="unregisterModal" tabindex="-1" aria-labelledby="unregisterModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="unregisterModalLabel">Confirm Unregistration</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <i class="fas fa-exclamation-triangle"></i>
                    <p>Are you sure you want to unregister this participant from the formation?</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <a href="" id="confirmUnregisterBtn" class="btn btn-danger">Unregister</a>
                </div>
            </div>
        </div>
    </div>
</c:if>

<script>
    function showUnregisterConfirmation(participantId, formationId) {
        document.getElementById('confirmUnregisterBtn').href = "participants?action=unregister&participantId=" + participantId + "&formationId=" + formationId;
        new bootstrap.Modal(document.getElementById('unregisterModal')).show();
    }
</script>
</body>
</html>