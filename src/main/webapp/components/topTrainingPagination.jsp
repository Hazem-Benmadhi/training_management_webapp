<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="modal-body">
  <c:choose>
    <c:when test="${not empty allTrainings}">
      <ul class="list-group">
        <c:forEach var="training" items="${allTrainings}">
          <li class="list-group-item d-flex justify-content-between align-items-center">
            <span><c:out value="${training.title}"/></span>
            <span class="badge bg-primary rounded-pill">
                <c:out value="${training.participantCount}"/> Participants
            </span>
          </li>
        </c:forEach>
      </ul>
    </c:when>
    <c:otherwise>
      <div class="alert alert-info">No trainings found.</div>
    </c:otherwise>
  </c:choose>
</div>
<div class="modal-footer justify-content-center">
  <nav aria-label="Page navigation">
    <ul class="pagination">
      <li class="page-item ${currentPage <= 1 ? 'disabled' : ''}">
        <a href="#" class="page-link training-page-link" data-page="${currentPage - 1 > 0 ? currentPage - 1 : 1}">&laquo; Previous</a>
      </li>
      <c:forEach begin="1" end="${totalPages}" var="i">
        <li class="page-item ${i == currentPage ? 'active' : ''}">
          <a href="#" class="page-link training-page-link" data-page="${i}">${i}</a>
        </li>
      </c:forEach>
      <li class="page-item ${currentPage >= totalPages ? 'disabled' : ''}">
        <a href="#" class="page-link training-page-link" data-page="${currentPage + 1 <= totalPages ? currentPage + 1 : totalPages}">Next &raquo;</a>
      </li>
    </ul>
  </nav>
</div>
