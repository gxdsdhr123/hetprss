<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
<title>车辆保障区域</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<body>
	<div id="tool-box">
		<button id="create" type="button" class="btn btn-link">增加</button>
		<button id="modify" type="button" class="btn btn-link">修改</button>
		<button id="delete" type="button" class="btn btn-link">删除</button>
	</div>
	<table id="areaTable"></table>
	<script type="text/javascript" src="${ctxStatic}/prss/scheduling/schedulingArea.js"></script>
</body>
</html>