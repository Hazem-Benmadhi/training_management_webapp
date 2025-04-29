<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${empty sessionScope.user}">
    <c:redirect url="${pageContext.request.contextPath}/login" />
</c:if>

<html>
<head>
    <title>Participants</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" href="css/pageContent.css">
</head>
<body>
<c:if test="${user.role eq 'admin' || user.role eq 'simple'}">
    <div class="participants-container">
        <div class="page-header">
            <h1 class="page-title">Training Participants</h1>
            <button type="button" class="add-btn" data-bs-toggle="modal" data-bs-target="#participantModal" onclick="resetParticipantForm()">
                <i class="fas fa-plus"></i> Add New Participant
            </button>
        </div>

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

        <c:choose>
            <c:when test="${not empty participants}">
                <div class="participants-grid">
                    <c:forEach var="participant" items="${participants}">
                        <div class="participant-card">
                            <div class="participant-header">
                                <h3 class="participant-name">${participant.firstName} ${participant.lastName}</h3>
                                <div class="formations-badge">
                                    <i class="fas fa-book"></i> ${formationCounts[participant.id]}
                                </div>
                            </div>
                            <div class="participant-content">
                                <ul class="participant-info">
                                    <li>
                                        <i class="fas fa-envelope"></i>
                                        <span>${participant.email}</span>
                                    </li>
                                    <li>
                                        <i class="fas fa-phone"></i>
                                        <span>${participant.tel}</span>
                                    </li>
                                </ul>
                                <div class="d-flex">
                                    <div class="structure-badge">
                                        <i class="fas fa-building"></i> ${participant.structure}
                                    </div>
                                    <div class="profile-badge">
                                        <i class="fas fa-id-badge"></i> ${participant.profil.libelle}
                                    </div>
                                </div>
                            </div>
                            <div class="participant-footer">
                                <button onclick="loadParticipantData(${participant.id})" class="action-btn edit-btn"
                                        data-bs-toggle="modal" data-bs-target="#participantModal">
                                    <i class="fas fa-edit"></i>
                                </button>
                                <a href="participants?action=delete&id=${participant.id}" class="action-btn delete-btn">
                                    <i class="fas fa-trash-alt"></i>
                                </a>
                                <a href="participants?action=details&id=${participant.id}" class="action-btn details-btn">
                                    <i class="fas fa-info-circle"></i>
                                </a>
                                <button onclick="setRegistrationParticipantId(${participant.id})" class="register-btn"
                                        data-bs-toggle="modal" data-bs-target="#registrationModal">
                                    <i class="fas fa-user-plus"></i> Register
                                </button>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <i class="fas fa-users"></i>
                    <h4>No Participants Yet</h4>
                    <p>There are no participants available in the system. Click the button below to add your first participant.</p>
                    <button type="button" class="add-btn" data-bs-toggle="modal" data-bs-target="#participantModal" onclick="resetParticipantForm()">
                        <i class="fas fa-plus"></i> Add New Participant
                    </button>
                </div>
            </c:otherwise>
        </c:choose>

        <!-- Registration Modal -->
        <div class="modal fade" id="registrationModal" tabindex="-1" aria-labelledby="registrationModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="registrationModalLabel">Register Participant to Formation</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form id="registrationForm" action="participants" method="post">
                            <input type="hidden" name="action" value="confirmRegister">
                            <input type="hidden" id="registrationParticipantId" name="participantId" value="">
                            <div class="mb-3">
                                <label for="formationSelect" class="form-label">Choose a Formation:</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="fas fa-book"></i></span>
                                    <select class="form-select" name="formationId" id="formationSelect" required>
                                        <c:forEach var="formation" items="${formations}">
                                            <option value="${formation.id}">${formation.title}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" form="registrationForm" class="btn save-btn">Register</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Participant Modal -->
        <div class="modal fade" id="participantModal" tabindex="-1" aria-labelledby="participantModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="participantModalLabel">Add Participant</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form id="participantForm" action="participants" method="post">
                            <input type="hidden" name="action" value="add">
                            <input type="hidden" id="participantId" name="id" value="">

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

                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="structure" class="form-label">Structure</label>
                                    <div class="input-group">
                                        <span class="input-group-text"><i class="fas fa-building"></i></span>
                                        <select name="structure" id="structure" class="form-select">
                                            <option value="CENTRALE">Centrale</option>
                                            <option value="REGIONALE">Régionale</option>
                                        </select>
                                    </div>
                                </div>

                                <div class="col-md-6 mb-3">
                                    <label for="profilSelect" class="form-label">Profil</label>
                                    <div class="input-group">
                                        <span class="input-group-text"><i class="fas fa-id-badge"></i></span>
                                        <select id="profilSelect" class="form-select" name="profilLibelle" onchange="toggleCustomProfile(this.value)" required>
                                            <c:forEach var="p" items="${existingProfiles}">
                                                <option value="${p}">${p}</option>
                                            </c:forEach>
                                            <option value="OTHER">Other</option>
                                        </select>
                                    </div>
                                </div>
                            </div>

                            <div id="customProfilDiv" style="display: none; margin-top: 10px;">
                                <div class="row">
                                    <div class="col-md-12 mb-3">
                                        <label for="customProfilInput" class="form-label">Custom Profile</label>
                                        <div class="input-group">
                                            <span class="input-group-text"><i class="fas fa-pen"></i></span>
                                            <input type="text" class="form-control" name="customProfil" id="customProfilInput" placeholder="Enter custom profile name"/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" form="participantForm" class="btn save-btn">Save Participant</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</c:if>

<script>
    // Function to toggle custom profile field
    function toggleCustomProfile(value) {
        const customDiv = document.getElementById("customProfilDiv");
        const customInput = document.getElementById("customProfilInput");
        if (value === "OTHER") {
            customDiv.style.display = "block";
            customInput.required = true;
        } else {
            customDiv.style.display = "none";
            customInput.required = false;
        }
    }

    // Reset form when adding new participant
    function resetParticipantForm() {
        document.getElementById("participantModalLabel").innerText = "Add Participant";
        document.getElementById("participantForm").reset();
        document.getElementById("participantId").value = "";

        // Update form action
        const formElement = document.getElementById("participantForm");
        formElement.querySelector('input[name="action"]').value = "add";

        toggleCustomProfile(document.getElementById("profilSelect").value);
    }

    // Load participant data for editing
    function loadParticipantData(participantId) {
        document.getElementById("participantModalLabel").innerText = "Edit Participant";

        // Update form action
        const formElement = document.getElementById("participantForm");
        formElement.querySelector('input[name="action"]').value = "add";
        console.log("participant id : " + participantId);

        fetch('participants?action=edit&id=' + participantId)
            .then(response => {
                if (!response.ok) throw new Error('Network response was not ok');
                return response.json();
            })
            .then(data => {
                console.log("data : " , data);
                document.getElementById("participantId").value = data.id;
                document.getElementById("firstName").value = data.firstName;
                document.getElementById("lastName").value = data.lastName;
                document.getElementById("email").value = data.email;
                document.getElementById("tel").value = data.tel;
                document.getElementById("structure").value = data.structure;

                // Handle profile selection
                const isDefaultProfile = Array.from(document.getElementById("profilSelect").options)
                    .some(option => option.value === data.profil.libelle);

                if (isDefaultProfile) {
                    document.getElementById("profilSelect").value = data.profil.libelle;
                    document.getElementById("customProfilInput").value = "";
                } else {
                    document.getElementById("profilSelect").value = "OTHER";
                    document.getElementById("customProfilInput").value = data.profil.libelle;
                }
                toggleCustomProfile(document.getElementById("profilSelect").value);
            })
            .catch(error => {
                console.error('Error loading participant data:', error);
                alert('Failed to load participant details. Please try again.');
            });
    }

    // Initialize when modal is shown
    document.getElementById('participantModal').addEventListener('show.bs.modal', function () {
        toggleCustomProfile(document.getElementById("profilSelect").value);
    });

    // Set participant ID when opening registration modal
    function setRegistrationParticipantId(participantId) {
        document.getElementById('registrationParticipantId').value = participantId;
    }
</script>

</body>
</html>