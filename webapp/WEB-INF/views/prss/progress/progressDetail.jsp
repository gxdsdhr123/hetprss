<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<c:forEach items="${fltTasks}" var="task">
	<div class="task-item">
		<table>
			<tr style="border-bottom: 1px solid #888;color:#eee">
				<td colspan="3">
					<c:choose>
						<c:when test="${task.alarm eq 3}">
							<i class="fa fa-exclamation-circle text-red">&nbsp;</i>${task.name}
						</c:when>
						<c:when test="${task.alarm eq 2}">
							<i class="fa fa-exclamation-circle text-orange">&nbsp;</i>${task.name}
						</c:when>
						<c:when test="${task.alarm eq 1}">
							<i class="fa fa-exclamation-circle" style="color:#e6f312">&nbsp;</i>${task.name}
						</c:when>
						<c:otherwise>
							<i class="fa fa-circle" style="color:#2C5961">&nbsp;</i>${task.name}
						</c:otherwise>
					</c:choose>
					<c:if test="${not empty task.nodeState}">【${task.nodeState}】</c:if>
				</td>
				<td>${task.operator}</td>
			</tr>
			<c:forEach items="${task.nodes}" var="node">
				<c:if test="${not empty node.nodeName}">
					<tr style="color:#eee">
						<td style="padding-left:15px;">预计${node.nodeName}:</td>
						<td style="padding-left:10px;">${node.eTime}</td>
						<td style="padding-left:10px;">实际${node.nodeName}:</td>
						<td style="padding-left:10px;">${node.fTime}</td>
					</tr>
				</c:if>
			</c:forEach>
		</table>
	</div>
</c:forEach>
