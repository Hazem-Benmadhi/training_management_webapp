<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>formations</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body>
<div class="container">
  <div class="mt-5" style="text-align: center">
    <h2>List of Formations</h2>
  </div>
  <div class="mt-5">
    <a href="formations?action=new" class="btn btn-primary">Add New Formation</a>
  </div>
  <div class="mt-3">
    <table class="table table-striped">
      <thead>
      <td>Title</td>
      <td>Date</td>
      <td>Duration (days)</td>
      <td>Budget</td>
      <td>Domain</td>
      <td>Trainer Name</td>
      <td>Actions</td>
      </thead>
      <tbody>
        <c:forEach var="formation" items="${formations}">
        <tr>
          <td>${formation.title}</td>
          <td>${formation.date}</td>
          <td>${formation.duration}</td>
          <td>${formation.budget}</td>
          <td>${formation.iddomain.libelle}</td>
          <td>${formation.idtrainer.firstName}</td>
          <td>
            <a href="formations?action=edit&id=${formation.id}" class="btn btn-success">Edit</a>
            <a href="formations?action=delete&id=${formation.id}" class="btn btn-warning" onclick="confirm('Are you sure to delete this category ?') || event.preventDefault()">Delete</a>
          </td>
          </c:forEach>
          <td></td>
        </tr>
      </tbody>
    </table>
  </div>
</div>
</body>
</html>

