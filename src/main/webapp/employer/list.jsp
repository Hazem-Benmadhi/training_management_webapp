<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${empty sessionScope.user}">
  <c:redirect url="${pageContext.request.contextPath}/login" />
</c:if>

<html>
<head>
  <title>Employer Network</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
  <style>
    :root {
      --primary-color: #4caf50;
      --accent-color: #ff6e40;
      --light-bg: #f5f7fa;
    }

    body {
      background-color: var(--light-bg);
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    }

    .page-header {
      background: linear-gradient(135deg, var(--primary-color), #2e7d32);
      color: white;
      padding: 2rem 0;
      border-radius: 0 0 15px 15px;
      box-shadow: 0 4px 12px rgba(0,0,0,0.1);
      margin-bottom: 2rem;
    }

    .employer-card {
      background-color: white;
      border-radius: 12px;
      box-shadow: 0 4px 8px rgba(0,0,0,0.05);
      transition: transform 0.3s, box-shadow 0.3s;
    }

    .employer-card:hover {
      transform: translateY(-5px);
      box-shadow: 0 6px 12px rgba(0,0,0,0.1);
    }

    .employer-id {
      background-color: var(--primary-color);
      color: white;
      width: 40px;
      height: 40px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-weight: bold;
    }

    .add-button {
      background-color: var(--accent-color);
      border: none;
      border-radius: 50px;
      padding: 0.8rem 1.5rem;
      font-weight: 600;
      box-shadow: 0 4px 8px rgba(255,110,64,0.3);
      transition: transform 0.2s, box-shadow 0.2s;
    }

    .add-button:hover {
      background-color: #ff5722;
      transform: translateY(-2px);
      box-shadow: 0 6px 12px rgba(255,110,64,0.4);
    }

    .delete-btn {
      color: #ef5350;
      border: 1px solid #ef5350;
      border-radius: 50%;
      width: 36px;
      height: 36px;
      display: flex;
      align-items: center;
      justify-content: center;
      transition: all 0.2s;
    }

    .delete-btn:hover {
      background-color: #ef5350;
      color: white;
    }

    .modal-content {
      border-radius: 15px;
    }

    .modal-header {
    background: linear-gradient(135deg, var(--primary-color), #2e7d32);
      color: white;
      border-radius: 15px 15px 0 0;
    }

    .empty-state {
      padding: 5rem 0;
      text-align: center;
      color: #9e9e9e;
    }

    .section-title {
      position: relative;
      display: inline-block;
      margin-bottom: 2rem;
    }

    .section-title:after {
      content: '';
      position: absolute;
      bottom: -10px;
      left: 0;
      width: 60px;
      height: 4px;
      background-color: var(--accent-color);
      border-radius: 2px;
    }

    .animated-count {
      font-size: 1.5rem;
      font-weight: bold;
      color: var(--primary-color);
    }
  </style>
</head>
<body>
<c:if test="${user.role eq 'admin'}">
  <!-- Header Section -->
  <div class="page-header">
    <div class="container">
      <div class="row align-items-center">
        <div class="col-md-8">
          <h1><i class="fas fa-building me-2"></i>Employer Network</h1>
        </div>
        <div class="col-md-4 text-md-end">
          <div class="d-flex justify-content-md-end">
            <div class="me-4 text-center">
              <div class="animated-count">${employers.size()}</div>
              <div>Employers</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- Main Content -->
  <div class="container">
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h2 class="section-title">Employer Directory</h2>
      <button type="button" class="add-button" data-bs-toggle="modal" data-bs-target="#employerModal">
        <i class="fas fa-plus me-2"></i> Add Employer
      </button>
    </div>

    <!-- Card Layout -->
    <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4">
      <c:choose>
        <c:when test="${not empty employers}">
          <c:forEach var="employer" items="${employers}">
            <div class="col">
              <div class="employer-card p-4">
                <div class="d-flex justify-content-between align-items-center mb-3">
                  <div class="employer-id">${employer.id}</div>
                  <a href="employers?action=delete&id=${employer.id}" class="delete-btn"
                     onclick="return confirm('Are you sure you want to delete this employer?')">
                    <i class="fas fa-trash-alt"></i>
                  </a>
                </div>
                <h4 class="mb-1">${employer.nameEmployer}</h4>
                <div class="mt-3 d-flex justify-content-end">
                  <button class="btn btn-sm btn-outline-primary me-2"
                          onclick="editEmployer(${employer.id}, '${employer.nameEmployer}')">
                    <i class="fas fa-edit me-1"></i> Edit
                  </button>
                  <button class="btn btn-sm btn-outline-info">
                    <i class="fas fa-info-circle me-1"></i> Details
                  </button>
                </div>
              </div>
            </div>
          </c:forEach>
        </c:when>
        <c:otherwise>
          <div class="col-12">
            <div class="empty-state">
              <i class="fas fa-building mb-3" style="font-size: 3rem;"></i>
              <h3>No employers found</h3>
              <p>Start adding employers to your database</p>
            </div>
          </div>
        </c:otherwise>
      </c:choose>
    </div>
  </div>

  <!-- Add/Edit Employer Modal -->
  <div class="modal fade" id="employerModal" tabindex="-1" aria-labelledby="employerModalLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="employerModalLabel">
            <i class="fas fa-building me-2"></i> <span id="modalAction">Add</span> Employer
          </h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <form id="employerForm" action="employers" method="post">
            <input type="hidden" name="action" value="add" id="formAction">
            <input type="hidden" id="employerId" name="id" value="">

            <div class="mb-3">
              <label for="employerName" class="form-label">Employer Name</label>
              <div class="input-group">
                <span class="input-group-text"><i class="fas fa-building"></i></span>
                <input type="text" class="form-control" name="employerName" id="employerName"
                       placeholder="Enter employer name" required>
              </div>
            </div>
          </form>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">
            <i class="fas fa-times me-1"></i> Cancel
          </button>
          <button type="submit" form="employerForm" class="btn btn-primary">
            <i class="fas fa-save me-1"></i> Save
          </button>
        </div>
      </div>
    </div>
  </div>
</c:if>

  <!-- JavaScript for Modal Edit Functionality -->
  <script>
    function editEmployer(id, name) {
      document.getElementById('modalAction').textContent = 'Edit';
      document.getElementById('formAction').value = 'update';
      document.getElementById('employerId').value = id;
      document.getElementById('employerName').value = name;

      // Show the modal
      var myModal = new bootstrap.Modal(document.getElementById('employerModal'));
      myModal.show();
    }
  </script>

  <!-- Bootstrap JS -->
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
