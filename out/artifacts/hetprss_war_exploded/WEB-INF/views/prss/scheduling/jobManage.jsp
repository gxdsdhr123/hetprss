<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>作业管理</title>
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
	<input type="hidden" id="jobKind" value="${jobKind}">
	<div id="tool-box">
		<button id="refreshBtn" type="button" class="btn btn-link">刷新</button>
		<button id="createBtn" type="button" class="btn btn-link">手工创建</button>
		<button id="editBtn" type="button" class="btn btn-link">修改</button>
		<button id="removeBtn" type="button" class="btn btn-link">删除</button>
		<button id="nextBtn" type="button" class="btn btn-link">执行任务</button>
		<button id="completeBtn" type="button" class="btn btn-link">终止任务</button>
		<button id="transferBtn" type="button" class="btn btn-link">转交任务</button>
		<button id="releasePersonBtn" type="button" class="btn btn-link">人员释放</button>
		<button id="recoverBtn" type="button" class="btn btn-link">任务恢复</button>
		<button id="viewStateBtn" type="button" class="btn btn-link">任务状态</button>
	</div>
	<table class="layui-table flight-info" lay-skin="nob" style="margin: 0px;">
		<tbody>
			<tr>
				<td style="width:150px" align="right">进港航班号：</td>
				<td>${inFlight.fltNo}</td>
				<td align="right">起场：</td>
				<td>${inFlight.dAirport}</td>
				<td align="right">计落：</td>
				<td>${inFlight.sta}</td>
				<td align="right">预落：</td>
				<td>${inFlight.eta}</td>
				<td align="right">实落：</td>
				<td>${inFlight.ata}</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td align="right">机号：</td>
				<td>${inFlight.actNo}</td>
				<td align="right">机型：</td>
				<td>${inFlight.actType}</td>
				<td align="right">机位：</td>
				<td>${inFlight.actStand}</td>
				<td align="right">到达口：</td>
				<td>${inFlight.gate}</td>
			</tr>
			<tr>
				<td style="width:150px" align="right">出港航班号：</td>
				<td>${outFlight.fltNo}</td>
				<td align="right">落场：</td>
				<td>${outFlight.aAirport}</td>
				<td align="right">计起：</td>
				<td>${outFlight.std}</td>
				<td align="right">预起：</td>
				<td>${outFlight.etd}</td>
				<td align="right">实起：</td>
				<td>${outFlight.atd}</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td align="right">机号：</td>
				<td>${outFlight.actNo}</td>
				<td align="right">机型：</td>
				<td>${outFlight.actType}</td>
				<td align="right">机位：</td>
				<td>${outFlight.actStand}</td>
				<td align="right">登机口：</td>
				<td>${outFlight.gate}</td>
			</tr>
		</tbody>
	</table>
	<table class="layui-table" id="taskListGrid">
		<thead>
			<tr>
				<th width="50">&nbsp;</th>
				<th width="80">序号</th>
				<th>任务名称</th>
				<th>作业类型</th>
				<th>作业人员</th>
				<th>航班号</th>
				<th>进出港</th>
				<th>保障状态</th>
				<th>节点状态</th>
				<c:forEach items="${plusColumns}" var="column">
					<th>${column.disName}</th>
				</c:forEach>
				<c:if test="${hisFlag=='his'}">
				<th>操作</th>
				</c:if>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${jobs}" var="job" varStatus="status">
				<tr style="cursor: pointer;">
					<td><input type="radio" name="taskRadio" 
						data-starttm="${job.startTime}" 
						data-endtm="${job.endTime}" 
						data-jobtype="${job.jobTypeId}" 
						data-fltid="${job.fltid}" 
						data-orderid="${job.orderId}" 
						data-procid="${job.processId}" 
						data-flowtaskid="${job.flowTaskId}" 
						data-state="${job.state}" 
						data-personid="${job.personId}" 
						value="${job.id}"></td>
					<td>${status.index+1}</td>
					<td>${job.name}</td>
					<td>${job.jobType}</td>
					<td>${job.person}</td>
					<td>${job.fltNo}</td>
					<td>${fns:startsWith(job.inOutFlag,"A")?'进港':fns:startsWith(job.inOutFlag,"D")?'出港':job.inOutFlag}</td>
					<td>${job.state==0?'未分配':job.state==1?'已分配':job.state==2?'执行中':job.state==3?'完成':job.state==4?'待排':job.state==5?'终止':job.state==6?'暂停':job.state}</td>
					<td>${job.currTask}</td>
					<c:forEach items="${plusColumns}" var="column">
						<td>${job.plusData[column.attrCode]}</td>
					</c:forEach>
					<c:if test="${hisFlag=='his'}">
					<td align="center"><button class="layui-btn layui-btn-mini layui-btn-normal" onClick="getNodeView(${job.id})">节点时间</button></td>
					</c:if>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<script type="text/javascript"
		src="${ctxStatic}/prss/scheduling/jobManage.js"></script>
</body>
</html>