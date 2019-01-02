<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<title>要客导入</title>
<script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/fdVipHis.js"></script>
</head>
<body>
	<div id="toolbar">
		<div class="layui-inline">
			<label class="layui-form-label" style="padding: 10px 10px !important;margin-bottom: 0px;">数据日期</label>
			<div class="layui-input-inline">
				<input id="hisdate" name="hisdate" onClick="dateFilter();" class="layui-input" readonly="readonly"
					value="" type="text">
			</div>
		</div>
	</div>
	<table id="fdVipImportGrid"></table>
</body>
</html>