<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>收费单</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript"
	src="${ctxStatic}/prss/produce/chargeBillList.js"></script>
</head>
<body>

	<div id="toolbar">
		<button id="newBtn" type="button" class="btn btn-link">新增单据</button>
		<button id="editBtn" type="button" class="btn btn-link">修改单据</button>
		<button id="delBtn" type="button" class="btn btn-link">删除单据</button>
	</div>
	<table id="chargeBillGrid"></table>
	<input type="hidden" id="flag" name="flag" value="${flag}">
</body>
</html>