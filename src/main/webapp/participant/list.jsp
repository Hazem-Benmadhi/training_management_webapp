<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Participant</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <!-- Add these for the modal functionality -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
<div class="container">
    <div class="mt-5" style="text-align: center">
        <h2>List of Participant</h2>
    </div>
    <div class="mt-5">
        <!-- Changed to button triggering modal instead of link -->
        <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#participantModal"
                onclick="resetForm()">Add New Participant</button>
    </div>
    <div class="mt-3">
        <table class="table table-bordered table-hover">
            <thead class="table-dark">
            <tr>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Email</th>
                <th>Telephone</th>
                <th>Formations Count</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="participant" items="${participants}">
                <tr>
                    <td>${participant.firstName}</td>
                    <td>${participant.lastName}</td>
                    <td>${participant.email}</td>
                    <td>${participant.tel}</td>
                    <td>${participant.formations != null ? participant.formations.size() : 0}</td>
                    <td>
                        <button onclick="loadParticipantData(${participant.id})" class="btn btn-success"
                                data-bs-toggle="modal" data-bs-target="#participantModal">Edit</button>
                        <a href="participants?action=delete&id=${participant.id}" class="btn btn-warning">Delete</a>
                        <a href="participants?action=details&id=${participant.id}" class="btn btn-info">Details</a>
                        <button onclick="setRegistrationParticipantId(${participant.id})" class="btn btn-info"
                                data-bs-toggle="modal" data-bs-target="#registrationModal">Register</button>
                    </td>

                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<!-- Registration Modal -->
<div class="modal fade" id="registrationModal" tabindex="-1" aria-labelledby="registrationModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="registrationModalLabel">Register Participant to Formation</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form id="registrationForm" action="participants?action=confirmRegister" method="post">
                    <input type="hidden" id="registrationParticipantId" name="participantId" value="">
                    <div class="mb-3">
                        <label for="formationSelect" class="form-label">Choose a Formation:</label>
                        <select class="form-control" name="formationId" id="formationSelect" required>
                            <c:forEach var="formation" items="${formations}">
                                <option value="${formation.id}">${formation.title}</option>
                            </c:forEach>
                        </select>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                <button type="submit" form="registrationForm" class="btn btn-primary">Register</button>
            </div>
        </div>
    </div>
</div>

<!-- Participant Modal -->
<div class="modal fade" id="participantModal" tabindex="-1" aria-labelledby="participantModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="participantModalLabel">Edit Participant</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form id="participantForm" action="participants" method="post">
                    <input type="hidden" name="action" value="add">
                    <input type="hidden" id="participantId" name="id" value="">

                    <div class="mb-3">
                        <label for="firstName" class="form-label">First Name</label>
                        <input type="text" class="form-control" name="firstName" id="firstName" required>
                    </div>

                    <div class="mb-3">
                        <label for="lastName" class="form-label">Last Name</label>
                        <input type="text" class="form-control" name="lastName" id="lastName" required>
                    </div>

                    <div class="mb-3">
                        <label for="email" class="form-label">Email</label>
                        <input type="text" class="form-control" name="email" id="email" required>
                    </div>

                    <div class="mb-3">
                        <label for="tel" class="form-label">Telephone</label>
                        <input type="text" class="form-control" name="tel" id="tel" required>
                    </div>

                    <div class="mb-3">
                        <label>Structure</label>
                        <select name="structure" id="structure" class="form-control">
                            <option value="CENTRALE">Centrale</option>
                            <option value="REGIONALE">Régionale</option>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label for="profilSelect">Profil:</label>
                        <select id="profilSelect" class="form-control" name="profilLibelle" onchange="toggleCustomProfil(this.value)" required>
                            <c:forEach var="p" items="${defaultProfiles}">
                                <option value="${p}">${p}</option>
                            </c:forEach>
                            <option value="OTHER">Other</option>
                        </select>

                        <div id="customProfilDiv" style="display: none; margin-top: 10px;">
                            <label for="customProfilInput">Enter custom profile:</label>
                            <input type="text" class="form-control" name="customProfil" id="customProfilInput"/>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                <button type="submit" form="participantForm" class="btn btn-primary">Save</button>
            </div>
        </div>
    </div>
</div>

<script>
     // Function to toggle custom profile field
    function toggleCustomProfil(value) {
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
    function resetForm() {
        document.getElementById("participantForm").reset();
        document.getElementById("participantId").value = "";
        document.getElementById("participantModalLabel").innerText = "Add Participant";
        toggleCustomProfil(document.getElementById("profilSelect").value);
    }

    // Load participant data for editing
    function loadParticipantData(participantId) {
        fetch('participants?action=edit&id=' + participantId)
            .then(response => response.json())
            .then(data => {
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
                toggleCustomProfil(document.getElementById("profilSelect").value);

                document.getElementById("participantModalLabel").innerText = "Edit Participant";
            });
    }

    // Initialize when modal is shown
    document.getElementById('participantModal').addEventListener('show.bs.modal', function () {
        toggleCustomProfil(document.getElementById("profilSelect").value);
    });
</script>

<script>
    // Handle registration form submission with AJAX
    document.getElementById('registrationForm').addEventListener('submit', async function(e) {
        e.preventDefault();

        const form = this;
        const submitButton = document.querySelector('button[form="registrationForm"][type="submit"]');
       if (!submitButton) {
            console.error('Submit button not found!');
            return;
        }
        const modal = bootstrap.Modal.getInstance(document.getElementById('registrationModal'));

        try {
            // Disable button during submission
            submitButton.disabled = true;
            submitButton.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Registering...';

            // Send AJAX request
            const response = await fetch(form.action, {
                method: 'POST',
                body: new FormData(form),
                headers: {
                    'X-Requested-With': 'XMLHttpRequest' // Identify as AJAX request
                }
            });

            const data = await response.json();

            if (!response.ok) throw new Error(data.message || 'Registration failed');

            // Show success message
            const result = await response.json();
            if (result.success) {
                alert('Registration successful!');
                modal.hide();  // Close modal and refresh the participants list
                window.location.reload(); // Refresh on success
            } else {
                alert(result.message || 'Registration failed');
            }
        } catch (error) {
            console.error('Error:', error);
            alert('Error: ' + error.message);
        } finally {
            // Re-enable button
            submitButton.disabled = false;
            submitButton.textContent = 'Register';
        }
    });

    // Set participant ID when opening modal
    function setRegistrationParticipantId(participantId) {
        document.getElementById('registrationParticipantId').value = participantId;
    }
</script>

</body>
</html>