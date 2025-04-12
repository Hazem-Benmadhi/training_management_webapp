<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Employer List</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <h2 class="text-center">Employer List</h2>
    <div class="my-3">
        <a href="employers?action=new" class="btn btn-primary">Add Employer</a>
    </div>
    <table class="table table-striped">
        <thead>
        <tr>
            <th>ID</th>
            <th>Employer Name</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="employer" items="${employers}">
            <tr>
                <td>${employer.id}</td>
                <td>${employer.nameEmployer}</td>
                <td>
                    <a href="employers?action=delete&id=${employer.id}" class="btn btn-danger"
                       onclick="return confirm('want to delete this employer ?')">Delete</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
