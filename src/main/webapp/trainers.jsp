<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Trainer</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body>
<div class="container">
    <div class="mt-5" style="text-align: center">
        <h2>List of Trainers</h2>
    </div>
    <div class="mt-5">
        <a href="trainers?action=new" class="btn btn-primary">Add New Trainers</a>
    </div>
    <div class="mt-3">
        <table class="table table-striped">
            <thead>
            <tr>
                <th>ID</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Email</th>
                <th>Telephone</th>
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
                    <td>
                        <a href="trainers?action=edit&id=${trainer.id}" class="btn btn-success">Edit</a>
                        <a href="trainers?action=delete&id=${trainer.id}" class="btn btn-warning" onclick="return confirm('Are you sure to delete this trainer?')">Delete</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
