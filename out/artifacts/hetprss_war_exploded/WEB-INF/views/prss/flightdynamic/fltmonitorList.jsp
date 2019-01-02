<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/prss/flightdynamic/css/fltmonitor.css"
	rel="stylesheet" />
<%-- <link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css"
	rel="stylesheet" />
<script type="text/javascript"
	src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="${ctxStatic}/modules/sys/maintain/maintainCommon.js"></script>
<script type="text/javascript"
	src="${ctxStatic}/jquery/plugins/SJgantt/SJgantt-fd.js"></script> --%>
</head>
<body>

	<div class="box-header with-border">
		<div class="col-md-12">
			<input id="inFltid" type="hidden" value="${fltInfo.inFltid}">
			<input id="outFltid" type="hidden" value="${fltInfo.outFltid}">
			<input id="isY" type="hidden" value="${fltInfo.actstandKind}">
			<input id="hisFlag" type="hidden" value="${hisFlag}">
			<table class="header_table">
				<c:if test="${fltInfo.inFltid != null and fltInfo.inFltid != ''}">
					<tr>
						<td>进港航班：<span>${fltInfo.inFlightNumber}</span></td>
						<td>起场：<span>${fltInfo.departApt4code}</span></td>
						<td>计落：<span>${fltInfo.sta}</span></td>
						<td>预落：<span>${fltInfo.eta}</span></td>
						<td>实落：<span>${fltInfo.ata}</span></td>
						<td>机号：<span>${fltInfo.aircraftNumber}</span></td>
						<td>机型：<span>${fltInfo.acttypeCode}</span></td>
					</tr>
				</c:if>
				<c:if test="${fltInfo.outFltid != null and fltInfo.outFltid != ''}">
					<tr>
						<td>出港航班：<span>${fltInfo.outFlightNumber}</span></td>
						<td>落场：<span>${fltInfo.arrivalApt4code}</span></td>
						<td>计起：<span>${fltInfo.std}</span></td>
						<td>预起：<span>${fltInfo.etd}</span></td>
						<td>实起：<span>${fltInfo.atd}</span></td>
						<td>机位：<span>${fltInfo.actstandCode}</span></td>
						<td>登机口：<span>${fltInfo.gate}</span></td>
					</tr>
				</c:if>
			</table>
			<!-- 
				<div class="row">
					<div class="col-md-9">
						<div class="clearfix">
							<span class="pull-left">进度</span> <small class="pull-right">90%</small>
						</div>
						<div class="progress xs">
							<div class="progress-bar progress-bar-blue" style="width: 90%;"></div>
						</div>
					</div>
					<div class="col-md-2 col-md-offset-1">
						<i class="layui-icon" style="font-size: 30px; color: #1E9FFF;">&#xe6ed;</i>
					</div>
				</div>
			</div> -->
		</div>
	</div>
	<div class="fltmonitor" id="fltmonitor" style="width:100%;position: relative;overflow: auto;">
		<div id="monitor" class="box-body" style="width:100%;">
		图形加载中……
		</div>
	</div>
	<div id="layer_table" style="margin:10px;display:none;overflow:auto;position: relative;"></div>
	<div id="taskInfo" style="margin:10px;display:none;overflow:auto;position: relative; width:300px; height:400px;"></div>
	<script type="text/javascript" src="${ctxStatic}/jquery/plugins/progressChart/progress-chart.js"></script>
	<%-- <script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/fltmonitor/a0n.js"></script>
	<script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/fltmonitor/a0y.js"></script>
	<script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/fltmonitor/a1n.js"></script>
	<script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/fltmonitor/d0n.js"></script>
	<script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/fltmonitor/d0y.js"></script>
	<script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/fltmonitor/a1y.js"></script> --%>
	<script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/fltmonitorList.js"></script>

</body>
</html>