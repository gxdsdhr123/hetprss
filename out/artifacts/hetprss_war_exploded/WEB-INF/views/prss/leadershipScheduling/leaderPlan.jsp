<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>领导排班管理</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<body>
	<input type="hidden" id="officeId" value="${officeId }"/>
	<input type="hidden" id="time" value="${time }"/>
	
	<div style="padding-top: 5px;">
		<div style="float: left;padding-left:15px" id="toolbar">
			<button id="addDepartmentBtn" type="button" class="btn btn-link">增加</button>
			<button id="modifyDepartmentBtn" type="button" class="btn btn-link">修改</button>
			<button id="delDepartmentBtn" type="button" class="btn btn-link">删除</button>
		</div>
	</div>
	<div id="createDetailTableDiv" style="height:400px;width:100%;position: relative;" onselectstart="return false" style="-moz-user-select:none;">
		<table id="createDetailTableDetail"></table>
	</div>
	
	<div id="contentDiv" style="display:none">
		<table id="aptiduteTable"></table>
	</div>

<script type="text/javascript" src="${ctxStatic}/prss/leadershipScheduling/leaderPlan.js"></script>
</body>
</html>