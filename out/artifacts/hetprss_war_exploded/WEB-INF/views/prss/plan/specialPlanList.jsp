<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>公务/通航计划</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript" src="${ctxStatic}/bootstrap/plugins/bootstrap-table/extensions/bootstrap-table-contextmenu.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/prss/plan/specialPlanList.js"></script>
</head>
<body>
	<div id="tool-box">
		<button id="refreshBtn" type="button" class="btn btn-link">刷新</button>
		<button id="addBtn" type="button" class="btn btn-link">新增</button>
		<button id="delBtn" type="button" class="btn btn-link">删除</button>
		<button id="recycleBtn" type="button" class="btn layui-btn-normal">回收站</button>
		<select id="planStatusSel" name="planStatusSel" style="width: 100px;margin-left:20px;display:inline-block" onchange="changeSel()">
			<option value="0">未执行</option>
			<option value="0,1">全部</option>
			<option value="1">历史</option>
		</select>
		<!-- TODO 临时隐藏，确定需求后实现 -->
		<button class="btn btn-link" type="button" onclick="addForecastPage('forecast')" style="display: none">
			预测
		</button>
	</div>
	<div id="baseTables">
		<table id="baseTable"></table>
	</div>
	<ul id="context-menu" class="dropdown-menu">
		<li data-item="msgView">
			<a>查看报文</a>
		</li>
	</ul>
	<!-- 下载文件表单 -->
	<form id="downFileForm" action="${ctx}/plan/specialPlan/downloadAttachment" style="display: none" method="post">
		<input type="hidden" id="downFileInfo" name="downFileInfo">
	</form>
</body>
</html>