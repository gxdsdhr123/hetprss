<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>进港卸机记录详情页面</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript"
	src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<style type="text/css">
td {
	text-align: center;
	color: white;
}
</style>
</head>
<body>
	<div id="container">
			<form id="tableForm" action="" method="post">
				<table id="baseTable" border="0" style="width: 900px; text-align: left; margin: 20px auto;">
					<tbody>
					 <c:forEach items="${detailMap}" var="detailMap" varStatus="status">
						<tr>
							<td style="width: 210px">进港航班号：${detailMap.IN_FLIGHT_NUMBER}</td>
							<td style="width: 210px">出港航班号：${detailMap.OUT_FLIGHT_NUMBER}</td>
							<td style="width: 210px">机号：${detailMap.AIRCRAFT_NUMBER}</td>
							<td style="width: 210px">机型：${detailMap.ACTTYPE_CODE}</td>
							<td style="width: 210px">机位：${detailMap.ACTSTAND_CODE}</td>
						</tr>
						<tr>
							<td style="width: 210px">进港预落：${detailMap.ETA}</td>
							<td style="width: 210px">出港预起：${detailMap.ETD}</td>
							<td colspan="3">航线：${detailMap.ROUTE}</td>
						</tr>
						<tr>									
					</tr>
					</c:forEach>
					</tbody>
				</table>
				<hr>
				<table class="layui-table" align="center">
					<thead>
						<tr>
							<td colspan=4>行李</td>
						</tr>
						<tr>
							<td width=100>序号</td>
							<td width=150>拖斗号</td>
							<td width=150>备注</td>
							<td width=150>件数</td>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${detailXL}" var="detailXL" varStatus="status">
							<tr>
								<td>${status.index + 1}</td>
								<td>${detailXL.xlbucketNumber}</td>
								<td>${detailXL.xlremark}</td>
								<td>${detailXL.xlpackageNumber}</td>
							</tr>
						</c:forEach>
					</tbody>
					</table>
					<table class="layui-table"  align="center">
					<thead>
					<tr>
						<td colspan=4>货运</td>
					</tr>
					<tr>
						<td width=100>序号</td>
						<td width=150>拖斗号</td>
						<td width=150>备注</td>
						<td width=150>件数</td>
					</tr>
					</thead>
					<tbody>
						<c:forEach items="${detailHY}" var="detailHY" varStatus="status">
					<tr>
						<td>${status.index + 1}</td>
						<td>${detailHY.hybucketNumber}</td>
						<td>${detailHY.hyremark}</td>
						<td>${detailHY.hypackageNumber}</td>
					</tr>
					</c:forEach>
					</tbody>
				</table>
			</form>
		</div>
</body>
</html>