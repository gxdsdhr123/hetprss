<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<script src="${ctxStatic}/echarts/echarts.min.js"></script>
<script src="${ctxStatic}/echarts/theme/dark-prss.js"></script>
<script src="${ctxStatic}/prss/imax/common/echarts.common.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/prss/plan/tomorrowPlanForecast.js"></script>
<script type="text/javascript">
	var confData = ${confData};
</script>
</head>
<body>
	<div id="conf" style="display: none">
		<div id="tool-box">
			<button class="btn btn-link" type="button" onclick="saveFun()">保存配置</button>
		</div>
		<table id="dataGrid"></table>
	</div>
	<br><br><br>
	<table>
		<tr>
			<td colspan="2" align="center"><h3 id="showMsg"></h3><br></td>
		</tr>
		<tr>
			<td align="center">
				<!-- 出港航班架次时刻预测图 -->
				<div id="chartDepart" class="echarts-container"></div>
			</td>
			<td align="center">
				<!-- 机场全天各时段旅客人数 -->
				<div id="chartPassenger" class="echarts-container"></div>
			</td>
		</tr>
		<tr>
			<td align="center">
				<!-- 值机柜台建议开放个数时段分布 -->
				<div id="chartChekcIn" class="echarts-container"></div>
			</td>
			<td align="center">
				<!-- 安检通道建议开放个数时段分布 -->
				<div id="chartSecurity" class="echarts-container"></div>
			</td>
		</tr>
	</table>
</body>
</html>