<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/prss/flightdynamic/css/fltProgressMain.css" rel="stylesheet">
<script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/fltProgressMain.js"></script>
</head>
<body>
	<table id="dataGrid" class="table table-hover table-striped"></table>
</body>
</html>