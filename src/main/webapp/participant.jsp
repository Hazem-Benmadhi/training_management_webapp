<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Participant</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body>
<div class="container">
    <div class="mt-5" style="text-align: center">
        <h2>List of Participant</h2>
    </div>
    <div class="mt-5">
        <a href="participants?action=new" class="btn btn-primary">Add New Participant</a>
    </div>
    <div class="mt-3">
        <table class="table table-striped">
            <thead>
            <tr>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Email</th>
                <th>Telephone</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="participant" items="${participants}">
                <tr>
                    <td>${participant.firstName}</td>
                    <td>${participant.lastName}</td>
                    <td>${participant.email}</td>
                    <td>${participant.tel}</td>
                    <td>
                        <a href="participants?action=edit&id=${participant.id}" class="btn btn-success">Edit</a>
                        <a href="participants?action=register&id=${participant.id}" class="btn btn-success">
                            <c:choose>
                                <c:when test="${participant.isRegistered}">Registered</c:when>
                                <c:otherwise>Not Registered</c:otherwise>
                            </c:choose>
                        </a>
                        <a href="participants?action=delete&id=${participant.id}" class="btn btn-warning" onclick="return confirm('Are you sure to delete this trainer?')">Delete</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
