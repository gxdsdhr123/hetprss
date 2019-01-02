<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>手动创建任务</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	var jobTypeSource = ${jobTypeJson};
	var processSource = ${processSource};
</script>
<style type="text/css">
	.flight-info tbody tr td{
		font-size:16px;
	}
</style>
</head>
<body>
	<div style="min-height: 1024px;overflow:auto;">
		<form id="inputForm" action="${ctx}/scheduling/jobManage/save" method="post">
			<input type="hidden" id="schemaId" value="${schemaId}">
			<input type="hidden" id="inFltId" value="${inFlight.fltid}">
			<input type="hidden" id="outFltId" value="${outFlight.fltid}">
			<input type="hidden" id="jobTaskId" value="${jobTaskId}">
			<textarea style="display:none" name="taskList" id="taskList"></textarea>
		</form>
		<table class="layui-table flight-info" lay-skin="nob">
			<tbody>
				<tr>
					<td align="right">进港航班号：</td>
					<td>${inFlight.fltNo}</td>
					<td align="right">起场：</td>
					<td>${inFlight.dAirport}</td>
					<td align="right">预落：</td>
					<td>${inFlight.eta}</td>
					<td align="right">机号：</td>
					<td>${inFlight.actNo}</td>
					<td align="right">机型：</td>
					<td>${inFlight.actType}</td>
				</tr>
				<tr>
					<td align="right">出港航班号：</td>
					<td>${outFlight.fltNo}</td>
					<td align="right">落场：</td>
					<td>${outFlight.aAirport}</td>
					<td align="right">预起：</td>
					<td>${outFlight.etd}</td>
					<td align="right">机位：</td>
					<td>${outFlight.actStand}</td>
					<td align="right">登机口：</td>
					<td>${outFlight.gate}</td>
				</tr>
				<tr>
					<td colspan="2" width="100px" style="font-size:14px;">
						<label>进出港类型：</label> 
						<label style="cursor: pointer; font-weight: normal;"> 
							<input type="checkbox" value="1" name="inOutFlag" ${empty inFlight.fltid?'disabled':''}>&nbsp;进港航班
						</label> 
						<label style="cursor: pointer; font-weight: normal;"> 
							<input type="checkbox" value="2" name="inOutFlag" ${empty outFlight.fltid?'disabled':''}>&nbsp;出港航班
						</label> 
					</td>
					<td colspan="8" style="font-size:14px;">
						<label>作业类型：</label>
						<c:forEach items="${jobTypes}" var="job">
							<label style="cursor: pointer; font-weight: normal;"> <input
								type="checkbox" name="jobType" value="${job.typeCode}">&nbsp;${job.typeName}
							</label>
						</c:forEach>
					</td>
				</tr>
			</tbody>
		</table>
		<div id="toolbar">
			<button id="addRow" type="button" class="btn btn-link">
				<i class="fa fa-plus">&nbsp;</i>新增行
			</button>
			<button id="removeRow" type="button" class="btn btn-link">
				<i class="fa fa-remove">&nbsp;</i>删除
			</button>
		</div>
		<table id="taskGrid" class="table table-hover table-striped"></table>
	</div>
	<script type="text/javascript"
		src="${ctxStatic}/prss/scheduling/jobTaskForm.js"></script>
</body>
</html>