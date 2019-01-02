<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>报文管理</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css"
	rel="stylesheet" />
<script type="text/javascript"
	src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
</head>
<body>
	<div class="row">
		<div class="col-md-12 col-xs-12 col-sm-12" style="padding-left: 0px;">
			<div class="box-body">
				<div class="layui-inline">
					<label class="layui-form-label">航班日期</label>
					<div class="layui-input-inline">
						<div class="input-group">
							<input type='text' maxlength="20" name='flightdate'
								placeholder='请选择日期' class='form-control'
								onclick="WdatePicker({dateFmt:'yyyyMMdd'});" />						
						</div>
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">航班号</label>
					<div class="layui-input-inline">
						<input class="layui-input" type="text" name="flightnumber">
					</div>
				</div>
				
				<div class="layui-inline">
					<label class="layui-form-label">机号</label>
					<div class="layui-input-inline">
						<input class="layui-input" type="text" name="aircraft">
					</div>
				</div>
				<div class="bs-bars pull-right">
					<div id="toolbar">
						<button type="button" class="btn btn-link" onclick="refresh();">查询</button>
						<button type="button" class="btn btn-link analysis">手动解析</button>
					</div>
				</div>
				<table id="baseTable"
					class="table table-striped table-bordered table-hover"></table>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="${ctxStatic}/prss/telegraph/autoanalysislist.js"></script> 
</body>
</html>