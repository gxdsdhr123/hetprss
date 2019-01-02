<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>流程模板管理</title>
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript" src="${ctxStatic}/prss/workflow/processList.js"></script>
</head>
<body>
	<div id="toolbar">
		<button id="addBtn" type="button" class="btn btn-link">
			<i class="fa fa-plus">&nbsp;</i>新增
		</button>
		<button id="editBtn" type="button" class="btn btn-link">
			<i class="fa fa-edit">&nbsp;</i>修改
		</button>
		<button id="remove" type="button" class="btn btn-link">
			<i class="fa fa-remove">&nbsp;</i>删除
		</button>
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
	<table id="dataGrid" class="table table-hover table-striped">
	</table>
</body>
</html>