<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>手持设备管理</title>
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript" src="${ctxStatic}/prss/mobile/mobileManageList.js"></script>
</head>
<body>
	<div id="toolbar">
		<button id="addBtn" type="button" class="btn btn-link">新增</button>
		<button id="editBtn" type="button" class="btn btn-link">修改</button>
		<button id="delBtn" type="button" class="btn btn-link">删除</button>
		<!-- <button id="logBtn" type="button" class="btn btn-link">维护记录</button>
		<button id="printBtn" type="button" class="btn btn-link">打印</button> -->
	</div>
	<table id="mobileManageTable"></table>
</body>
</html>