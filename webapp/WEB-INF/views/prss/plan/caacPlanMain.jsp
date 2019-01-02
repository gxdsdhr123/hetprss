<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>局方长期计划</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/prss/plan/css/planMain.css" rel="stylesheet" />
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
var data = ${data};
var type = "${type}";
</script>
</head>
<body>
	<div id="tool-box">
<%-- 		<shiro:hasPermission name="caac:plan:import"> --%>
			<button id="import" type="button" class="btn btn-link">
				<!-- <i class="fa fa-cloud-upload">&nbsp;</i> -->
				计划导入
			</button>
<%-- 		</shiro:hasPermission> --%>
<%-- 		<shiro:hasPermission name="caac:plan:create"> --%>
			<button id="create" type="button" class="btn btn-link">
				<!-- <i class="fa fa-plus">&nbsp;</i> -->
				新增
			</button>
<%-- 		</shiro:hasPermission> --%>
<%-- 		<shiro:hasPermission name="caac:plan:remove"> --%>
			<button id="remove" type="button" class="btn btn-link">
				<!-- <i class="fa fa-remove">&nbsp;</i> -->
				删除
			</button>
<%-- 		</shiro:hasPermission> --%>
<%-- 		<shiro:hasPermission name="caac:plan:print"> --%>
			<button id="print" type="button" class="btn btn-link">
				<!-- <i class="fa fa-print">&nbsp;</i> -->
				打印
			</button>
<%-- 		</shiro:hasPermission> --%>
	</div>
	<div id="baseTables">
		<table id="baseTable"></table>
	</div>
	<form id="printForm" action="${ctx}/caac/plan/print" hidden="">
	   <textarea id="printTitle" name="printTitle"></textarea>
	</form>
	<form id="flightWeek" class='layui-form' action=''
		style="display: none">
		<div class="layui-form-item" pane="">
			<input lay-skin="primary" title="星期一" type="checkbox"> <input
				lay-skin="primary" title="星期二" type="checkbox"> <input
				lay-skin="primary" title="星期三" type="checkbox">
		</div>
		<div class="layui-form-item" pane="">
			<input lay-skin="primary" title="星期四" type="checkbox"> <input
				lay-skin="primary" title="星期五" type="checkbox"> <input
				lay-skin="primary" title="星期六" type="checkbox">
		</div>
		<div class="layui-form-item" pane="">
			<input lay-skin="primary" title="星期日" type="checkbox">
		</div>
	</form>

	<form id="fileList" action="${ctx}/caac/plan/importPlan" method="post" style="display: none;" enctype="multipart/form-data">
        <input id="fileInput" name="file" type="file" multiple="multiple" style="display: none;" onchange="fileOnChange();">
        <span id = "fileName"></span>
    </form>
    
    <script type="text/javascript" src="${ctxStatic}/prss/plan/caacPlanCommon.js"></script>
    <script type="text/javascript" src="${ctxStatic}/prss/plan/caacPlanMain.js"></script>
</body>
</html>