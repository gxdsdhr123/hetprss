<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>作业人设置</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function(){
		$("#userGrid tbody tr").click(function(){
			$(this).find("td:eq(0) input[name=personRadio]").prop("checked",true);
		});
	});
</script>
<body>
	<table class="layui-table" id="userGrid">
		<thead>
			<tr>
				<th width="50">&nbsp;</th>
				<th>序号</th>
				<th>作业人</th>
				<th>班组</th>
				<th>任务数</th>
				<th>状态</th>
				<th>开始时间1</th>
				<th>结束时间1</th>
				<th>开始时间2</th>
				<th>结束时间2</th>
				<th>开始时间3</th>
				<th>结束时间3</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${persons}" var="person" varStatus="status">
				<tr style="cursor: pointer;">
					<td><input type="radio" name="personRadio" data-name="${person.name}" value="${person.workerId}"></td>
					<td>${status.index+1}</td>
					<td>${person.name}</td>
					<td>${person.groupName}</td>
					<td>${person.num}</td>
					<td>${person.workState==0?'空闲':'忙碌'}</td>
					<td>${person.sTime1}</td>
					<td>${person.eTime1}</td>
					<td>${person.sTime2}</td>
					<td>${person.eTime2}</td>
					<td>${person.sTime3}</td>
					<td>${person.eTime3}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>