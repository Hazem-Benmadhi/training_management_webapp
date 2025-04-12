<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>formations</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body>
<div class="container mt-5">
    <div class="text-center mb-4">
      <h2>List of Formation</h2>
    </div>

    <div class="mb-3">
      <a href="formations?action=new" class="btn btn-primary">Add New Formation</a>

      <!-- Button trigger modal -->
      <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModal">
        Add New Formation
      </button>

      <!-- Modal -->
      <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="exampleModalLabel">
                <c:choose>
                  <c:when test="${not empty formation}">Edit</c:when>
                  <c:otherwise>Create</c:otherwise>
                </c:choose> Formation
              </h5>
              <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <div class="modal-body">
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
                    <c:choose>
                      <c:when test="${not empty domains}">
                        <c:forEach var="domain" items="${domains}">
                          <option value="${domain.id}"
                            ${not empty formation.iddomain and domain.id == formation.iddomain.id ? 'selected' : ''}>
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

                <!-- Trainer Dropdown -->
                <div class="mb-3">
                  <label for="idTrainer" class="form-label">Trainer</label>
                  <select class="form-control" name="idTrainer" id="idTrainer" required>
                    <c:choose>
                      <c:when test="${not empty trainers}">
                        <c:forEach var="trainer" items="${trainers}">
                          <option value="${trainer.id}"
                            ${not empty formation.idtrainer and trainer.id == formation.iddomain.id ? 'selected' : ''}>
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


                <button type="submit" class="btn btn-danger">Save</button>
              </form>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
              <button type="button" class="btn btn-primary">Save changes</button>
            </div>
          </div>
        </div>
      </div>
    </div>

  <div class="mt-3">
    <table class="table table-bordered table-hover">
      <thead class="table-dark">
      <tr>
        <th>Title</th>
        <th>Date</th>
        <th>Duration (days)</th>
        <th>Budget</th>
        <th>Domain</th>
        <th>Trainer Name</th>
        <th>Actions</th>
      </tr>
      </thead>
      <tbody>
        <c:forEach var="formation" items="${formations}">
        <tr>
          <td>${formation.title}</td>
          <td>${formation.date}</td>
          <td>${formation.duration}</td>
          <td>${formation.budget}</td>
          <td>${formation.domain.libelle}</td>
          <td>${formation.trainer.firstName}</td>
          <td>
            <a href="formations?action=edit&id=${formation.id}" class="btn btn-success">Edit</a>
            <a href="formations?action=delete&id=${formation.id}" class="btn btn-warning" onclick="confirm('Are you sure to delete this category ?') || event.preventDefault()">Delete</a>
          </td>
          </c:forEach>
        </tr>
      </tbody>
    </table>
  </div>
</div>
</body>
</html>

