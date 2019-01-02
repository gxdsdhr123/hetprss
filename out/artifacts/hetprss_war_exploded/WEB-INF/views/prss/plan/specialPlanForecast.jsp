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
<script type="text/javascript" src="${ctxStatic}/prss/plan/specialPlanForecast.js"></script>
<title>公务/通航计划预测</title>
</head>
<body>
	<div id="toolBox">
		<div class="layui-inline">
			<i class="fa fa-plane">&nbsp;</i>预测起始时间:
			<input id="starttime" value="" class="Wdate" style="cursor: pointer" type="text" onClick="WdatePicker({dateFmt:'yyyyMMdd'})"> 
		</div>
		<div class="layui-inline">
			<i class="fa fa-plane">&nbsp;</i>预测截止时间:
			<input id="endtime" value="" class="Wdate" style="cursor: pointer" type="text" onClick="WdatePicker({dateFmt:'yyyyMMdd'})"> 
		</div>
	</div>
	<table id="dataGrid"></table>
</body>
</html>