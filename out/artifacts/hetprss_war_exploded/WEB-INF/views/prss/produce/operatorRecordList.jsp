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
			<input type="hidden" id="reskind" value="${reskind }" >
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">开始日期</label>
					<div class="layui-input-inline">
						<input type='text' maxlength="20" name='beginTime' id='beginTime'
							placeholder='请选择日期' class='form-control'
							onclick="WdatePicker({dateFmt:'yyyyMMdd',maxDate:'#F{$dp.$D(\'endTime\')}'});" />
					</div>
					<label class="layui-form-label">结束日期</label>
					<div class="layui-input-inline">

						<input type='text' maxlength="20" name='endTime' id='endTime'
							placeholder='请选择日期' class='form-control'
							onclick="WdatePicker({dateFmt:'yyyyMMdd',minDate:'#F{$dp.$D(\'beginTime\')}'});" />
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">人员</label>
					<div class="layui-input-inline">
						<input name="operator_name" class="layui-input" type="text">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">车号</label>
					<div class="layui-input-inline">
						<input name="inner_number" class="layui-input" type="text">
					</div>
				</div>
				<div class="layui-inline  pull-right" id="toolbar">
					<button class="layui-btn layui-btn-small layui-btn-primary search" type="button">
						<i class="fa fa-search">&nbsp;</i>查询
					</button>
					<button id="query" class="layui-btn layui-btn-small layui-btn-primary" type="button">
						 <i class="fa fa-pencil-square-o">&nbsp;</i>查看
					</button>
					<button id="down" class="layui-btn layui-btn-small layui-btn-primary" type="button">
					 <i class="fa fa-download">&nbsp;</i>打印
					</button>
				</div>
			</div>
		</form>
		<form id="printForm" action="${ctx}/produce/operator/print" method="post" style="display: none">
			<input type="hidden" name="bondId" />
			<input type="hidden" name="reskind" value="${reskind }" >
		</form>
		<div id="baseTables">
			<table id="baseTable"></table>
		</div>
	</div>
	<script type="text/javascript"
		src="${ctxStatic}/prss/produce/operatorRecordList.js"></script>
</body>
</html>