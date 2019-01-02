<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>任务指派单打印</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript" src="${ctxStatic}/prss/scheduling/schedulingMissionBill.js"></script>
</head>
<body>
	<div id="tool-box">
		<table id="fltInfoTable" class="layui-table" style="width:500px;text-align:left;margin:10px auto;">
			<tbody>
				<tr>
					<td>进港航班号：${fltInfo.inFltNum}</td>
					<td>出港航班号：${fltInfo.outFltNum}</td>
					<td>机号：${fltInfo.aircraftNum}</td>
					<td>机型：${fltInfo.acttypeCode}</td>
				</tr>
				<tr>
					<td>进港预落：${fltInfo.inEta}</td>
					<td>出港预落：${fltInfo.outEta}</td>
					<td>机位：${fltInfo.actstandCode}</td>
					<td>航线：${fltInfo.route3code}</td>
				</tr>
			</tbody>
		</table>	
	</div>
	<div id="baseTables">
		<table id="baseTable"></table>
	</div>
	<!-- 导出excel -->
	<form id="exportForm" method="post">
		<!-- 进港航班号，解析对比保存导出用 -->
		<input type="hidden" id="inFltId" name="inFltId" value="${fltInfo.inFltId}"/>
		<input type="hidden" id="outFltId" name="outFltId" value="${fltInfo.outFltId}"/>
		<input type="hidden" id="inFltNum" name="inFltNum" value="${fltInfo.inFltNum}"/>
		<input type="hidden" id="inFltNum2" name="inFltNum2" value="${fltInfo.inFltNum2}"/>
		<input type="hidden" id="outFltNum" name="outFltNum" value="${fltInfo.outFltNum}"/>
		<input type="hidden" id="outFltNum2" name="outFltNum2" value="${fltInfo.outFltNum2}"/>
		<input type="hidden" id="inFlightDate" name="inFlightDate" value="${fltInfo.inFlightDate}"/>
		<input type="hidden" id="outFlightDate" name="outFlightDate" value="${fltInfo.outFlightDate}"/>
		<input type="hidden" id="supervisionUser"  name="supervisionUser"value="${fltInfo.supervisionUser}"/>
		<input type="hidden" id="inAircraftNum" name="inAircraftNum" value="${fltInfo.inAircraftNum}"/>
		<input type="hidden" id="inActtypeCode" name="inActtypeCode" value="${fltInfo.inActtypeCode}"/>
		<input type="hidden" id="outAircraftNum" name="outAircraftNum" value="${fltInfo.outAircraftNum}"/>
		<input type="hidden" id="outActtypeCode" name="outActtypeCode" value="${fltInfo.outActtypeCode}"/>
		<input type="hidden" id="inEta" name="inEta" value="${fltInfo.inEta}"/>
		<input type="hidden" id="inEtd" name="inEtd" value="${fltInfo.inEtd}"/>
		<input type="hidden" id="outEta" name="outEta" value="${fltInfo.outEta}"/>
		<input type="hidden" id="outEtd" name="outEtd" value="${fltInfo.outEtd}"/>
		<input type="hidden" id="inActstandCode" name="inActstandCode" value="${fltInfo.inActstandCode}"/>
		<input type="hidden" id="inRoute3code" name="inRoute3code" value="${fltInfo.inRoute3code}"/>
		<input type="hidden" id="outActstandCode" name="outActstandCode" value="${fltInfo.outActstandCode}"/>
		<input type="hidden" id="outRoute3code" name="outRoute3code" value="${fltInfo.outRoute3code}"/>
	</form>
</body>
</html>