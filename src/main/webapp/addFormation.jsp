<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Formation Form</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body>
<div class="container mt-5">
  <div>
    <h2> <c:choose>
      <c:when test="${not empty formation}">Edit</c:when>
      <c:otherwise>Create</c:otherwise>
    </c:choose> Formation</h2>
  </div>
  <div class="m-3">
    <form action="formations" method="post">
      <input type="hidden" name="action" value="add">
      <c:if test="${not empty formation}">
        <input type="hidden" name="id" value="${formation.id}">
      </c:if>

      <!-- Formation Title -->
      <div class="mb-3">
        <label for="title" class="form-label">Formation Title</label>
        <input type="text" value="${formation.title}" class="form-control" name="title" id="title" required>
      </div>

      <!-- Formation Date -->
      <div class="mb-3">
        <label for="date" class="form-label">Formation Date</label>
        <input type="date" value="${not empty formation.date ? formation.date: ''}" class="form-control" name="date" id="date" required>
      </div>

      <!-- Formation Duration -->
      <div class="mb-3">
        <label for="duration" class="form-label">Formation Duration (days)</label>
        <input type="number" value="${formation.duration}" class="form-control" name="duration" id="duration" required>
      </div>

      <!-- Formation Budget -->
      <div class="mb-3">
        <label for="budget" class="form-label">Formation Budget</label>
        <input type="number" value="${formation.budget}" class="form-control" name="budget" id="budget" step="0.01" required>
      </div>

      <!-- Domain Dropdown -->
      <div class="mb-3">
        <label for="idDomain" class="form-label">Domain</label>
        <select class="form-control" name="idDomain" id="idDomain" required>
          <c:forEach var="domain" items="${domains}">
            <option value="${domain.id}" ${domain.id == formation.iddomain.id ? 'selected' : ''}>
                ${domain.name}
            </option>
          </c:forEach>
        </select>
      </div>

      <!-- Trainer Dropdown -->
      <div class="mb-3">
        <label for="idTrainer" class="form-label">Trainer</label>
        <select class="form-control" name="idTrainer" id="idTrainer" required>
          <c:forEach var="trainer" items="${trainers}">
            <option value="${trainer.id}" ${trainer.id == formation.idtrainer.id ? 'selected' : ''}>
                ${trainer.name}
            </option>
          </c:forEach>
        </select>
      </div>

      <button type="submit" class="btn btn-danger">Save</button>
    </form>
  </div>
</div>
</body>
</html>
