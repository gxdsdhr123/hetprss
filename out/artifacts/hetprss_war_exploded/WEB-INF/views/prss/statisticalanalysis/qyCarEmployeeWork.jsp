<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
	<head>
		<title>牵引车员工工作量统计-按数量统计</title>
		<meta name="decorator" content="default" />
		<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
		<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
		<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script> 
		<script type="text/javascript" src="${ctxStatic}/prss/statisticalanalysis/qyCarEmployeeWork.js"></script>  
		<link href="${ctxStatic}/prss/statisticalanalysis/css/qyCarEmployeeWork.css" rel="stylesheet" />
		<script>
			var PATH = '${ctx}';
		</script>
	</head>
	<body>
		<div id="tool-box">
			<button id="searchBut" type="button" class="btn btn-link">筛选</button>
			<button id="printBut" type="button" class="btn btn-link">打印</button>
			<form id="printForm" style="display:none" action="${ctx}/qyCarEmployeeWork/print" method="post">
				<textarea id="beginTimeDisplay" name="beginTimeDisplay"></textarea>
				<textarea id="endTimeDisplay" name="endTimeDisplay"></textarea>
				<textarea id="banZuDisplay" name="banZuDisplay"></textarea>
				<textarea id="zuYuanDisplay" name="zuYuanDisplay"></textarea>
			</form>
		</div>
		<table id="baseTable" class="layui-table">
		
		</table>
		<div id="container" style="display:none;">
			<div id="containerTopDiv">
				<div class="preDiv">
					班组
				</div>
				<div class="selectDiv">
					<select id="banZu"></select>
				</div>
				<div class="preDiv">
					组员
				</div>
				<div class="selectDiv">
					<select id="zuYuan"></select>
				</div>
			</div>
			<div id="bottomDiv">
				<div class="preDiv">
					查询时间
				</div>
				<div class="selectDiv">
					<input id="beginTime" class="layui-input" onfocus="WdatePicker({dateFmt:'yyyyMMdd',maxDate:'%y-%M-%d'})" readonly="readonly" value="${beginTime}"/>
				</div>
				<div class="preDiv">
					-----
				</div>
				<div class="selectDiv">
					<input id="endTime" class="layui-input" onfocus="WdatePicker({dateFmt:'yyyyMMdd',maxDate:'%y-%M-%d'})" readonly="readonly" value="${endTime}"/>
				</div>
			</div>
		</div>
	</body>
</html>