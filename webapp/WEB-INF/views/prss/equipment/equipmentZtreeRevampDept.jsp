<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<!-- start -->
<%@include file="/WEB-INF/views/include/treeview.jsp"%>
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/fullcalendar/fullcalendar.min.css"/>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<link href="${ctxStatic}/prss/arrange/css/empPlanList.css" rel="stylesheet" />
</head>
<body>
<form id="filterDouXXX" class="layui-form" action="">
	
	<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label"><font color="red">* </font>部门名称</label>
			<div class="layui-input-inline">
				<input type="text" name="name" class="layui-input name" value="${result.NAME}"/>
			</div>
		</div>
	</div>
</form>
<script type="text/javascript" src="${ctxStatic}/prss/equipment/equipmentZtreeRevampDept.js"></script>
</body>
</html>