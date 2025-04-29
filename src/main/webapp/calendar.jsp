<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <title>Scheduler: Init as View</title>
    <!-- Webix Library -->
    <script type="text/javascript" src="./codebase/webix/webix.js"></script>
    <link
            rel="stylesheet"
            type="text/css"
            href="./codebase/webix/webix.css"
    />

    <!-- Scheduler -->
    <script type="text/javascript" src="./codebase/scheduler.js"></script>
    <link
            rel="stylesheet"
            type="text/css"
            href="./codebase/scheduler.css"
    />
</head>
<body>

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

        webix.ajax().get("${pageContext.request.contextPath}/api/calendarEvents?year=${currentYear}&month=${currentMonth}")
            .then(function(data) {
                scheduler.clearAll();
                scheduler.parse(data.json(), "json");
            });
    });
</script>
</body>
</html>
