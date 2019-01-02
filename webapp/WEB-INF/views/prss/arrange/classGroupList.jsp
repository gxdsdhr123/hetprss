<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>保障类型定义回收站</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/head.jsp"%>
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/prss/flightdynamic/css/flightDynList.css"
	rel="stylesheet" />
<body>
	<div id="tool-box">
		<div class="btn-group">
			<button id="add" type="button" class="btn btn-link">增加</button>
			<button id="modify" type="button" class="btn btn-link">修改</button>
			<button id="del" type="button" class="btn btn-link">删除</button>
		</div>
	</div>
	<div id="baseTables">
		<table id="baseTable"></table>
	</div>
	<script>
		layui.use([ 'element', 'layer' ], function() {
			var element = layui.element;
			var layer = layui.layer;

		});
	</script>
	<script type="text/javascript"
		src="${ctxStatic}/bootstrap/plugins/bootstrap-table/extensions/bootstrap-table-contextmenu.min.js"></script>
	<script type="text/javascript"
		src="${ctxStatic}/prss/arrange/classGroupList.js"></script>
</body>
</html>