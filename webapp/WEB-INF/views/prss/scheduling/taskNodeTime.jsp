<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>任务节点时间</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<style type="text/css">
	.flight-info tbody tr td{
		font-size:16px;
	}
</style>
</head>
<body>
	<input type="hidden" id="schemaId" value="${schemaId}">
	<input type="hidden" id="inFltId" value="${inFlight.fltid}">
	<input type="hidden" id="outFltId" value="${outFlight.fltid}">
	<input type="hidden" id="hisFlag" value="${hisFlag}">
	<table class="layui-table" id="taskListGrid">
		<thead>
			<tr>
				<th>任务名称</th>
				<th>时间</th>
				<th>作业人</th>
				<th>操作人</th>
				<th>代点时间</th>
				<th>代点原因</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${nodeTimeList}" var="node" varStatus="status">
				<tr style="cursor: pointer;">
					<td>${node.TASK_NAME}</td>
					<td>${node.NODE_TIME}</td>
					<td>${node.OPERATOR_NAME}</td>
					<td>${node.OPERATION_NAME}</td>
					<td>${node.SURROGATE_REASON}</td>
					<td>${node.REMARK}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>