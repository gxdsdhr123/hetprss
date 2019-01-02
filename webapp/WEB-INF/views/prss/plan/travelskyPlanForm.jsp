<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<
<html>
<head>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/prss/plan/css/travelskyPlanForm.css" rel="stylesheet" />
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${ctxStatic}/modules/sys/maintain/maintainCommon.js"></script>
<script type="text/javascript">
	var airportCodeSource = ${airportCodeSource};
	var actTypeSource = ${actTypeSource};
</script>
</head>
<body>
	<input type="hidden" id="isNew" value="${isNew}">
	<input type="hidden" id="fltDate" value="${fltDate}">
	<input type="hidden" id="fltNo" value="${fltNo}">
	<div id="baseTables">
		<table id=baseTable></table>
	</div>
	<script type="text/javascript" src="${ctxStatic}/prss/plan/travelskyPlanForm.js"></script>
</body>
</html>