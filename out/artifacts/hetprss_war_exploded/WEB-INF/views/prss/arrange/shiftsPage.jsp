<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<title>班制</title>
<script type="text/javascript">
var type="${type}";
var id1="${id1}";
var id2="${id2}";
var id3="${id3}";
</script>
</head>
<body>
<div style="padding:10px">
	<table id="dataGrid"></table>
</div>
	<script type="text/javascript" src="${ctxStatic}/prss/arrange/shiftsPage.js"></script>
</body>
</html>