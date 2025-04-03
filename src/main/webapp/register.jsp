<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Register Participant To a Formation</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body>
<div class="container mt-5">
    <h2>Register Participant</h2>

    <div class="m-3">
        <form action="participants?action=confirmRegister" method="post">
            <input type="hidden" name="participantId" value=${participantId}>
            <div class="mb-3">
                <label for="formation" class="form-label">Choose a Formation:</label>
                <select class="form-control" name="formationId" id="formation" required>
                    <c:forEach var="formation" items="${formations}">
                        <option value="${formation.id}">${formation.title}</option>
                    </c:forEach>
                </select>
            </div>
            <button type="submit" class="btn btn-danger">Register</button>
        </form>
    </div>
</div>
</body>
</html>







