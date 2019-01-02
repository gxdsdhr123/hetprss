<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
	<head>
		<title>航班正常率统计表</title>
		<meta name="decorator" content="default" />
		<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
		<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
		<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
		<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
		<script type="text/javascript" src="${ctxStatic}/prss/statisticalanalysis/saFltNormalList.js"></script>
	</head>
	<body>
		<div id="container">
			<form class="layui-form">
				<div class="layui-form-item" id="toolbar">
					<div class="layui-inline" >
						<label class="layui-form-label">开始日期</label>
						<div style="width: 100px" class="layui-input-inline">
							<input style="width: 100px" type='text' maxlength="20" name='startDate' id="startDate"
								placeholder='请选择日期' class='form-control'
								onclick="WdatePicker({dateFmt:'yyyyMMdd',maxDate:'#F{$dp.$D(\'endDate\')}'});" />
						</div>
						<label style="width: 100px"class="layui-form-label">结束日期</label>
						<div style="width: 100px" class="layui-input-inline">
							<input type='text' maxlength="20" name='endDate' id="endDate"
								placeholder='请选择日期' class='form-control'
								onclick="WdatePicker({dateFmt:'yyyyMMdd',minDate:'#F{$dp.$D(\'startDate\')}'});" />
						</div>
						<label style="width: 100px"class="layui-form-label"></label>
						<div style="width: 100px" class="layui-input-inline">
							<select id="inOutFlag" name="inOutFlag" class="form-control select2">
								<option value="" >全部航班</option>
								<option value="A">进港航班</option>
								<option value="D">出港航班</option>
							</select>
						</div>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<div class="layui-btn-group">
							<button id="search" class="layui-btn  layui-btn-primary" type="button">
							 	查询
							</button>
							<button id="down" class="layui-btn  layui-btn-primary" type="button">
								打印
							</button>
						</div>
					</div>
				</div>
			</form>
			<div id="baseTables">
				<table id="baseTable"></table>
			</div>
		</div>
		<!-- 导出excel -->
		<form id="exportForm" method="post">
			<input type="hidden" id="param" name="param">
		</form>
	</body>
</html>