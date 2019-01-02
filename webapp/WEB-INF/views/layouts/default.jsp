<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="sitemesh"
	uri="http://www.opensymphony.com/sitemesh/decorator"%>
<!DOCTYPE html>
<html style="overflow-x: auto; overflow-y: auto;">
<head>
<title><sitemesh:title /></title>
<%@include file="/WEB-INF/views/include/head.jsp"%>
<sitemesh:head />
<script type='text/javascript'>
	if (layui) {
		layui.use('util', function() {
			if (layui.util) {
				var util = layui.util;
				util.fixbar();
			}
		})
	}
</script>
</head>
<body>
	<sitemesh:body />
</body>
</html>
