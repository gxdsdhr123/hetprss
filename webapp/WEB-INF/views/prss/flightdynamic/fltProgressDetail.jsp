<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>作业详情</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<script type="text/javascript" src="${ctxStatic}/prss/message/talk.js"></script>
<script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/fltProgressDetail.js"></script>
<body>
	<table class="layui-table" lay-skin="nob" style="margin: 0px;">
		<tbody>
			<tr>
				<td style="width:120px">进港航班号：</td>
				<td>${inFlight.fltNo}</td>
				<td>起场：</td>
				<td>${inFlight.dAirport}</td>
				<td>计落：</td>
				<td>${inFlight.sta}</td>
				<td>预落：</td>
				<td>${inFlight.eta}</td>
				<td>实落：</td>
				<td>${inFlight.ata}</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>机号：</td>
				<td>${inFlight.actNo}</td>
				<td>机型：</td>
				<td>${inFlight.actType}</td>
				<td>机位：</td>
				<td>${inFlight.actStand}</td>
				<td>到达口：</td>
				<td>${inFlight.gate}</td>
			</tr>
			<tr>
				<td style="width:120px">出港航班号：</td>
				<td>${outFlight.fltNo}</td>
				<td>落场：</td>
				<td>${outFlight.aAirport}</td>
				<td>计起：</td>
				<td>${outFlight.std}</td>
				<td>预起：</td>
				<td>${outFlight.etd}</td>
				<td>实起：</td>
				<td>${outFlight.atd}</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>机号：</td>
				<td>${outFlight.actNo}</td>
				<td>机型：</td>
				<td>${outFlight.actType}</td>
				<td>机位：</td>
				<td>${outFlight.actStand}</td>
				<td>登机口：</td>
				<td>${outFlight.gate}</td>
			</tr>
		</tbody>
	</table>
	<table class="layui-table" id="taskListGrid">
		<thead>
			<tr>
				<th width="80">序号</th>
				<th>任务名称</th>
				<th>作业类型</th>
				<th>作业人员</th>
				<th>航班号</th>
				<th>节点状态</th>
				<th>人员晚到</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${jobs}" var="job" varStatus="status">
				<tr>
					<td class="text-center">${status.index+1}</td>
					<td>${job.name}</td>
					<td>${job.jobType}</td>
					<td>${job.person}</td>
					<td class="text-center">${job.fltNo}</td>
					<td>${job.currTask}</td>
					<td>${job.lateState}</td>
					<td class="text-center">
						<c:if test="${not empty job.processId and  not empty job.orderId}">
							<button class="layui-btn layui-btn-small layui-btn-primary" type="button" onclick="viewState('${job.processId}','${job.orderId}')"><i class="fa fa-eye">&nbsp;</i>查看</button>
						</c:if>
						<c:if test="${not empty job.personId}">
							<button class="layui-btn layui-btn-small layui-btn-primary" type="button" onclick="talk('${job.personId}',0)"><i class="fa fa-phone">&nbsp;</i>通话</button>
						</c:if>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<script type="text/javascript" src="${ctxStatic}/prss/scheduling/jobManage.js"></script>
</body>
</html>