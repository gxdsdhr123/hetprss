<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${ctxStatic}/modules/sys/maintain/maintainCommon.js"></script>
<script type="text/javascript" src="${ctxStatic}/prss/plan/atmbPlanForm.js"></script>
<title>空管次日计划编辑</title>
<script type="text/javascript">
	var airports = ${airports};
	var airlines = ${airlines};
	var actTypes = ${actTypes};
	var propertys = ${propertys};
</script>
</head>
<body>
	<input type="hidden" id="isNew" value="${isNew}">
	<input type="hidden" id="fltDate" value="${fltDate}">
	<input type="hidden" id="fltNo" value="${fltNo}">
	<input type="hidden" id="ioType" value="${ioType}">
	<input type="hidden" id="ids" value="${ids}">
	<div id="tool-box">
		<button id="aircraftBtn" type="button" class="btn btn-link">添加机号</button>
		<button id="actTypeBtn" type="button" class="btn btn-link">添加机型</button>
		<button id="airportBtn" type="button" class="btn btn-link">添加机场</button>
		<button id="alnBtn" type="button" class="btn btn-link">添加航空公司</button>
	</div>
	<table id="dataGrid"></table>
</body>
</html>