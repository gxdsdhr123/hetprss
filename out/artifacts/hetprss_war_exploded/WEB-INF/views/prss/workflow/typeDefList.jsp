<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>保障类型定义</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/head.jsp"%>
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/prss/workflow/css/typeDefList.css"
	rel="stylesheet" />
<body>
	<div class="layui-side">
		<div class="layui-side-scroll" style="background: #021E44">
			<div class="layui-collapse" lay-filter="test">
				<div class="layui-colla-item">
					<h5 class="layui-colla-title">保障类型</h5>
				</div>
			</div>
		</div>
	</div>
	<div class="layui-body">
		<div id="tool-box">
			<button id="addType" type="button" class="btn btn-link">增加保障类型</button>
			<button id="modifyType" type="button" class="btn btn-link">修改保障类型</button>
			<button id="delType" type="button" class="btn btn-link">删除保障类型</button>
			<button id="addWork" type="button" class="btn btn-link">增加作业</button>
			<button id="modifyWork" type="button" class="btn btn-link">修改作业</button>
			<button id="delWork" type="button" class="btn btn-link">删除作业</button>
		</div>
		<div id="baseTables">
			<table id="baseTable"></table>
		</div>
	</div>


	<script>
		layui.use([ 'element', 'layer' ], function() {
			var element = layui.element;
			var layer = layui.layer;

			//监听折叠
			element.on('collapse(test)', function(data) {
				/* layer.msg('展开状态：' + data.show); */
			});
		});
	</script>
	<script type="text/javascript"
		src="${ctxStatic}/bootstrap/plugins/bootstrap-table/extensions/bootstrap-table-contextmenu.min.js"></script>
	<script type="text/javascript"
		src="${ctxStatic}/prss/workflow/typeDefList.js"></script>
</body>
</html>