<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>登机时间</title>
<script type="text/javascript"
	src="${ctxStatic}/prss/flightdynamic/changeBrdTime.js"></script>
	<script type="text/javascript"
	src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
</head>
<body>
	<form id="form" action="" class="layui-form">
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">旧值</label>
				<div class="layui-input-inline" >
					<input type="text" id="oldValue" name="oldValue" class="layui-input" 
					value="${oldValue}" readonly="readonly">
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">新值</label>
				<div class="layui-input-inline" >
					<input type="text" id="newValue" name="newValue" class="layui-input" >
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">日期</label>
				<div class="layui-input-inline" >
					<input type="text" id="date" name="date" class="layui-input" value="${today }"
					onclick="WdatePicker({dateFmt:'yyyyMMdd'});" >
				</div>
			</div>
		</div>
		<input type="hidden" class="layui-input" name="field" id="field" value="${field}" >
		<input type="hidden" class="layui-input" name="fltid" id="fltid" value="${fltid}" >
	</form>
</body>
</html>