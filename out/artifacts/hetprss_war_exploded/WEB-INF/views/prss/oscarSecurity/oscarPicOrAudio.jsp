<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>OSCAR图片或音频播放</title>
</head>
<body>
	<c:choose>
		<c:when test="${sign == 1}">
			<c:forEach items="${showList}" var="fileId">
				<img src="${ctx}/oscarrSecurity/report/outputPicture?id=${fileId.FILEPATH}" style="width:100%;height:400px;margin-bottom:5px;"/>
			</c:forEach>
		</c:when>
		<c:when test="${sign == 2}">
			<c:forEach items="${showList}" var="fileId">
				<video width="100%" height="40" controls="controls">
				    <source src="${ctx}/oscarrSecurity/report/outputPicture?id=${fileId.FILEPATH}" type="video/mp4" />
				    <source src="${ctx}/oscarrSecurity/report/outputPicture?id=${fileId.FILEPATH}" type="video/ogg" />
				    <source src="${ctx}/oscarrSecurity/report/outputPicture?id=${fileId.FILEPATH}" type="video/webm" />
				</video>
				<br>
			</c:forEach>
		</c:when>
		<c:when test="${sign == 3}">
			<c:forEach items="${showList}" var="fileId">
				<video width="100%" height="440" controls="controls">
				    <source src="${ctx}/oscarrSecurity/report/outputPicture?id=${fileId.FILEPATH}" type="video/mp4" />
				    <source src="${ctx}/oscarrSecurity/report/outputPicture?id=${fileId.FILEPATH}" type="video/ogg" />
				    <source src="${ctx}/oscarrSecurity/report/outputPicture?id=${fileId.FILEPATH}" type="video/webm" />
				</video>
				<br>
			</c:forEach>
		</c:when>
		<c:when test="${sign == 9}">
			<c:forEach items="${showList}" var="fileId">
				<c:if test="${fileId.FILETYPE == 1}">
					<img src="${ctx}/oscarrSecurity/report/outputPicture?id=${fileId.FILEPATH}" style="width:100%;height:400px;margin-bottom:5px;"/>
					<br>
				</c:if>
				<c:if test="${fileId.FILETYPE == 2}">
					<video width="100%" height="40" controls="controls">
					    <source src="${ctx}/oscarrSecurity/report/outputPicture?id=${fileId.FILEPATH}" type="video/mp4" />
					    <source src="${ctx}/oscarrSecurity/report/outputPicture?id=${fileId.FILEPATH}" type="video/ogg" />
					    <source src="${ctx}/oscarrSecurity/report/outputPicture?id=${fileId.FILEPATH}" type="video/webm" />
					</video>
					<br>
				</c:if>
				<c:if test="${fileId.FILETYPE == 3}">
					<video width="100%" height="440" controls="controls">
					    <source src="${ctx}/oscarrSecurity/report/outputPicture?id=${fileId.FILEPATH}" type="video/mp4" />
					    <source src="${ctx}/oscarrSecurity/report/outputPicture?id=${fileId.FILEPATH}" type="video/ogg" />
					    <source src="${ctx}/oscarrSecurity/report/outputPicture?id=${fileId.FILEPATH}" type="video/webm" />
					</video>
				</c:if>
				<br>
			</c:forEach>
		</c:when>
	</c:choose>
</body>
</html>