<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>异常情况查看</title>
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/exceptional.js"></script>
</head>
<body>
	<input type="hidden" id="fltid" value="${fltid}">
	<input type="hidden" id="hisFlag" value="${hisFlag}">
	<input type="hidden" id="hisDate" value="${hisDate}">
	<input type="hidden" id="user" value="${fns:getUser()}">
	<div id="toolbar">
		<form class="layui-form" action="">
			<input type="checkbox" id="img" name="img" title="图片" lay-filter="img">
			<input type="checkbox" id="vol" name="vol" title="语音" lay-filter="vol">
			<input type="checkbox" id="vid" name="vid" title="视频" lay-filter="vid">
		</form>
	</div>
	<table id="errorTable"></table>
</body>
</html>