<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>航班监控</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/prss/flightdynamic/css/flightDynMonitorMain.css" rel="stylesheet" />
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
var data = ${data};
var type = "${type}";
</script>
</head>
<body>
	<div id="baseTables">
		<table id="baseTable"></table>
	</div>
    
    <script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/flightDynMonitorMain.js"></script>
</body>
</html>