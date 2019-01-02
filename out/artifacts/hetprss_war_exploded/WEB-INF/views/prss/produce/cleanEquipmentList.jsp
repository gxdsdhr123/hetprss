<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>清仓设备组勤务报告</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<%@include file="/WEB-INF/views/prss/flightdynamic/flightDynFilter.jsp"%>
<body>
	<div id="container">
		<form class="layui-form">
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">开始日期</label>
					<div class="layui-input-inline">
						<input type='text' maxlength="20" name='begin' id="begin"
							placeholder='请选择日期' class='form-control'
							onclick="WdatePicker({dateFmt:'yyyyMMdd',maxDate:'#F{$dp.$D(\'end\')}'});" />
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">结束日期</label>
					<div class="layui-input-inline">
						<input type='text' maxlength="20" name='end' id="end"
							placeholder='请选择日期' class='form-control'
							onclick="WdatePicker({dateFmt:'yyyyMMdd',minDate:'#F{$dp.$D(\'begin\')}'});" />
					</div>
				</div>
				<div class="layui-inline  pull-right" id="toolbar">
					<button id="btnSubmit" class="layui-btn layui-btn-small layui-btn-primary search" type="button">
					 	<i class="fa fa-search">&nbsp;</i>查询
					</button>
					<button id="modify" class="layui-btn layui-btn-small layui-btn-primary" type="button">
						 <i class="fa fa-pencil-square-o">&nbsp;</i>修改
					</button>
					<button id="down" class="layui-btn layui-btn-small layui-btn-primary" type="button">
					 <i class="fa fa-download">&nbsp;</i>下载
					</button>
				</div>
			</div>
		</form>
		<form id="printForm" action="${ctx}/clean/equipment/print" method="post" style="display: none">
			<input type="hidden" name="id" />
		</form>
		<div id="baseTables">
			<table id="baseTable"></table>
		</div>
	</div>
	<script type="text/javascript" src="${ctxStatic}/prss/produce/cleanEquipmentList.js"></script>
</body>
</html>