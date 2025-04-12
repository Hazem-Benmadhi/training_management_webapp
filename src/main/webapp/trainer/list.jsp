<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Trainers</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body>
<div class="container mt-5">
    <div class="text-center mb-4">
        <h2>List of Trainers</h2>
    </div>

    <div class="mb-3">
        <a href="trainers?action=new" class="btn btn-primary">Add New Trainer</a>
    </div>

    <table class="table table-bordered table-hover">
        <thead class="table-dark">
        <tr>
            <th>ID</th>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Email</th>
            <th>Telephone</th>
            <th>Type</th>
            <th>Employer</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="trainer" items="${trainers}">
            <tr>
                <td>${trainer.id}</td>
                <td>${trainer.firstName}</td>
                <td>${trainer.lastName}</td>
                <td>${trainer.email}</td>
                <td>${trainer.tel}</td>
                <td>${trainer.type}</td>
                <td>
                    <c:choose>
                        <c:when test="${not empty trainer.employer}">
                            ${trainer.employer.nameEmployer}
                        </c:when>
                        <c:otherwise>
                            <em>None</em>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <a href="trainers?action=edit&id=${trainer.id}" class="btn btn-sm btn-success">Edit</a>
                    <a href="trainers?action=delete&id=${trainer.id}" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure you want to delete this trainer?')">Delete</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
