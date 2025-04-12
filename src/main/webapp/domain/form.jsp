<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Formation Form</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
</head>
<body>
<div class="container mt-5">
    <div>
        <h2>Add Domain</h2>
    </div>
    <div class="m-3">
        <form action="domains" method="post">
            <input type="hidden" name="action" value="add">

            <!-- Domain Libelle -->
            <div class="mb-3">
                <label for="libelle" class="form-label">Domain Libelle</label>
                <input type="text" value="${domain.libelle}" class="form-control" name="libelle" id="libelle" required>
            </div>

            <button type="submit" class="btn btn-danger">Save</button>
        </form>
    </div>
</div>
</body>
</html>
