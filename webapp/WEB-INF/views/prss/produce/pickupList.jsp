<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>失物招领单管理</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${ctxStatic}/prss/produce/pickupList.js"></script>
<style type="text/css">
	#container{
		margin-top:10px;
	}
</style>
</head>
<body>
	<div id="container">
		<form id="printForm" method="post" action="${ctx}/produce/pickup/printPickup">
			<input type="hidden" name="ids" id="ids"/>
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">开始日期</label>
					<div class="layui-input-inline">
						<input type="text" name="startDate" id="startDate" class="layui-input"
							onclick="WdatePicker({dateFmt:'yyyyMMdd',maxDate:'%y-%M-{%d}'});" value="" 
							placeholder="请选择开始日期"/>
					</div>

				</div>
				<div class="layui-inline">
					<label class="layui-form-label">结束日期</label>
					<div class="layui-input-inline"> 
						<input type="text" name="endDate" id="endDate" class="layui-input" 
							onclick="WdatePicker({dateFmt:'yyyyMMdd',maxDate:'%y-%M-{%d}',minDate:'#F{$dp.$D(\'startDate\')}'});"  value=""
							placeholder="请选择结束日期"/>
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">搜索</label>
					<div class="layui-input-inline"> 
						<input type="text" name="searchtext" id="searchtext" class="layui-input"   value=""/>
					</div>
				</div>
				<div class="layui-inline  pull-right" id="toolbar">
					<button id="searchBtn" type="button" class="btn btn-link"><i class="fa fa-search">&nbsp;</i>查询</button>
					<button id="newBtn" type="button" class="btn btn-link">新建</button>
					<button id="editBtn" type="button" class="btn btn-link">修改</button>
					<button id="delBtn" type="button" class="btn btn-link">删除</button>
					<button id="downloadBtn" type="button" class="btn btn-link">下载</button>
				</div>
			</div>
		</form>
		<div>
			<table id="pickupGrid"></table>
		</div>
	</div>
	<div id="pic-table-pop" style="display: none;padding:10px;">
		<table id="pic-table"></table>
	</div>
	<a id="saveImg" style="display:none;"></a>
</body>
</html>