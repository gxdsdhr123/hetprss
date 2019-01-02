<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css"
	rel="stylesheet" />
<link href="${ctxStatic}/prss/plan/css/longTermPlanImport.css"
	rel="stylesheet" />
<script type="text/javascript"
	src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<title>航班动态要客导入</title>
</head>
<body>
	<div id="importToolBox">
		<button id="importPlan" type="button" class="btn btn-link">
			<i class="fa fa-cloud-upload">&nbsp;</i>导入计划
		</button>
		<button id="remove" type="button" class="btn btn-link">
			<i class="fa fa-remove">&nbsp;</i>删除
		</button>
		<button id="synchro" type="button" class="btn btn-link">
			<i class="fa fa-hdd-o">&nbsp;</i>确定同步
		</button>
	</div>
	<div id="longTermPlanImportTables">
		<table id="importTable"></table>
	</div>
	<form id="fileList" action="${ctx}/flightDynamic/readVipExcel"
		method="post" enctype="multipart/form-data">
		<div class="layui-form-item">
			<label class="layui-form-label">选择日期:</label>
			<div class="layui-input-inline">
				<input type="text" name="timeinput" required lay-verify="required"
					id="timeinput" readonly="readonly" placeholder="请选择时间"
					autocomplete="off" class="layui-input" onclick="popupDate();">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">要客计划:</label>
			<div class="layui-input-inline">
				<input id="fileInput" name="file" type="file">
			</div>
		</div>
	</form>
	<script type="text/javascript"
		src="${ctxStatic}/prss/flightdynamic/fdImportVip.js"></script>
</body>
</html>