<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>Trainer Form</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
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

      <button type="submit" class="btn btn-danger">Save</button>
    </form>
  </div>
</div>
</body>
</html>
