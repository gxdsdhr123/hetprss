<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>局方长期计划新增</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/prss/plan/css/planMain.css" rel="stylesheet" />
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
var data = ${data};
var type ="${type}";
</script>
</head>
<body>
	<div id="baseTables">
		<table id="baseTable"></table>
	</div>
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
    <script type="text/javascript" src="${ctxStatic}/prss/plan/caacPlanCommon.js"></script>
    <script type="text/javascript" src="${ctxStatic}/prss/plan/caacPlanAdd.js"></script>
</body>
</html>