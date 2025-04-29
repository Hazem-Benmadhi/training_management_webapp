<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="row mb-4">
    <div class="col-md-3">
        <div class="card stats-card bg-soft-primary">
            <div class="icon color-primary">
                <i class="fas fa-users"></i>
            </div>
            <div class="number color-primary"><c:out value="${statistics.totalParticipants}" /></div>
            <div class="label">Total Participants</div>
        </div>
    </div>
    <div class="col-md-3">
        <div class="card stats-card bg-soft-success">
            <div class="icon color-success">
                <i class="fas fa-book-open"></i>
            </div>
            <div class="number color-success"><c:out value="${statistics.activeTrainings}" /></div>
            <div class="label">Active Trainings</div>
        </div>
    </div>
    <div class="col-md-3">
        <div class="card stats-card bg-soft-info">
            <div class="icon color-info">
                <i class="fas fa-chalkboard-teacher"></i>
            </div>
            <div class="number color-info"><c:out value="${statistics.totalTrainers}" /></div>
            <div class="label">Trainers</div>
        </div>
    </div>
    <div class="col-md-3">
        <div class="card stats-card bg-soft-warning">
            <div class="icon color-warning">
                <i class="fas fa-money-bill-wave"></i>
            </div>
            <div class="number color-warning">
                <fmt:formatNumber type="currency" value="${statistics.totalBudget}" currencySymbol="$" maxFractionDigits="0"/>
            </div>
            <div class="label">Total Budget</div>
        </div>
    </div>
</div>