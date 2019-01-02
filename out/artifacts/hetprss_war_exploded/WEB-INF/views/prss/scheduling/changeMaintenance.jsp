<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>机务备注</title>
<script type="text/javascript"
	src="${ctxStatic}/prss/scheduling/changeMaintenance.js"></script>
	<script type="text/javascript"
	src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<style type="text/css">
body {
    min-height: 100px !important;
}
</style>
</head>
<body>
	<form id="form" action="" class="layui-form">
		<div class="layui-form-item">
			<div class="layui-block" style="padding: 0px 20px 0px 20px;">
				<textarea id="value" name="value" class="layui-textarea" 
				style="width: 440px; height: 173px;">${ATTR_VAL}</textarea>
			</div>
		</div>
		<input type="hidden" class="layui-input" name="field" id="field" value="${field}" >
		<input type="hidden" class="layui-input" name="fltid" id="fltid" value="${fltid}" >
		<input type="hidden" class="layui-input" name="attrId" id="attrId" value="${attrId}" >
	</form>
</body>
</html>