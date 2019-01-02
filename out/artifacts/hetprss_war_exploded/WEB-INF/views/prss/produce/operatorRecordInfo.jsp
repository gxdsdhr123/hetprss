<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>员工操作记录</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css"
	rel="stylesheet" />
<script type="text/javascript"
	src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
	<style type="text/css">
		td{
			text-align: center;
		}
		.layui-table{
			margin: 0px;
		}
	</style>
</head>
<body>
	<div id="container">
		<input name="bondId" value="${bondId }" type="hidden" />
		<input type="hidden" id="reskind" value="${reskind }" >
		<div id="baseTables"></div>
	</div>
	<script type="text/javascript"
		src="${ctxStatic}/prss/produce/operatorRecordInfo.js"></script>
</body>
</html>