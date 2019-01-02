<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<
<html>
<head>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
var airportCodeSource = ${airportCodeSource};
var actTypeSource = ${actTypeSource};
var propertyCodeSource = ${propertyCodeSource};
var airlinesCodeSource = ${airlinesCodeSource};
var dataRows = '${dataRows}';
var operateType = '${operateType}';
</script>
</head>
<body>
	<div id="baseTables">
		<table id=baseTable></table>
	</div>
	<form id="flightWeek" class='layui-form' action='' style="display: none">
		<div class="layui-form-item" pane="">
			<input lay-skin="primary" title="星期一" type="checkbox"> <input
				lay-skin="primary" title="星期二" type="checkbox"> <input
				lay-skin="primary" title="星期三" type="checkbox">
		</div>
		<div class="layui-form-item" pane="">
			<input lay-skin="primary" title="星期四" type="checkbox"> <input
				lay-skin="primary" title="星期五" type="checkbox"> <input
				lay-skin="primary" title="星期六" type="checkbox">
		</div>
		<div class="layui-form-item" pane="">
			<input lay-skin="primary" title="星期日" type="checkbox">
		</div>
	</form>
	<div id="flightDates" style="display: none">
		<div>
			<textarea id="flightDatesVal" style="background-color: black;width:405px;height: 240px;"></textarea>
		</div>
	</div>
	<script type="text/javascript" src="${ctxStatic}/prss/plan/internationalPlanAdd.js"></script>
</body>
</html>