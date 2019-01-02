<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>特车统计</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" type="text/css">

<body>
	<div id="container" style="padding-top:30px">
		<form  id="listForm"  action="${ctx}/tc/statistics/exportExcel" method="post">
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">开始日期</label>
					<div class="layui-input-inline" style="width: 100px;">
						<input id="dateStart" name="dateStart" class="layui-input" type="text" value="${defaultStart}"
						 onfocus="WdatePicker({dateFmt:'yyyy.MM.dd',startDate:'%y-%M-{%d-1}'})">
					</div>
					<div class="layui-form-mid">结束日期</div>
					<div class="layui-input-inline" style="width: 100px;" >
						<input id="dateEnd" name="dateEnd" class="layui-input" type="text" value="${defaultEnd}"
						 onfocus="WdatePicker({dateFmt:'yyyy.MM.dd',startDate:'%y-%M-{%d-1}'})">
					</div>
				</div>
				<div class="layui-inline  pull-right" id="toolbar">
					<button id="btnSubmit" class="layui-btn layui-btn-small layui-btn-primary search" type="button">
					 	<i class="fa fa-search">&nbsp;</i>筛选
					</button>
					<button id="btnCoefficient" class="layui-btn layui-btn-small layui-btn-primary" type="button">
					 系数
					</button>
					<button id="btnChart" class="layui-btn layui-btn-small layui-btn-primary print"  type="button">
					打印
					</button>
				</div>
			</div>
		</form>
		

		<div id="baseTables" style="width:100%;">
			<table id="baseTable"></table>
		</div>
<!-- 		<div style="clear:both;"></div> -->
	</div>
	<script type="text/javascript" src="${ctxStatic}/prss/statisticalanalysis/tcStatisticsList.js"></script>
</body>
</html>