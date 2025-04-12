<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Participant Form</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body>
<div class="container mt-5">
    <div>
        <h2> <c:choose>
            <c:when test="${not empty trainer}">Edit</c:when>
            <c:otherwise>Create</c:otherwise>
        </c:choose> Participant</h2>
    </div>
    <div class="m-3">
        <form action="participants" method="post">
            <input type="hidden" name="action" value="add">
            <c:if test="${not empty participant}">
                <input type="hidden" name="id" value="${participant.id}">
            </c:if>

            <div class="mb-3">
                <label for="firstName" class="form-label">First Name</label>
                <input type="text" value="${participant.firstName}" class="form-control" name="firstName" id="firstName" required>
            </div>

            <div class="mb-3">
                <label for="lastName" class="form-label">Last Name</label>
                <input type="text" value="${participant.lastName}" class="form-control" name="lastName" id="lastName" required>
            </div>

            <div class="mb-3">
                <label for="email" class="form-label">Email</label>
                <input type="text" value="${participant.email}" class="form-control" name="email" id="email" required>
            </div>

            <div class="mb-3">
                <label for="tel" class="form-label">Telephone</label>
                <input type="text" value="${participant.tel}" class="form-control" name="tel" id="tel" required>
            </div>

            <div class="mb-3">
                <label>Structure</label>
                <select name="structure" class="form-control">
                    <option value="CENTRALE" ${participant.structure == 'CENTRALE' ? 'selected' : ''}>Centrale</option>
                    <option value="REGIONALE" ${participant.structure == 'REGIONALE' ? 'selected' : ''}>Régionale</option>
                </select>
            </div>

            <div class="mb-3">
                <%--@declare id="profil"--%><label for="profil">Profil:</label>
                <select id="profilSelect" class="form-control" name="profilLibelle" onchange="toggleCustomProfil(this.value)" required>
                    <c:forEach var="p" items="${defaultProfiles}">
                        <option value="${p}" <c:if test="${participant.profil != null && participant.profil.libelle == p}">selected</c:if>>
                                ${p}
                        </option>
                    </c:forEach>
                    <option value="OTHER">Other</option>
                </select>

                <!-- Hidden text input for custom profile -->
                <div id="customProfilDiv" style="display: none; margin-top: 10px;">
                    <%--@declare id="customprofil"--%><label for="customProfil">Enter custom profile:</label>
                    <input type="text" class="form-control" name="customProfil" id="customProfilInput"/>
                </div>
                <script>
                    function toggleCustomProfil(value) {
                        const customDiv = document.getElementById("customProfilDiv");
                        const customInput = document.getElementById("customProfilInput");
                        if (value === "OTHER") {
                            customDiv.style.display = "block";
                            customInput.required = true;
                        } else {
                            customDiv.style.display = "none";
                            customInput.required = false;
                        }
                    }

                    // Trigger on load in case "Other" is already selected (during edit)
                    window.onload = function () {
                        toggleCustomProfil(document.getElementById("profilSelect").value);
                    };
                </script>
            </div>

            <button type="submit" class="btn btn-danger">Save</button>
        </form>
    </div>
</div>
</body>
</html>
