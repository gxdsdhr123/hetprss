<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<title>延误信息录入</title>
<script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/fdDelay.js"></script>
<script type="text/javascript">
	var tmCode = ${tmCode};
	var tmReason = ${tmReason};
	var dimType = ${dimType};
</script>
</head>
<body>
	<input type="hidden" name="fltid" id="fltid" value="${fltid}">
	<input type="hidden" id="hisFlag" value="${hisFlag}">
	<input type="hidden" name="airline" id="airline" value="${airline}">
	<div id="toolbar">
		<button id="addDelayBtn" type="button" class="btn btn-link">新增行</button>
		<button id="addTelegramBtn" type="button" class="btn btn-link">新增报文延误代码</button>
	</div>
	<table id="delayGrid"></table>
</body>
</html>