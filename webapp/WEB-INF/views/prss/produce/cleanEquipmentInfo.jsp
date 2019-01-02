<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>员工操作记录</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css"
	rel="stylesheet" />
<script type="text/javascript"
	src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
	<style type="text/css">
		td{
			text-align: center;
		}
	</style>
</head>
<body>
	<div id="container">
		<form class="layui-form">
		<input type="hidden" id="id" value="${id }"/>
		<input type="hidden" id="typeId" value="${typeId }"/>
			<table id="updateTable" class="layui-table tree_table">
			</table>
			<table id="tTable" class="layui-table tree_table"></table>
			<table id="zTable" class="layui-table tree_table"></table>
			<table id="gzTable" class="layui-table tree_table"></table>
			<table id="tpTable" class="layui-table tree_table">
<%-- 				<c:forEach items="${fileIds}" var="fileId"> --%>
<%-- 					<img src="${ctx}/clean/equipment/pic?fileId=${fileId}" class="procPic" /> --%>
<%-- 				</c:forEach> --%>
			</table>
		</form>
		<form id="downForm" action="${ctx}/clean/equipment/download" method="post" style="display: none">
			<input type="hidden" name="fltid" />
			<input type="hidden" name="type" />
		</form>
	</div>
	<script type="text/javascript"
		src="${ctxStatic}/prss/produce/cleanEquipmentInfo.js"></script>
</body>
</html>