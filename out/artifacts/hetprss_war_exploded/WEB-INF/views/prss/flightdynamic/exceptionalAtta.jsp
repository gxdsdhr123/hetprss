<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>异常情况附件查看</title>
</head>
<body>
	<c:choose>
		<c:when test="${type==1}">
			<c:forEach items="${fileIds}" var="fileId">
				<img src="${ctx}/flightDynamic/readExpAtta?fileId=${fileId}" style="width:100%;height:400px;margin-bottom:5px;"/>
			</c:forEach>
		</c:when>
		<c:when test="${type==2}">
			<c:forEach items="${fileIds}" var="fileId">
				<video width="100%" height="40" controls="controls">
				    <source src="${ctx}/flightDynamic/readExpAtta?fileId=${fileId}" type="video/mp4" />
				    <source src="${ctx}/flightDynamic/readExpAtta?fileId=${fileId}" type="video/ogg" />
				    <source src="${ctx}/flightDynamic/readExpAtta?fileId=${fileId}" type="video/webm" />
				</video>
				<br>
			</c:forEach>
		</c:when>
		<c:when test="${type==3}">
			<c:forEach items="${fileIds}" var="fileId">
				<video width="100%" height="440" controls="controls">
				    <source src="${ctx}/flightDynamic/readExpAtta?fileId=${fileId}" type="video/mp4" />
				    <source src="${ctx}/flightDynamic/readExpAtta?fileId=${fileId}" type="video/ogg" />
				    <source src="${ctx}/flightDynamic/readExpAtta?fileId=${fileId}" type="video/webm" />
				</video>
				<br>
			</c:forEach>
		</c:when>
	</c:choose>
</body>
</html>