<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>领导排班管理</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<style type="text/css">
#cellDetailInfo {
    width: 23%;
    position: absolute;
    z-index: 9;
    background: rgba(0, 0, 0, 0.35);
    margin-left: 77%;
}
.mark_c {
    position: absolute;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.01);
    z-index: 999999999999999999;
}
</style>
<body>
	<div class="mark_c" style="display: block;"></div>

	<div id="cellDetailInfo" >
	</div>

	<div id="tool-box" style="padding-top: 5px;">
		<div style="float: left;padding-left:15px" id="toolbar">
			<button id="prevWeekBtn" type="button" class="btn btn-link">上一周</button>
			<button id="nextWeekBtn" type="button" class="btn btn-link">下一周</button>
		</div>
		<div style="float: left;padding-left:10px">
			  <input id="searchTime" class="layui-input" onfocus="WdatePicker({dateFmt:'yyyyMMdd',readOnly:true})" value="${searchTime }" />
		</div>
		<div style="float: left;padding-left:15px" id="toolbar">
			<button id="searchBtn" type="button" class="btn btn-link">查询</button>
			<button id="leadDutyBtn" type="button" class="btn btn-link">领导值班计划</button>
			<button id="delPlanBtn" type="button" class="btn btn-link">删除计划</button>
			<button id="printBtn" type="button" class="btn btn-link">打印</button>
		</div>
		<div style="float: left;padding-left:15px" id="toolbar">
			<button id="addDepartmentBtn" type="button" class="btn btn-link">增加</button>
			<button id="modifyDepartmentBtn" type="button" class="btn btn-link">修改</button>
			<button id="delDepartmentBtn" type="button" class="btn btn-link">删除</button>
		</div>
	</div>
	<div id="createDetailTableDiv" style="width:100%;position: relative;" onselectstart="return false" style="-moz-user-select:none;">
		<table id="createDetailTable" style="table-layout: fixed;"></table>
	</div>
	
	<form id="printForm" action="${ctx}/leaderPlan/leader/print" method="post" style="display: none">
		<textarea id="printTitle" name="title"></textarea>
		<textarea id="printData" name="data"></textarea>
		<input type="hidden" name="searchTime"  value="${searchTime }"/>
	</form>
	
	

	
	
<script type="text/javascript" src="${ctxStatic}/prss/leadershipScheduling/leadershipScheduling.js"></script>
</body>
</html>