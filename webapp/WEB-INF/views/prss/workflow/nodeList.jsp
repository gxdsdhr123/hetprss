<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>节点管理</title>
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript" src="${ctxStatic}/prss/workflow/nodeList.js"></script>
</head>
<body>
	<div id="toolbar">
		<button id="addBtn" type="button" class="btn btn-link">新增</button>
		<button id="editBtn" type="button" class="btn btn-link">修改</button>
		<button id="delBtn" type="button" class="btn btn-link">删除</button>
		保障类型：
		<select id="jobKind" onchange="changKind()" style="width:200px">
			<option value="">请选择保障类型</option>
			<c:forEach items="${kindList}" var="kind">
				<option value="${kind.RESKIND}">${kind.KINDNAME}</option>
			</c:forEach>
		</select>
		作业类型：
		<select id="jobType" onchange="query()" style="width:200px">
			<option value="">请选择作业类型</option>
		</select>
	</div>
	<table id="nodeGrid"></table>
</body>
</html>