<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>进港行李交接单</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
</head>
<body>
	<div id="tool-box">
		<shiro:hasPermission name="plan:longterm:create">
			<button id="create" type="button" class="btn btn-link">
				<i class="fa fa-plus">&nbsp;</i>
				新增
			</button>
		</shiro:hasPermission>
		
		<shiro:hasPermission name="plan:longterm:modify">
			<button id="modify" type="button" class="btn btn-link">
				<i class="fa fa-edit">&nbsp;</i>
				修改
			</button>
		</shiro:hasPermission>
		
		<shiro:hasPermission name="plan:longterm:remove">
			<button id="remove" type="button" class="btn btn-link">
				<i class="fa fa-remove">&nbsp;</i>
				删除
			</button>
		</shiro:hasPermission>
	</div>
	
	<table id="baseTable"></table>
	
	<script type="text/javascript" src="${ctxStatic}/prss/produce/enterPortLuggage/enterPortLuggage.js"></script>
</body>
</html>
