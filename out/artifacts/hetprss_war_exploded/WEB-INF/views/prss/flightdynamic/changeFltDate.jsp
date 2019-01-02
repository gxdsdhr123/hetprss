<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>属性变更</title>
<script type="text/javascript"
	src="${ctxStatic}/prss/flightdynamic/changeFltDate.js"></script>
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" type="text/css">
</head>
<body>
	<form id="form" action="" class="layui-form">
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">航班日期旧值</label>
				<div class="layui-input-inline" >
					<input type="text" class="layui-input" name="oldValue" id="oldValue" value="${oldValue}" readonly>
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">航班日期新值</label>
				<div class="layui-input-inline" >
					<input id="newValue" class="layui-input" type="text" 
								onfocus="WdatePicker({dateFmt:'yyyyMMdd',startDate:'%y-%M-%d'})">
				</div>
			</div>
		</div>
		<input type="hidden" class="layui-input" name="fltid" id="fltid" value="${fltid}" >

	</form>
	<iframe id="frame" name="frame" width="100%" height="200px" frameborder="no"> </iframe>
</body>
</html>