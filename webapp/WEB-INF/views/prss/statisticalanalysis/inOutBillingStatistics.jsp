<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>旅客服务航延信息统计</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" type="text/css">
<style type="text/css">
	.bootstrap-table .table > thead > tr > th{
		vertical-align: middle;
	}
	#container{
		margin-top:10px;
	}
	.fixed-table-loading{
		position: sticky;
		top: 0px !important;
		height: 100%;
	}
</style>
<body>
	<div id="container">
		<form id="printForm" method="post" action="${ctx}/inOutBillingStatistics/print">
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">开始日期</label>
					<div class="layui-input-inline">
						<input type="text" name="startDate" id="startDate" class="layui-input"
							onclick="WdatePicker({dateFmt:'yyyyMMdd',maxDate:'%y-%M-{%d-1}'});" value="" />
					</div>

				</div>
				<div class="layui-inline">
					<label class="layui-form-label">结束日期</label>
					<div class="layui-input-inline"> 
						<input type="text" name="endDate" id="endDate" class="layui-input" 
							onclick="WdatePicker({dateFmt:'yyyyMMdd',maxDate:'%y-%M-{%d-1}',minDate:'#F{$dp.$D(\'startDate\')}'});"  value=""/>
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">范围</label>
					<div class="layui-input-inline"> 
						<select name="inOut" id="inOut" class="form-control">
						  <option value="A">进港</option>
						  <option value="D">出港</option>
						</select>
					</div>
				</div>
				<div class="layui-inline  pull-right" id="toolbar">
					<button id="btnSubmit" class="layui-btn layui-btn-small layui-btn-primary search" type="button" onclick="_search()">
					 	<i class="fa fa-search">&nbsp;</i>查询
					</button>
					<button id="addBtn" class="layui-btn layui-btn-small layui-btn-primary print" type="submit">
					 	打印
					</button>
				</div>
			</div>
		</form>
		<div>
			<table id="dataTable"></table>
		</div>
	</div>
	<script type="text/javascript" src="${ctxStatic}/prss/statisticalanalysis/inOutBillingStatistics.js"></script>
</body>
</html>