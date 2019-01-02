<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>手持机版本管理</title>
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript" src="${ctxStatic}/prss/mobile/mobileVersionList.js"></script>
</head>
<body>
	<div id="toolbar">
		<button id="addVsnBtn" type="button" class="btn btn-link">新增</button>
		<button id="editVsnBtn" type="button" class="btn btn-link">修改</button>
		<button id="delVsnBtn" type="button" class="btn btn-link">删除</button>
	</div>
	<table id="mobileVersionTable"></table>
</body>
</html>