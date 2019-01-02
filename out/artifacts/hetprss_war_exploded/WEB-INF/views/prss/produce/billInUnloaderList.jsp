<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
	<head>
		<title>进港卸机记录单</title>
		<meta name="decorator" content="default" />
		<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
		<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
		<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
		<script type="text/javascript" src="${ctxStatic}/prss/produce/billInUnloaderList.js"></script>
	</head>
	<body>
		<div id="container">
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">航班日期</label>
					<div class="layui-input-inline">
						<input type='text' maxlength="20" name='fltDate' id="fltDate"
							placeholder='请选择日期' class='form-control' value="${fltDate}"
							onclick="WdatePicker({dateFmt:'yyyyMMdd'});" />
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">航班号</label>
					<div class="layui-input-inline">
						<input type='text' maxlength="20" name='fltNum' id="fltNum" class='form-control'/>
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">监装员</label>
					<div class="layui-input-inline">
						<input type='text' maxlength="20" name='operator' id="operator" class='form-control'/>
					</div>
				</div>
				<div class="layui-inline  pull-right" id="toolbar">
					<button id="btnSubmit" class="layui-btn layui-btn-small layui-btn-primary search" type="button">查询</button>&nbsp;&nbsp;
					<button id="btnSub" class="layui-btn layui-btn-small layui-btn-primary sub" type="button">查看</button>
				</div>
			</div>
			<div id="baseTables">
				<table id="baseTable"></table>
			</div>
		</div>
	</body>
</html>