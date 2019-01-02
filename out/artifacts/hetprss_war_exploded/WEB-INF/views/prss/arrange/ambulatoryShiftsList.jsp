<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>非固定班制类型</title>
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript" src="${ctxStatic}/prss/arrange/ambulatoryShiftsList.js"></script>
<link href="${ctxStatic}/prss/flightdynamic/css/flightDynList.css"
	rel="stylesheet" />
</head>
<body>
	<div id="toolbar">
		<button id="addBtn" type="button" class="btn btn-link">新增</button>
		<button id="editBtn" type="button" class="btn btn-link">修改</button>
		<button id="delBtn" type="button" class="btn btn-link">删除</button>
	</div>
	<table id="ambulatoryShiftsGrid"></table>
</body>
</html>