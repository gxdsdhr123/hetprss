<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>客舱清洁月工作量统计</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" type="text/css">
<body>
	<div id="container" style="padding-top:30px">
		<form  id="listForm"  action="${ctx}/kcqj/statistics/exportMonthlyExcel" method="post">
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">年</label>
					<div class="layui-input-inline" style="width: 100px;">
						<input id="year" name="year" class="layui-input" type="text" value="${year}"
						 onfocus="WdatePicker({dateFmt:'yyyy'})">
					</div>
					<div class="layui-form-mid">月</div>
					<div class="layui-input-inline" style="width: 100px;" >
						<input id="month" name="month" class="layui-input" type="text" value="${month}"
						 onfocus="WdatePicker({dateFmt:'MM'})">
					</div>
					
				</div>
				<div class="layui-inline  pull-right" id="toolbar">
					<button id="btnSubmit" class="layui-btn layui-btn-small layui-btn-primary search" type="button">
					 	<i class="fa fa-search">&nbsp;</i>筛选
					</button>
					<button id="btnChart" class="layui-btn layui-btn-small layui-btn-primary print"  type="button">
					导出
					</button>
				</div>
			</div>
		</form>
		

		<div id="baseTables" style="width:100%;">
			<table id="baseTable"></table>
		</div>
	</div>
	<script type="text/javascript" src="${ctxStatic}/prss/statisticalanalysis/kcqjMonthlyStatsList.js"></script>
</body>
</html>