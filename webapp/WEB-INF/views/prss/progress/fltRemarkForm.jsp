<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>航班备注</title>
<meta name="decorator" content="default" />
</head>
<body>
	<c:choose>
		<c:when test="${not empty error}">
			<div style="padding:10px;">
				<i class="fa fa-warning" style="color:yellow"></i>${error}
			</div>
		</c:when>
		<c:otherwise>
			<form id="inputForm" action="${ctx}/scheduling/progress/saveFltRemark" class="layui-form" method="post">
				<input type="hidden" name="id" id="id" value="${remarkInfo.id}">
				<input type="hidden" name="fltid" id="fltid" value="${remarkInfo.fltid}">
				<input type="hidden" name="attrId" id="attrId" value="${remarkInfo.attrId}">
				<div class="layui-form-item layui-form-text">
				    <textarea class="layui-textarea" name="attrValue" style="height:250px" placeholder="请输入内容">${remarkInfo.value}</textarea>
				 </div>
			</form>
		</c:otherwise>
	</c:choose>
	<script type="text/javascript"
		src="${ctxStatic}/prss/progress/fltRemarkForm.js"></script>
</body>
</html>