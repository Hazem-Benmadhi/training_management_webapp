<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Participant Formations</title>
</head>
<body>
<div class="container">
    <h3>Formations for ${participant.firstName} ${participant.lastName}</h3>
    <table class="table">
        <thead>
        <tr>
            <th>Title</th>
            <th>Date</th>
            <th>Trainer</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="formation" items="${participants.formations}">
            <tr>
                <td>${formation.title}</td>
                <td>${formation.date}</td>
                <td>${formation.idtrainer.firstName} ${formation.idtrainer.lastName}</td>
                <td>
                    <a href="participants?action=unregister&participantId=${participant.id}&formationId=${formation.id}"
                       class="btn btn-danger">Unregister</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
