<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${empty sessionScope.user}">
  <c:redirect url="${pageContext.request.contextPath}/login" />
</c:if>

<html>
<head>
  <title>Formations</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="css/pageContent.css">
</head>
<body>
<c:if test="${user.role eq 'admin' || user.role eq 'simple'}">
  <div class="formations-container">
    <div class="page-header">
      <h1 class="page-title">Training Formations</h1>
      <button type="button" class="add-btn" data-bs-toggle="modal" data-bs-target="#formationModal" onclick="resetFormationForm()">
        <i class="fas fa-plus"></i> Add New Formation
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
      <c:when test="${not empty formations}">
        <div class="formations-grid">
          <c:forEach var="formation" items="${formations}">
            <div class="formation-card">
              <div class="formation-header">
                <h3 class="formation-title">${formation.title}</h3>
              </div>
              <div class="formation-content">
                <ul class="formation-info">
                  <li>
                    <i class="fas fa-calendar-alt"></i>
                    <span>Date: ${formation.date}</span>
                  </li>
                  <li>
                    <i class="fas fa-clock"></i>
                    <span>Duration: ${formation.duration} days</span>
                  </li>
                  <li>
                    <i class="fas fa-money-bill-wave"></i>
                    <span>Budget: <span class="budget-value">$${formation.budget}</span></span>
                  </li>
                  <li>
                    <i class="fas fa-chalkboard-teacher"></i>
                    <span>Trainer: ${formation.trainer.firstName}</span>
                  </li>
                </ul>
                <div class="domain-badge">
                  <i class="fas fa-sitemap"></i> ${formation.domain.libelle}
                </div>
              </div>
              <div class="formation-footer">
                <button onclick="loadFormationData(${formation.id})" class="action-btn edit-btn" data-bs-toggle="modal" data-bs-target="#formationModal">
                  <i class="fas fa-edit"></i>
                </button>
                <a href="formations?action=delete&id=${formation.id}" class="action-btn delete-btn">
                  <i class="fas fa-trash-alt"></i>
                </a>
              </div>
            </div>
          </c:forEach>
        </div>
      </c:when>
      <c:otherwise>
        <div class="empty-state">
          <i class="fas fa-book"></i>
          <h4>No Formations Yet</h4>
          <p>There are no training formations available in the system. Click the button below to create your first formation.</p>
          <button type="button" class="add-btn" data-bs-toggle="modal" data-bs-target="#formationModal" onclick="resetFormationForm()">
            <i class="fas fa-plus"></i> Add New Formation
          </button>
        </div>
      </c:otherwise>
    </c:choose>

    <!-- Formation Modal -->
    <div class="modal fade" id="formationModal" tabindex="-1" aria-labelledby="formationModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="formationModalLabel">Add Formation</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <form id="formationForm" action="formations" method="post">
              <input type="hidden" name="action" value="add">
              <input type="hidden" id="formationId" name="id" value="">

              <div class="row">
                <div class="col-md-12 mb-3">
                  <label for="title" class="form-label">Formation Title</label>
                  <div class="input-group">
                    <span class="input-group-text"><i class="fas fa-heading"></i></span>
                    <input type="text" class="form-control" name="title" id="title" placeholder="Enter formation title" required>
                  </div>
                </div>
              </div>

              <div class="row">
                <div class="col-md-6 mb-3">
                  <label for="date" class="form-label">Formation Date</label>
                  <div class="input-group">
                    <span class="input-group-text"><i class="fas fa-calendar"></i></span>
                    <input type="date" class="form-control" name="date" id="date" required>
                  </div>
                </div>

                <div class="col-md-6 mb-3">
                  <label for="duration" class="form-label">Duration (days)</label>
                  <div class="input-group">
                    <span class="input-group-text"><i class="fas fa-hourglass-half"></i></span>
                    <input type="number" class="form-control" name="duration" id="duration" min="1" placeholder="Enter duration in days" required>
                  </div>
                </div>
              </div>

              <div class="row">
                <div class="col-md-6 mb-3">
                  <label for="budget" class="form-label">Budget</label>
                  <div class="input-group">
                    <span class="input-group-text"><i class="fas fa-dollar-sign"></i></span>
                    <input type="number" class="form-control" name="budget" id="budget" step="0.01" min="0" placeholder="Enter budget amount" required>
                  </div>
                </div>

                <div class="col-md-6 mb-3">
                  <label for="idDomain" class="form-label">Domain</label>
                  <div class="input-group">
                    <span class="input-group-text"><i class="fas fa-sitemap"></i></span>
                    <select class="form-select" name="idDomain" id="idDomain" required>
                      <c:choose>
                        <c:when test="${not empty domains}">
                          <c:forEach var="domain" items="${domains}">
                            <option value="${domain.id}" ${not empty formation.iddomain and domain.id == formation.iddomain.id ? 'selected' : ''}>
                                ${domain.libelle}
                            </option>
                          </c:forEach>
                        </c:when>
                        <c:otherwise>
                          <option disabled>No domains available</option>
                        </c:otherwise>
                      </c:choose>
                    </select>
                  </div>
                </div>
              </div>

              <div class="row">
                <div class="col-md-12 mb-3">
                  <label for="idTrainer" class="form-label">Trainer</label>
                  <div class="input-group">
                    <span class="input-group-text"><i class="fas fa-chalkboard-teacher"></i></span>
                    <select class="form-select" name="idTrainer" id="idTrainer" required>
                      <c:choose>
                        <c:when test="${not empty trainers}">
                          <c:forEach var="trainer" items="${trainers}">
                            <option value="${trainer.id}"
                              ${not empty formation.idtrainer and trainer.id == formation.idtrainer.id ? 'selected' : ''}>
                                ${trainer.firstName}
                            </option>
                          </c:forEach>
                        </c:when>
                        <c:otherwise>
                          <option disabled>No Trainer available</option>
                        </c:otherwise>
                      </c:choose>
                    </select>
                  </div>
                </div>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancel</button>
            <button type="submit" form="formationForm" class="btn save-btn">Save Formation</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</c:if>

<script>
  // Reset form for new formation
  function resetFormationForm() {
    document.getElementById("formationModalLabel").innerText = "Add Formation";
    document.getElementById("formationForm").reset();
    document.getElementById("formationId").value = "";
    document.getElementById('date').value = new Date().toISOString().split('T')[0];

    // Update form action
    const formElement = document.getElementById("formationForm");
    formElement.querySelector('input[name="action"]').value = "add";
  }

  function loadFormationData(formationId) {
    fetch('formations?action=edit&id=' + formationId)
            .then(response => {
              if (!response.ok) throw new Error('Network response was not ok');
              return response.json();
            })
            .then(data => {
              document.getElementById("formationId").value = data.id;
              document.getElementById("title").value = data.title;
              document.getElementById("date").value = data.date;
              document.getElementById("duration").value = data.duration;
              document.getElementById("budget").value = data.budget;

              // Set domain if available
              const domainSelect = document.getElementById("idDomain");
              if (data.domain && data.domain.id) {
                domainSelect.value = data.domain.id;
              }

              // Set trainer if available
              const trainerSelect = document.getElementById("idTrainer");
              if (data.trainer && data.trainer.id) {
                trainerSelect.value = data.trainer.id;
              }

              // Update form action
              const formElement = document.getElementById("formationForm");
              formElement.querySelector('input[name="action"]').value = "update";

              document.getElementById("formationModalLabel").innerText = "Edit Formation";
            })
            .catch(error => {
              console.error('Error loading formation data:', error);
              alert('Failed to load formation details. Please try again.');
            });
  }
</script>
</body>
</html>