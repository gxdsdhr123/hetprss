<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>航班动态回收站</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<%@include file="/WEB-INF/views/prss/flightdynamic/flightDynFilter.jsp"%>
<link href="${ctxStatic}/prss/flightdynamic/css/flightDynList.css" rel="stylesheet" />
<body>
	<div id="tool-box">
		<button id="refresh" type="button" class="btn btn-link">刷新</button>
		<button id="show" type="button" class="btn btn-link">查看</button>
		<shiro:hasPermission name="fd:recycle:selectAll">
			<button id="selectAll" type="button" class="btn btn-link">全选</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:recycle:remove">
			<button id="remove" type="button" class="btn btn-link">删除</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:recycle:recovery">
			<button id="recovery" type="button" class="btn btn-link">还原</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:recycle:clear">
			<button id="clear" type="button" class="btn btn-link">清空回收站</button>
		</shiro:hasPermission>
	</div>
	<div id="baseTables">
		<table id="baseTable"></table>
	</div>
	<div id="footButtons">
		<div id="rowInfo">
			共<span id="totalRow"></span>条记录，当前加载<span id="currentRow"></span>条记录
		</div>
		<div id="rowTool">
			继续加载<input type="text" id="numOfRow" class="layui-input">条记录
			<button id="keepLoad" type="button" class="btn btn-xs btn-primary">加载</button>
			<button id="loadAll" type="button" class="btn btn-xs btn-primary">加载全部</button>
		</div>
		
	</div>
	<iframe style="display: none;" id="hideFrame" name="hideFrame"></iframe>
	<script type="text/javascript" src="${ctxStatic}/bootstrap/plugins/bootstrap-table/extensions/bootstrap-table-contextmenu.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/fdRecycleList.js"></script>
</body>
</html>