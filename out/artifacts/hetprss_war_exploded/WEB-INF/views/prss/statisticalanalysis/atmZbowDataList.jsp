<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>管制/运行中心导出数据</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" type="text/css">
<style type="text/css">

.list-group-item{
	-moz-user-select: -moz-none;
   	-khtml-user-select: none;
   	-webkit-user-select: none;
   	-ms-user-select: none;
   	user-select: none;
}
		
</style>
<body>
	<div id="container" >
	<form  id="printExcel" action="${ctx}/statisticalanalysis/ATMZBOW/exportExcel" method="post">
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">选择日期</label>
					<div class="layui-input-inline">
						<input type="text" name="date" id="date" class="layui-input"
							onclick="WdatePicker({dateFmt:'yyyyMMdd'});" value="${defaultDate}" />
						<input type="hidden" id="type" name="type" value="${type }"/>
					</div>

				</div>
				<div class="layui-inline  pull-right" id="toolbar">
					<button class="layui-btn layui-btn-small layui-btn-primary search" type="button">
					 	<i class="fa fa-search">&nbsp;</i>查询
					</button>
					<button class="layui-btn layui-btn-small layui-btn-primary print" type="button">
					 	打印
					</button>
				</div>
			</div>
		</form>
		<div id="baseTables" style="width:100%;">
				<table id="baseTable"></table>
		</div>
	</div>
	<script type="text/javascript" src="${ctxStatic}/prss/statisticalanalysis/atmZbowDataList.js"></script>
</body>
</html>