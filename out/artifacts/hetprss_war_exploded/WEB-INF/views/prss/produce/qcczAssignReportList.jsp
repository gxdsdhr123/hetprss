<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
	<head>
		<title>清仓操作组勤务报告</title>
		<meta name="decorator" content="default" />
		<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
		<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
		<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
		<script type="text/javascript" src="${ctxStatic}/prss/produce/qcczAssignReportList.js"></script>
	</head>
	<body>
		<div id="container">
			<form class="layui-form">
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label">开始日期</label>
						<div class="layui-input-inline">
							<input type='text' maxlength="20" name='beginTime' id="beginTime"
								placeholder='请选择日期' class='form-control'
								onclick="WdatePicker({dateFmt:'yyyyMMdd',maxDate:'#F{$dp.$D(\'endTime\')}'});" />
						</div>
					</div>
					<div class="layui-inline">
						<label class="layui-form-label">结束日期</label>
						<div class="layui-input-inline">
							<input type='text' maxlength="20" name='endTime' id="endTime"
								placeholder='请选择日期' class='form-control'
								onclick="WdatePicker({dateFmt:'yyyyMMdd',minDate:'#F{$dp.$D(\'beginTime\')}'});" />
						</div>
					</div>
					<div class="layui-inline  pull-right" id="toolbar">
						<button id="btnSubmit" class="layui-btn layui-btn-small layui-btn-primary search" type="button">
						 	<i class="fa fa-search">&nbsp;</i>查询
						</button>
						<button id="add" class="layui-btn layui-btn-small layui-btn-primary" type="button">
							<i class="fa fa-pencil-square-o">&nbsp;</i>增加单据
						</button>
						<button id="modify" class="layui-btn layui-btn-small layui-btn-primary" type="button">
							<i class="fa fa-pencil-square-o">&nbsp;</i>修改单据
						</button>
						<button id="delete" class="layui-btn layui-btn-small layui-btn-primary" type="button">
							<i class="fa fa-pencil-square-o">&nbsp;</i>删除单据
						</button>
						<button id="down" class="layui-btn layui-btn-small layui-btn-primary" type="button">
							<i class="fa fa-download">&nbsp;</i>下载
						</button>
					</div>
				</div>
			</form>
			<form id="printForm" action="${ctx}/produce/qcczAssignReport/print" method="post" style="display: none">
				<input type="hidden" name="id" />
			</form>
			<div id="baseTables">
				<table id="baseTable"></table>
			</div>
		</div>
	</body>
</html>