<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>Trainer Form</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">

  <script>
    function toggleEmployerInput() {
      const employerSelect = document.getElementById('employerSelect');
      const newEmployerDiv = document.getElementById('newEmployerDiv');
      newEmployerDiv.style.display = employerSelect.value === 'new' ? 'block' : 'none';
    }
  </script>

</head>
<body>
<div class="container mt-5">
  <div>
    <h2> <c:choose>
      <c:when test="${not empty trainer}">Edit</c:when>
      <c:otherwise>Create</c:otherwise>
    </c:choose> Trainer</h2>
  </div>
  <div class="m-3">
    <form action="trainers" method="post">
      <input type="hidden" name="action" value="add">
      <c:if test="${not empty trainer}">
        <input type="hidden" name="id" value="${trainer.id}">
      </c:if>

      <div class="mb-3">
        <label for="firstName" class="form-label">First Name</label>
        <input type="text" value="${trainer.firstName}" class="form-control" name="firstName" id="firstName" required>
      </div>

      <div class="mb-3">
        <label for="lastName" class="form-label">Last Name</label>
        <input type="text" value="${trainer.lastName}" class="form-control" name="lastName" id="lastName" required>
      </div>

      <div class="mb-3">
        <label for="email" class="form-label">Email</label>
        <input type="text" value="${trainer.email}" class="form-control" name="email" id="email" required>
      </div>

      <div class="mb-3">
        <label for="tel" class="form-label">Telephone</label>
        <input type="text" value="${trainer.tel}" class="form-control" name="tel" id="tel" required>
      </div>

<%--      <div class="mb-3">--%>
<%--        <label>Trainer Type:</label>--%>
<%--        <select name="type" class="form-control" required>--%>
<%--          <c:forEach var="type" items="${trainerType}">--%>
<%--            <option value="${type}" <c:if test="${type == trainer.type}">selected</c:if>>${type}</option>--%>
<%--          </c:forEach>--%>
<%--        </select>--%>
<%--      </div>--%>

<%--      <div class="mb-3">--%>
<%--        <label>Employer (only if EXTERNE):</label>--%>
<%--        <select name="employerId" class="form-control">--%>
<%--          <option value="">-- None --</option>--%>
<%--          <c:forEach var="employer" items="${employers}">--%>
<%--            <option value="${employer.id}" <c:if test="${trainer.employer != null && employer.id == trainer.employer.id}">selected</c:if>>--%>
<%--                ${employer.nameEmployer}--%>
<%--            </option>--%>
<%--          </c:forEach>--%>
<%--        </select>--%>
<%--      </div>--%>

      <div class="mb-3">
        <label for="type" class="form-label">Type</label>
        <select class="form-select" id="type" name="type" required onchange="toggleEmployerField()">
          <option value="INTERNE" ${trainer.type == 'INTERNE' ? 'selected' : ''}>INTERNE</option>
          <option value="EXTERNE" ${trainer.type == 'EXTERNE' ? 'selected' : ''}>EXTERNE</option>
        </select>
      </div>

      <div class="mb-3" id="employerSection" style="${trainer.type == 'EXTERNE' ? '' : 'display:none'}">
        <label for="employerSelect" class="form-label">Employer</label>
        <select class="form-select" id="employerSelect" name="employerId" onchange="toggleEmployerInput()">
          <option value="">-- select employer --</option>
          <c:forEach var="employer" items="${employers}">
            <option value="${employer.id}" ${trainer.employer != null && trainer.employer.id == employer.id ? 'selected' : ''}>
                ${employer.nameEmployer}
            </option>
          </c:forEach>
          <option value="new">other (add new employer)</option>
        </select>
      </div>

      <div class="mb-3" id="newEmployerDiv" style="display:none;">
        <label for="newEmployerName" class="form-label">New employer name</label>
        <input type="text" class="form-control" id="newEmployerName" name="newEmployerName">
      </div>


      <button type="submit" class="btn btn-danger">Save</button>
      <a href="trainers?action=list" class="btn btn-secondary">Cancel</a>
    </form>
  </div>
</div>

<script>
  function toggleEmployerField() {
    const typeSelect = document.getElementById('type');
    const employerSection = document.getElementById('employerSection');
    const isExternal = typeSelect.value === 'EXTERNE';
    employerSection.style.display = isExternal ? 'block' : 'none';
  }
  toggleEmployerField();
</script>

</body>
</html>
