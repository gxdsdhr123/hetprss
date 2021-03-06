<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>收费单管理</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript"
	src="${ctxStatic}/prss/produce/billList.js"></script>
</head>
<body>
	<div id="toolbar">
		<button id="newBtn" type="button" class="btn btn-link">新增单据</button>
		<button id="editBtn" type="button" class="btn btn-link">修改单据</button>
		<button id="delBtn" type="button" class="btn btn-link">删除单据</button>
		<button id="downloadBtn" type="button" class="btn btn-link">下载</button>
	</div>
	<table id="billGrid"></table>
	<form id="printForm" action="${ctx}/produce/bill/print" method="post" style="display: none">
			<textarea id="printTitle" name="title"></textarea>
			<textarea id="idDou" name="id"></textarea>
			<textarea id="type" name="type"></textarea>
			<textarea id="tableName" name="tableName"></textarea>
	</form>
	
	
</body>
</html>