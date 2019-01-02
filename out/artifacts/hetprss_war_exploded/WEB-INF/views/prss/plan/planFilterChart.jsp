<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
	<head>
		<title>长期计划分析图</title>
		<meta name="decorator" content="default" />
		<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
		<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
		<script src="${ctxStatic}/echarts/echarts.min.js"></script>
		<script src="${ctxStatic}/echarts/theme/dark-prss.js"></script>
		<script src="${ctxStatic}/prss/imax/common/echarts.common.js" type="text/javascript"></script>
		<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script> 
		<script type="text/javascript" src="${ctxStatic}/prss/plan/planFilterChart.js"></script>
		<link href="${ctxStatic}/prss/statisticalanalysis/css/flightGuaranteeRecord.css" rel="stylesheet" />
		<script>
			var PATH = '${ctx}';
		</script>
	</head>
	<body>
		
		<!-- 长期计划分析统计 -->
		<div class="box run_container">
			<div class="box-body row">
				<div class="col-sm-12">
					<div id="planChart" class="echarts-container"></div>
				</div>
			</div>
			
		</div>
	</body>
</html>