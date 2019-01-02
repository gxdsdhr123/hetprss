<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>消息订阅</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>

<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<style>
</style>
</head>
<body>
	<div class="row">
		<div class="col-md-12 col-xs-12 col-sm-12">
			<div class="box-body">
				<div class="layui-inline">
					<label class="layui-form-label">模板名称</label>
					<div class="layui-input-inline">
						<input class="layui-input" type="text" name="tg_name">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">创建时间</label>
					<div class="layui-input-inline">
						<input type='text' maxlength="20" name='crtime'
							placeholder='请选择日期' class='form-control'
							onclick="WdatePicker({dateFmt:'yyyyMMdd'});" />
					</div>
				</div>
				<div class="bs-bars pull-right">
					<div id="toolbar">
						<button class="btn btn-link search" type="button">查询</button>
						<button class="btn btn-link " type="button" id="create">新增</button>
						<button id="remove" type="button" class="btn btn-link">删除</button>
					</div>
				</div>
				<table id="baseTable"
					class="table table-striped table-bordered table-hover"></table>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="${ctxStatic}/prss/telegraph/telegraphautomain.js"></script>
</body>
</html>