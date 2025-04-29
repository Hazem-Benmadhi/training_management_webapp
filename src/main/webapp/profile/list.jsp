<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${empty sessionScope.user}">
    <c:redirect url="${pageContext.request.contextPath}/login" />
</c:if>

<html>
<head>
    <title>Profile Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/pageContent.css">
    <style>
        .profile-card {
            transition: all 0.3s;
            min-height: 120px;
        }
        .profile-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
        }

        .text{
            box-sizing : border-box;
            color : rgb(253 137 13);
            display : block;
            font-family : system-ui, -apple-system, "Segoe UI", Roboto, "Helvetica Neue", "Noto Sans", "Liberation Sans", Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol", "Noto Color Emoji";
            font-size: 30px;
            font-weight : 500;
            height : 36px;
            line-height: 36px;
            margin-block-end:0;
            margin-block-start:0;
            margin-bottom:0;
            margin-inline-end :0;
            margin-inline-start : 0;
            margin-top : 0;
            text-align : start;
            text-size-adjust : 100%;
            unicode-bidi : isolate;
            width : 313.516px;
            -webkit-tap-highlight-color : rgba(0, 0, 0, 0);
        }

    </style>
</head>
<body>
<div class="container py-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="mb-0 text">
            <i class="fas fa-id-badge"></i> Profile Management
        </h2>
        <button class="add-btn" data-bs-toggle="modal" data-bs-target="#addProfileModal">
            <i class="fas fa-plus"></i> Add Profile
        </button>
    </div>

    <!-- Profile Cards -->
    <div class="row row-cols-1 row-cols-md-3 g-4" id="profilesContainer">
        <c:forEach var="profile" items="${profiles}">
            <div class="col">
                <div class="card profile-card h-100">
                    <div class="card-body d-flex flex-column">
                        <h5 class="card-title flex-grow-1">
                            <i class="fas fa-user-tag"></i> ${profile.libelle}
                        </h5>
                        <div class="mt-auto">
                            <button class="btn btn-sm btn-outline-danger w-100 delete-user"
                                    data-id="${profile.id}">
                                <i class="fas fa-trash-alt"></i> Delete
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>

    <!-- Profile Modal -->
    <div class="modal fade" id="addProfileModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Add New Profile</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form id="addProfileForm">
                        <div class="mb-3">
                            <label class="form-label">Profile Name</label>
                            <input type="text" class="form-control" name="libelle" required
                                   placeholder="e.g. ADMIN, MANAGER">
                        </div>
                        <div class="d-grid">
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-save"></i> Save Profile
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://kit.fontawesome.com/a076d05399.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<script>
    $(document).ready(function() {
        // Add new profile
        $('#addProfileForm').submit(function(e) {
            e.preventDefault();
            $.post('profiles', $(this).serialize())
                .done(function(response) {
                    if (response.success) {
                        $('#addProfileModal').modal('hide');
                        location.reload(); // Full refresh to ensure consistency
                    } else {
                        alert('Error: ' + response.error);
                    }
                })
                .fail(function(xhr) {
                    alert('Error: ' + xhr.responseJSON.error);
                });
        });

        // Delete profile
        $(document).on('click', '.delete-user', function() {
           if (confirm('Are you sure you want to delete this profile?')) {  // Remove the confirmation message from hear
                $.ajax({
                    url: 'profiles?id=' + $(this).data('id'),
                    type: 'DELETE',
                    success: function(response) {
                        if (response.success) {
                            location.reload();
                        } else {
                            alert(response.error);
                        }
                    },
                    error: function(xhr) {
                        if (xhr.status === 409) { // Conflict status
                            alert(xhr.responseJSON.error);
                        } else {
                            alert('Error: ' + xhr.responseJSON.error);
                        }
                    }
                });
           }
        });
    });
</script>
</body>
</html>