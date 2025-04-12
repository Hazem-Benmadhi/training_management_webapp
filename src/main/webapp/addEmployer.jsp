<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>${employer.id == null ? 'Add' : 'Edit'} Employer</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
  <h2 class="text-center">${employer.id == null ? 'Add' : 'Edit'} Employer</h2>
  <form method="post" action="employers">
    <input type="hidden" name="id" value="${employer.id}"/>
    <div class="mb-3">
      <label for="nameEmployer" class="form-label">Employer Name</label>
      <input type="text" class="form-control" id="nameEmployer" name="nameEmployer"
             value="${employer.nameEmployer}" required>
    </div>
    <button type="submit" class="btn btn-success">Save</button>
    <a href="employers?action=list" class="btn btn-secondary">Cancel</a>
  </form>
</div>
</body>
</html>
