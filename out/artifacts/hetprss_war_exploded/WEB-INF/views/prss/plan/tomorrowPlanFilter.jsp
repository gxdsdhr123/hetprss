<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>次日计划导入核对</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/prss/plan/css/planMain.css" rel="stylesheet" />
<script type="text/javascript">
var result = ${result};
var data = ${data};
</script>
</head>
<body>
	<div id="baseTablesLeft">
		<table id="baseTableLeft"></table>
	</div>
    <div id="baseTablesRight">
        <table id="baseTableRight"></table>
    </div>
    <script type="text/javascript" src="${ctxStatic}/prss/plan/tomorrowPlanFilter.js"></script>
</body>
</html>