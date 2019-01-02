<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>航班动态-值机柜台默认设置</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/prss/flightdynamic/css/flightDynSet.css" rel="stylesheet" />
<style type="text/css">
.fixed-table-loading{
	top:0px !important;
}
.fixed-table-toolbar .columns-right {
	padding: 10px 0px;
}
.search {
	padding: 10px;
}
</style>
</head>
<body>
	<div class="layui-fluid" style="height: 100%;">
		<form class="layui-form tool-bar" action="">
			  <div class="layui-inline" style="margin-left:20px">
			    <button id="add" class="layui-btn" type="button" style="width:100px">增加</button>
			  </div>
			  <div class="layui-inline" style="margin-left:20px">
			    <button id="delete" class="layui-btn" type="button" style="width:100px">删除</button>
			  </div>
		</form>
		<div class="container-fluid" style="height:calc(100% - 63px);position:relative">
			<table id="baseTable"></table>
		</div>
	</div>
	<script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/counterDefultAllotTable.js"></script>
</body>
</html>