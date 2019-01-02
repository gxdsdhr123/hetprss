<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/snaker.jsp"%>
<title>流程设计器</title>
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<script type="text/javascript"
	src="${ctxStatic}/prss/workflow/processView.js"></script>
</head>
<body>
	<input type="hidden" id="processId" value="${processId}">
	<input type="hidden" id="orderId" value="${order.id}">
	<table class="layui-table">
		<tr>
			<td align=center>任务名称</td>
			<td align=center>任务创建时间</td>
			<td align=center>任务完成时间</td>
			<td align=center>作业人</td>
			<td align=center>操作人</td>
			<td align=center>代点原因</td>
			<td align=center>代点原因描述</td>
		</tr>
		<c:forEach items="${tasks}" var="item">
			<tr>
				<td>
					${item.displayName}&nbsp;
					<c:if test="${not empty rollbackTask[item.id]}">
						<i class="fa fa-flag text-red"></i>
					</c:if>
				</td>
				<td>${item.createTime}&nbsp;</td>
				<td>${item.finishTime}&nbsp;</td>
				<td>${item.operatorName}&nbsp;</td>
				<td>${not empty surrogateRecord[item.id].operation?surrogateRecord[item.id].operation:item.operatorName}&nbsp;</td>
				<td>${surrogateRecord[item.id].reason}&nbsp;</td>
				<td>${surrogateRecord[item.id].remark}&nbsp;</td>
			</tr>
		</c:forEach>
	</table>
	<div class="properties_all" align="center">
		<div id="snakerflow">
		</div>
	</div>
</body>
</html>