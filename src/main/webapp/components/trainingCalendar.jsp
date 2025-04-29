<%--<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>--%>
<%--<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>--%>
<%--<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>--%>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>--%>

<%--<div class="card">--%>
<%--  <div class="card-header">--%>
<%--    <span>Training Calendar</span>--%>
<%--    <div>--%>
<%--      <button class="btn btn-sm btn-outline-secondary me-2" id="prevMonthBtn">--%>
<%--        <i class="fas fa-chevron-left"></i>--%>
<%--      </button>--%>
<%--      <span id="currentMonthYear">--%>
<%--                <fmt:formatDate value="${calendarDate}" pattern="MMMM yyyy" />--%>
<%--            </span>--%>
<%--      <button class="btn btn-sm btn-outline-secondary ms-2" id="nextMonthBtn">--%>
<%--        <i class="fas fa-chevron-right"></i>--%>
<%--      </button>--%>
<%--    </div>--%>
<%--  </div>--%>
<%--  <div class="card-body">--%>
<%--    <div class="calendar">--%>
<%--      <div class="days">--%>
<%--        <div class="day header">Sun</div>--%>
<%--        <div class="day header">Mon</div>--%>
<%--        <div class="day header">Tue</div>--%>
<%--        <div class="day header">Wed</div>--%>
<%--        <div class="day header">Thu</div>--%>
<%--        <div class="day header">Fri</div>--%>
<%--        <div class="day header">Sat</div>--%>

<%--        <c:forEach var="day" begin="1" end="${firstDayOfWeek - 1}">--%>
<%--          <div class="day"></div>--%>
<%--        </c:forEach>--%>

<%--        <c:forEach var="day" begin="1" end="${daysInMonth}">--%>
<%--          <div class="day">--%>
<%--            <div class="date">${day}</div>--%>
<%--            <c:forEach var="event" items="${calendarEvents}">--%>
<%--              <fmt:formatDate value="${event.date}" pattern="d" var="eventDay" />--%>
<%--              <c:if test="${eventDay == day}">--%>
<%--                <div class="event ${event.color} text-white">--%>
<%--                  <c:out value="${fn:substring(event.title, 0, 10)}" />--%>
<%--                  <fmt:formatDate value="${event.date}" pattern="H:mm" />--%>
<%--                </div>--%>
<%--              </c:if>--%>
<%--            </c:forEach>--%>
<%--          </div>--%>
<%--        </c:forEach>--%>

<%--        <c:forEach var="day" begin="1" end="${42 - firstDayOfWeek - daysInMonth + 1}">--%>
<%--          <div class="day"></div>--%>
<%--        </c:forEach>--%>
<%--      </div>--%>
<%--    </div>--%>
<%--  </div>--%>
<%--</div>--%>

<%--<script>--%>
<%--  document.addEventListener('DOMContentLoaded', function() {--%>
<%--    const prevMonthBtn = document.getElementById('prevMonthBtn');--%>
<%--    const nextMonthBtn = document.getElementById('nextMonthBtn');--%>

<%--    prevMonthBtn.addEventListener('click', function() {--%>
<%--      navigateMonth(-1);--%>
<%--    });--%>

<%--    nextMonthBtn.addEventListener('click', function() {--%>
<%--      navigateMonth(1);--%>
<%--    });--%>

<%--    function navigateMonth(offset) {--%>
<%--      const currentUrl = new URL(window.location.href);--%>

<%--      // Get current month and year from URL or use current date--%>
<%--      let year = parseInt(currentUrl.searchParams.get('year')) || ${currentYear};--%>
<%--      let month = parseInt(currentUrl.searchParams.get('month')) || ${currentMonth};--%>

<%--      // Calculate new month and year--%>
<%--      month += offset;--%>

<%--      if (month < 1) {--%>
<%--        month = 12;--%>
<%--        year--;--%>
<%--      } else if (month > 12) {--%>
<%--        month = 1;--%>
<%--        year++;--%>
<%--      }--%>

<%--      // Update URL parameters--%>
<%--      currentUrl.searchParams.set('year', year);--%>
<%--      currentUrl.searchParams.set('month', month);--%>

<%--      // Navigate to new URL--%>
<%--      window.location.href = currentUrl.toString();--%>
<%--    }--%>
<%--  });--%>
<%--</script>--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Webix Library -->
<script type="text/javascript" src="./../codebase/webix/webix.js"></script>
<link rel="stylesheet" type="text/css" href="./../codebase/webix/webix.css"/>

<!-- Scheduler -->
<script type="text/javascript" src="./../codebase/scheduler.js"></script>
<link rel="stylesheet" type="text/css" href="./../codebase/scheduler.css"/>


<div class="card">
  <div class="card-header d-flex justify-content-between align-items-center">
    <span>Training Calendar</span>
  </div>
  <div class="card-body">
    <div id="scheduler_here" style="height: 600px;"></div>
  </div>
</div>

<script>
  webix.ready(function() {
    scheduler.config.readonly = true;
    scheduler.init("scheduler_here", new Date(${currentYear}, ${currentMonth - 1}, 1), "month");

    webix.ajax().get("dashboard/api/calendarEvents?year=2025&month=4", function (text, data, xhr) {
      const events = JSON.parse(text);
      scheduler.parse(events, "json");
    });
  });
</script>
