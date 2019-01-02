<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>公务/通航计划</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript" src="${ctxStatic}/bootstrap/plugins/bootstrap-table/extensions/bootstrap-table-contextmenu.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/prss/plan/specialPlanRecycle.js"></script>
</head>
<body>
	<div id="tool-box">
		<button id="refreshBtn" type="button" class="btn btn-link">刷新</button>
		<button id="recoveryBtn" type="button" class="btn btn-link">恢复</button>
		<button id="removeBtn" type="button" class="btn btn-link">彻底删除</button>
	</div>
	<div id="baseTables">
		<table id="baseTable"></table>
	</div>
	<!-- 下载文件表单 -->
	<form id="downFileForm" action="${ctx}/plan/specialPlan/downloadAttachment" style="display: none" method="post">
		<input type="hidden" id="downFileInfo" name="downFileInfo">
	</form>
</body>
</html>