<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>历史航班动态查看</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/fdHistoricalForm.js"></script>
</head>
<body>
<input type="hidden" id="inFltId" value="${inFltId}">
<input type="hidden" id="outFltId" value="${outFltId}">
	<div id="baseTables">
		<table id=baseTable></table>
	</div>
</body>
</html>