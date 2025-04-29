<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Top 5 Trainings Card -->
<div class="card">
    <div class="card-header d-flex justify-content-between align-items-center">
        <span>Top 5 Trainings</span>
        <button type="button" class="btn btn-sm btn-outline-primary" data-bs-toggle="modal" data-bs-target="#allTrainingsModal" >
            View All
        </button>
    </div>
    <div class="card-body">
        <ul class="list-unstyled">
            <c:forEach var="training" items="${topTrainings}">
                <li class="mb-2">
                    <div class="d-flex justify-content-between align-items-center">
                        <span><c:out value="${training.title}"/></span>
                        <span class="badge bg-primary">
                            <c:out value="${training.participantCount}"/> Participants
                        </span>
                    </div>
                </li>
            </c:forEach>
        </ul>
    </div>
</div>

<!-- All Trainings Modal -->
<div class="modal fade" id="allTrainingsModal" tabindex="-1" aria-labelledby="allTrainingsModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg">
        <div class="modal-content">
            <div class="modal-header bg-primary text-white">
                <h5 class="modal-title" id="allTrainingsModalLabel">All Trainings</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div id="training-modal-body">
                <jsp:include page="/components/topTrainingPagination.jsp" />
            </div>
        </div>
    </div>
</div>

<script>
    let trainingsLoaded = false;
    document.addEventListener("DOMContentLoaded", function () {
        fetchTrainingPage(1);
    });

    document.addEventListener('DOMContentLoaded', function () {
        document.addEventListener('click', function (e) {
            if (e.target.classList.contains('training-page-link')){
                e.preventDefault();
                const page = e.target.dataset.page;
                fetchTrainingPage(page);
            }
        });
    });

    function fetchTrainingPage(page) {
        fetch("dashboard?action=allTrainings&page=" + page, {
            headers: {
                'X-Requested-With': 'XMLHttpRequest'
            }
        })
            .then(res => res.text())
            .then(html => {
                document.getElementById('training-modal-body').innerHTML = html;
            })
            .catch(err => {
                console.error("Error loading trainings:", err);
            });
    }
</script>

