<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${empty sessionScope.user}">
    <c:redirect url="${pageContext.request.contextPath}/login" />
</c:if>

<html>
<head>
    <title>Trainers</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="css/pageContent.css">
</head>
<body>
<c:if test="${user.role eq 'admin' || user.role eq 'simple'}">
    <div class="trainers-container">
        <div class="page-header">
            <h1 class="page-title">Training Instructors</h1>
            <button type="button" class="add-btn" data-bs-toggle="modal" data-bs-target="#trainerModal" onclick="resetTrainerForm()">
                <i class="fas fa-plus"></i> Add New Trainer
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
            <c:when test="${not empty trainers}">
                <div class="trainers-grid">
                    <c:forEach var="trainer" items="${trainers}">
                        <div class="trainer-card">
                            <div class="trainer-header">
                                <h3 class="trainer-title">${trainer.firstName} ${trainer.lastName}</h3>
                            </div>
                            <div class="trainer-content">
                                <ul class="trainer-info">
                                    <li>
                                        <i class="fas fa-envelope"></i>
                                        <span>${trainer.email}</span>
                                    </li>
                                    <li>
                                        <i class="fas fa-phone"></i>
                                        <span>${trainer.tel}</span>
                                    </li>
                                    <li>
                                        <i class="fas fa-id-badge"></i>
                                        <span>ID: ${trainer.id}</span>
                                    </li>
                                    <li>
                                        <i class="fas fa-building"></i>
                                        <span>Employer:
                    <c:choose>
                        <c:when test="${not empty trainer.employer}">
                            ${trainer.employer.nameEmployer}
                        </c:when>
                        <c:otherwise>
                            <em>None</em>
                        </c:otherwise>
                    </c:choose>
                  </span>
                                    </li>
                                </ul>
                                <div class="type-badge ${trainer.type eq 'INTERNE' ? 'type-interne' : 'type-externe'}">
                                    <i class="fas fa-${trainer.type eq 'INTERNE' ? 'user-tie' : 'user-friends'}"></i> ${trainer.type}
                                </div>
                            </div>
                            <div class="trainer-footer">
                                <button onclick="loadTrainerData(${trainer.id})" class="action-btn edit-btn" data-bs-toggle="modal" data-bs-target="#trainerModal">
                                    <i class="fas fa-edit"></i>
                                </button>
                                <a href="trainers?action=delete&id=${trainer.id}" class="action-btn delete-btn">
                                    <i class="fas fa-trash-alt"></i>
                                </a>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <i class="fas fa-chalkboard-teacher"></i>
                    <h4>No Trainers Yet</h4>
                    <p>There are no trainers available in the system. Click the button below to add your first trainer.</p>
                    <button type="button" class="add-btn" data-bs-toggle="modal" data-bs-target="#trainerModal" onclick="resetTrainerForm()">
                        <i class="fas fa-plus"></i> Add New Trainer
                    </button>
                </div>
            </c:otherwise>
        </c:choose>

        <!-- Trainer Modal -->
        <div class="modal fade" id="trainerModal" tabindex="-1" aria-labelledby="trainerModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="trainerModalLabel">Add Trainer</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form id="trainerForm" action="trainers" method="post">
                            <input type="hidden" name="action" value="add">
                            <input type="hidden" id="trainerId" name="id" value="">

                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="firstName" class="form-label">First Name</label>
                                    <div class="input-group">
                                        <span class="input-group-text"><i class="fas fa-user"></i></span>
                                        <input type="text" class="form-control" name="firstName" id="firstName" placeholder="Enter first name" required>
                                    </div>
                                </div>

                                <div class="col-md-6 mb-3">
                                    <label for="lastName" class="form-label">Last Name</label>
                                    <div class="input-group">
                                        <span class="input-group-text"><i class="fas fa-user"></i></span>
                                        <input type="text" class="form-control" name="lastName" id="lastName" placeholder="Enter last name" required>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="email" class="form-label">Email</label>
                                    <div class="input-group">
                                        <span class="input-group-text"><i class="fas fa-envelope"></i></span>
                                        <input type="email" class="form-control" name="email" id="email" placeholder="Enter email address" required>
                                    </div>
                                </div>

                                <div class="col-md-6 mb-3">
                                    <label for="tel" class="form-label">Telephone</label>
                                    <div class="input-group">
                                        <span class="input-group-text"><i class="fas fa-phone"></i></span>
                                        <input type="tel" class="form-control" name="tel" id="tel" placeholder="Enter phone number" required>
                                    </div>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="type" class="form-label">Type</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="fas fa-user-tag"></i></span>
                                    <select class="form-select" id="type" name="type" required onchange="toggleEmployerField()">
                                        <option value="INTERNE" ${trainer.type == 'INTERNE' ? 'selected' : ''}>INTERNE</option>
                                        <option value="EXTERNE" ${trainer.type == 'EXTERNE' ? 'selected' : ''}>EXTERNE</option>
                                    </select>
                                </div>
                            </div>

                            <div class="mb-3" id="employerSection" style="display: none;">
                                <label for="employerSelect" class="form-label">Employer</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="fas fa-building"></i></span>
                                    <select class="form-select" id="employerSelect" name="employerId">
                                        <option value="">-- select employer --</option>
                                        <c:forEach var="employer" items="${employers}">
                                            <option value="${employer.id}">${employer.nameEmployer}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" form="trainerForm" class="btn save-btn">Save Trainer</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</c:if>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Function to toggle employer field based on trainer type
    function toggleEmployerField() {
        const typeSelect = document.getElementById('type');
        const employerSection = document.getElementById('employerSection');
        employerSection.style.display = typeSelect.value === 'EXTERNE' ? 'block' : 'none';
    }

    // Reset form for new trainer
    function resetTrainerForm() {
        document.getElementById("trainerModalLabel").innerText = "Add Trainer";
        document.getElementById("trainerForm").reset();
        document.getElementById("trainerId").value = "";
        document.getElementById("employerSection").style.display = "none";

        // Update form action
        const formElement = document.getElementById("trainerForm");
        formElement.querySelector('input[name="action"]').value = "add";
    }

    // Load trainer data for editing
    function loadTrainerData(trainerId) {
        document.getElementById("trainerModalLabel").innerText = "Edit Trainer";

        fetch('trainers?action=edit&id=' + trainerId)
            .then(response => {
                if (!response.ok) throw new Error('Network response was not ok');
                return response.json();
            })
            .then(data => {
                document.getElementById("trainerId").value = data.id;
                document.getElementById("firstName").value = data.firstName;
                document.getElementById("lastName").value = data.lastName;
                document.getElementById("email").value = data.email;
                document.getElementById("tel").value = data.tel;
                document.getElementById("type").value = data.type;

                if (data.employer) {
                    document.getElementById("employerSelect").value = data.employer.id;
                }

                // Update form action
                const formElement = document.getElementById("trainerForm");
                formElement.querySelector('input[name="action"]').value = "add";

                toggleEmployerField();
            })
            .catch(error => {
                console.error('Error loading trainer data:', error);
                alert('Failed to load trainer details. Please try again.');
            });
    }

    // Initialize when modal is shown
    document.getElementById('trainerModal').addEventListener('show.bs.modal', function () {
        toggleEmployerField();
    });
</script>
</body>
</html>