<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>domains</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body>
<div class="container">
    <div class="mt-5" style="text-align: center">
        <h2>List of Domains</h2>
    </div>
    <div class="mt-5">
        <a href="domains?action=new" class="btn btn-primary">Add New Domain</a>
    </div>
    <div class="mt-3">
        <table class="table table-striped">
            <thead>
            <td>ID</td>
            <td>Libelle</td>
            <td>Actions</td>
            </thead>
            <tbody>
            <c:forEach var="domain" items="${domains}">
            <tr>
                <td>${domain.id}</td>
                <td>${domain.libelle}</td>
                <td>
                    <a href="domains?action=delete&id=${domain.id}" class="btn btn-warning" onclick="confirm('Are you sure to delete this domain ?') || event.preventDefault()">Delete</a>
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