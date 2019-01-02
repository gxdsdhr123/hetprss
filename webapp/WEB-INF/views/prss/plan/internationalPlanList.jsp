<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>国际航班计划</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript"
		src="${ctxStatic}/bootstrap/plugins/bootstrap-table/extensions/bootstrap-table-contextmenu.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/prss/plan/internationalPlanList.js"></script>

</head>
<body>
	<div id="tool-box">
		<button id="refresh" type="button" class="btn btn-link">刷新</button>
		<button id="addBtn" type="button" class="btn btn-link">新增</button>
		<button id="copyBtn" type="button" class="btn btn-link">复制</button>
		<button id="delBtn" type="button" class="btn btn-link">删除</button>
		<!-- <button id="printBtn" type="button" class="btn btn-link">打印</button> -->
	</div>
	<div id="baseTables">
		<table id="planGrid"></table>
	</div>	
	<ul id="context-menu" class="dropdown-menu">
		<li data-item="msgView">
			<a>查看报文</a>
		</li>
	</ul>
	<div id="calendar"></div>
		<form id="printForm" action="${ctx}/plan/interPlan/print" hidden="">
	   <textarea id="printTitle" name="printTitle"></textarea>
		</form>
</body>
</html>