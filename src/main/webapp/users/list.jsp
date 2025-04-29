<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${empty sessionScope.user}">
    <c:redirect url="${pageContext.request.contextPath}/login" />
</c:if>

<html>
<head>
    <title>User Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/pageContent.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.css">
    <style>
        .user-card {
            transition: transform 0.2s;
        }
        .user-card:hover {
            transform: scale(1.02);
        }
        .admin-badge {
            background-color: #dc3545;
        }
        .resp-badge {
            background-color: #fd7e14;
        }
        #alerts-container {
            position: fixed;
            top: 20px;
            right: 20px;
            z-index: 9999;
            width: 350px;
        }
        .toast-success {
            background-color: #198754;
        }
        .toast-error {
            background-color: #dc3545;
        }
        .toast-warning {
            background-color: #ffc107;
            color: #212529;
        }
        .modal-confirm {
            color: #636363;
        }
        .modal-confirm .modal-content {
            padding: 0;
            border-radius: 50px;
            border: none;
        }
        .modal-confirm .modal-header {
            border-bottom: none;
            position: relative;
            justify-content: center;
        }
        .modal-confirm .modal-title {
            color: #333;
        }
        .modal-confirm .close {
            position: absolute;
            top: -5px;
            right: -5px;
        }
        .modal-confirm .modal-body {
            color: #555;
            text-align: center;
        }
        .modal-confirm .icon-box {
            width: 80px;
            height: 80px;
            margin: 0 auto;
            border-radius: 50%;
            z-index: 9;
            text-align: center;
            border: 3px solid #f15e5e;
        }
        .modal-confirm .icon-box i {
            color: #f15e5e;
            font-size: 46px;
            display: inline-block;
            margin-top: 13px;
        }
        .modal-confirm .btn-danger {
            color: #fff;
            border-radius: 4px;
            background: #f15e5e;
            text-decoration: none;
            transition: all 0.4s;
            line-height: normal;
            border: none;
        }
        .modal-confirm .btn-danger:hover, .modal-confirm .btn-danger:focus {
            background: #ee3535;
            outline: none;
        }
        .modal-confirm .btn-secondary {
            border: 1px solid #ccc;
        }
        .modal-confirm .btn-secondary:hover, .modal-confirm .btn-secondary:focus {
            background: #eee;
        }
    </style>
</head>
<body>
<c:if test="${user.role eq 'admin'}">
    <!-- Alerts container -->
    <div id="alerts-container"></div>

    <div class="container py-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2><i class="fas fa-users-cog"></i> User Management</h2>
            <button class="add-btn" data-bs-toggle="modal" data-bs-target="#newUserModal">
                <i class="fas fa-user-plus"></i> Add User
            </button>
        </div>

        <div class="row row-cols-1 row-cols-md-3 g-4" id="users-container">
            <c:forEach items="${users}" var="user">
                <div class="col user-item" data-userid="${user.id}">
                    <div class="card user-card h-100">
                        <div class="card-body">
                            <div class="d-flex justify-content-between">
                                <h5 class="card-title">${user.login}</h5>
                                <span class="badge ${user.role == 'admin' ? 'admin-badge' :
                                                      user.role == 'responsible' ? 'resp-badge' : 'bg-secondary'} role-badge">
                                        ${user.role}
                                </span>
                            </div>

                            <div class="mt-3">
                                <select class="form-select role-select mb-2" data-userid="${user.id}">
                                    <option value="ADMIN" ${user.role == 'admin' ? 'selected' : ''}>ADMIN</option>
                                    <option value="RESPONSIBLE" ${user.role == 'responsible' ? 'selected' : ''}>RESPONSIBLE</option>
                                    <option value="SIMPLE" ${user.role == 'simple' ? 'selected' : ''}>SIMPLE</option>
                                </select>

                                <button class="btn btn-sm btn-outline-danger w-100 delete-user"
                                        data-userid="${user.id}" data-username="${user.login}">
                                    <i class="fas fa-trash-alt"></i> Delete
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

    <!-- Add User Modal -->
    <div class="modal fade" id="newUserModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Add New User</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form id="userForm">
                        <div class="mb-3">
                            <label class="form-label">Username</label>
                            <input type="text" class="form-control" name="login" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Password</label>
                            <input type="password" class="form-control" name="password" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Role</label>
                            <select class="form-select" name="role">
                                <option value="simple">Simple User</option>
                                <option value="responsible">Responsible</option>
                                <option value="admin">Admin</option>
                            </select>
                        </div>
                        <button type="submit" class="btn btn-primary w-100">
                            <i class="fas fa-save"></i> Create User
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Delete Confirmation Modal -->
    <div class="modal fade" id="deleteConfirmModal" tabindex="-1">
        <div class="modal-dialog modal-dialog-centered modal-confirm">
            <div class="modal-content">
                <div class="modal-header">
                    <div class="icon-box">
                        <i class="fas fa-trash-alt"></i>
                    </div>
                    <h4 class="modal-title">Are you sure?</h4>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <p>Do you really want to delete user "<span id="delete-username"></span>"?</p>
                </div>
                <div class="modal-footer justify-content-center">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="button" class="btn btn-danger" id="confirm-delete">Delete</button>
                </div>
            </div>
        </div>
    </div>
</c:if>

<!-- JavaScript -->
<script src="https://kit.fontawesome.com/a076d05399.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js"></script>

<script>
    $(document).ready(function() {
        // Configure toastr notifications
        toastr.options = {
            "closeButton": true,
            "debug": false,
            "newestOnTop": true,
            "progressBar": true,
            "positionClass": "toast-top-right",
            "preventDuplicates": false,
            "onclick": null,
            "showDuration": "300",
            "hideDuration": "1000",
            "timeOut": "5000",
            "extendedTimeOut": "1000",
            "showEasing": "swing",
            "hideEasing": "linear",
            "showMethod": "fadeIn",
            "hideMethod": "fadeOut"
        };

        // Create new user
        $('#userForm').submit(function(e) {
            e.preventDefault();

            $.ajax({
                url: 'users?action=create',
                type: 'POST',
                data: $(this).serialize(),
                dataType: 'json',
                success: function(response) {
                    if (response.success) {
                        // Close modal and clear form
                        $('#newUserModal').modal('hide');
                        $('#userForm')[0].reset();

                        // Show success message
                        toastr.success(response.message);

                        // Reload the user list without full page refresh
                        loadUsers();
                    } else {
                        toastr.error(response.message);
                    }
                },
                error: function(xhr) {
                    let errorMsg = 'Failed to create user';
                    if (xhr.responseJSON && xhr.responseJSON.message) {
                        errorMsg = xhr.responseJSON.message;
                    }
                    toastr.error(errorMsg);
                }
            });
        });

        // Update user role
        $(document).on('change', '.role-select', function() {
            const userId = $(this).data('userid');
            const newRole = $(this).val();
            const userCard = $(this).closest('.user-item');
            const roleBadge = userCard.find('.role-badge');

            $.ajax({
                url: 'users',
                type: 'POST',
                data: {
                    action: 'update',
                    userId: userId,
                    role: newRole
                },
                dataType: 'json',
                success: function(response) {
                    if (response.success) {
                        // Update the role badge without page reload
                        roleBadge.text(response.role);

                        // Update badge color based on new role
                        roleBadge.removeClass('admin-badge resp-badge bg-secondary');
                        if (response.role === 'ADMIN') {
                            roleBadge.addClass('admin-badge');
                        } else if (response.role === 'RESPONSIBLE') {
                            roleBadge.addClass('resp-badge');
                        } else {
                            roleBadge.addClass('bg-secondary');
                        }

                        toastr.success(response.message);
                    } else {
                        toastr.warning(response.message);
                        loadUsers(); // Reset to current state if backend rejects change
                    }
                },
                error: function(xhr) {
                    handleAjaxError(xhr);
                    loadUsers(); // Reset to current state
                }
            });
        });

        // Setup delete user handling - Show the modal
        $(document).on('click', '.delete-user', function() {
            const userId = $(this).data('userid');
            const username = $(this).data('username');

            // Set the user ID as data attribute on confirm button
            $('#confirm-delete').data('userid', userId);

            // Show the username in the confirmation message
            $('#delete-username').text(username);

            // Show the modal
            $('#deleteConfirmModal').modal('show');
        });

        // Handle the confirm delete button click
        $('#confirm-delete').click(function() {
            const userId = $(this).data('userid');
            const userCard = $(`.user-item[data-userid="${userId}"]`);

            $.ajax({
                url: 'users',
                type: 'POST',
                data: {
                    action: 'delete',
                    userId: userId
                },
                dataType: 'json',
                success: function(response) {
                    // Hide the modal
                    $('#deleteConfirmModal').modal('hide');

                    if (response.success) {
                        // Remove the user card from the DOM with animation
                        userCard.fadeOut(300, function() {
                            $(this).remove();
                        });
                        toastr.success(response.message);
                    } else {
                        toastr.warning(response.message);
                        loadUsers(); // Reset to current state
                    }
                },
                error: function(xhr) {
                    // Hide the modal
                    $('#deleteConfirmModal').modal('hide');
                    handleAjaxError(xhr);
                    loadUsers(); // Reset to current state
                }
            });
        });

        function handleAjaxError(xhr) {
            let message = 'An error occurred';
            if (xhr.responseJSON && xhr.responseJSON.message) {
                message = xhr.responseJSON.message;
            }
            toastr.error(message);
        }

        // Function to load users via AJAX
        function loadUsers() {
            $.ajax({
                url: 'users',
                type: 'GET',
                dataType: 'html',
                success: function(data) {
                    // Extract just the users container HTML from the response
                    let tempDiv = $('<div>').html(data);
                    let newUsers = tempDiv.find('#users-container').html();

                    if (newUsers) {
                        $('#users-container').html(newUsers);
                    } else {
                        // Fallback to full page reload if extraction fails
                        location.reload();
                    }
                },
                error: function() {
                    toastr.error('Failed to refresh user data');
                    // Fallback to full page reload
                    location.reload();
                }
            });
        }
    });
</script>
</body>
</html>