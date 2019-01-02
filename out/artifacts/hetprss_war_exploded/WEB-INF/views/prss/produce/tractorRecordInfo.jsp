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
			width: 9%;
		}
	</style>
</head>
<body>
	<div id="container">
		<form class="layui-form">
		<input type="hidden" id="id" value="${id }"/>
			<div id="baseTables">
				<table id="updateTable" class="layui-table tree_table">
				</table>
				<table id="tTable" class="layui-table tree_table"></table>
				<table id="zTable" class="layui-table tree_table"></table>
				<table id="gzTable" class="layui-table tree_table">
				    <thead>
					    <tr>
	                        <th>序号</th>
                            <th>接车/收车</th>
                            <th>故障描述</th>
                            <th>拍照</th>
                            <th>录像</th>
                            <th>视频</th>
	                    </tr>
				    </thead>
					<tbody></tbody>
				</table>
			</div>
		</form>
		<form id="downForm" action="${ctx}/produce/tractor/download" method="post" style="display: none">
			<input type="hidden" name="fltid" />
			<input type="hidden" name="type" />
		</form>
	</div>
	<script type="text/javascript"
		src="${ctxStatic}/prss/produce/tractorRecordInfo.js"></script>
</body>
</html>