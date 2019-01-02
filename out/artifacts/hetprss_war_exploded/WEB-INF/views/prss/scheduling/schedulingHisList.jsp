<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>指挥调度（列表）</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<link href="${ctxStatic}/prss/flightdynamic/css/flightDynList.css" rel="stylesheet" />
<style type="text/css">
#tool-box label .switch{
	width: 60px;
	display: inline-block;
	line-height: 0px;
	padding: 0px !important;
}
#tool-box .checkBox{
	
}
.layui-form-checkbox.layui-form-checked span{
	background-color:unset;
	color: #ccc;
}
.layui-form-checkbox[lay-skin="primary"] i{
	border: 1px solid #05163B;
	background-color: #05163B;
	color: #05163B;
	width: 13px;
	line-height: 13px;
	font-size: 11px;
}
.layui-form-checkbox[lay-skin="primary"] span{
	font-size:12px;
	color: #ccc;
}
.checkBoxes {
	padding: 0px !important;
	line-height: 20px;
}
.layui-form-checkbox[lay-skin="primary"]:hover i{
	border-color: #006DC0;
	color: #05163B;
}
.layui-form-checkbox:hover span{
	color: #ccc
}
</style>
</head>
<body>
	<input id="loginName" type="hidden" value="${fns:getUser().loginName}">
	<input type="hidden" id="schemaId" value="${schemaId}">
	<div id="tool-box">
		<button id="refresh" type="button" class="btn btn-link">刷新</button>
		<button id="filter" type="button" class="btn btn-link">筛选</button>
		<shiro:hasPermission name="sc:hist:print">
			<button id="print" type="button" class="btn btn-link">打印</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="sc:hist:message">
			<button id="message" type="button" class="btn btn-link">报文</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="sc:hist:order">
			<button id="order" type="button" class="btn btn-link">指令</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="sc:hist:ensure">
			<button id="ensure" type="button" class="btn btn-link">保障</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="sc:jobManageBtn">
			<button id="jobManageBtn" type="button" class="btn btn-link">作业管理</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="sc:error">
			<button id="error" type="button" class="btn btn-link">异常</button>
		</shiro:hasPermission>
		<div class="layui-inline">
			<label class="layui-form-label" style="padding: 10px 10px !important;margin-bottom: 0px;">数据日期</label>
			<div class="layui-input-inline">
				<input id="hisdate" name="hisdate" onClick="dateFilter();" class="layui-input" readonly="readonly"
					value="" type="text">
			</div>
		</div>
		<br>
		<form class="layui-form checkBoxes" action="">
			<shiro:hasPermission name="sc:bdCar">
				<input type="checkbox" class="checkBox" name="bdCar" title="远机位/205-218国内" lay-skin="primary" checked lay-filter="checkBoxes">
			</shiro:hasPermission>
			<shiro:hasPermission name="sc:farActstand">
				<input type="checkbox" class="checkBox" name="ktFar" title="远机位" lay-skin="primary" checked lay-filter="checkBoxes">
			</shiro:hasPermission>
			<shiro:hasPermission name="sc:narrow">
				<input type="checkbox" class="zt checkBox" name="narrow_narrow" title="窄体" lay-skin="primary" lay-filter="checkBoxes">
			</shiro:hasPermission>
			<shiro:hasPermission name="sc:airline:MU">
				<input type="checkbox" class="zt checkBox" name="narrow_MU" title="东航" lay-skin="primary" lay-filter="checkBoxes">
			</shiro:hasPermission>
			<shiro:hasPermission name="sc:airline:CZ">
				<input type="checkbox" class="zt checkBox" name="narrow_CZ" title="南航" lay-skin="primary" lay-filter="checkBoxes">
			</shiro:hasPermission>
			<shiro:hasPermission name="sc:airline:HU">
				<input type="checkbox" class="zt checkBox" name="narrow_HU" title="海航" lay-skin="primary" lay-filter="checkBoxes">
			</shiro:hasPermission>
			<shiro:hasPermission name="sc:wide">
				<input type="checkbox" class="checkBox" name="wide_wide" title="宽体" lay-skin="primary" checked lay-filter="checkBoxes">
			</shiro:hasPermission>
			<shiro:hasPermission name="sc:T1">
				<input type="checkbox" class="checkBox" name="T1" title="T1" lay-skin="primary" checked lay-filter="checkBoxes">
			</shiro:hasPermission>
			<shiro:hasPermission name="sc:T2I">
				<input type="checkbox" class="checkBox" name="T2I" title="T2国际" lay-skin="primary" checked lay-filter="checkBoxes">
			</shiro:hasPermission>
			<shiro:hasPermission name="sc:T2D">
				<input type="checkbox" class="checkBox" name="T2D" title="T2国内" lay-skin="primary" checked lay-filter="checkBoxes">
			</shiro:hasPermission>
			<shiro:hasPermission name="sc:T3I">
				<input type="checkbox" class="checkBox" name="T3I" title="T3外航" lay-skin="primary" checked lay-filter="checkBoxes">
			</shiro:hasPermission>
		</form>
	</div>
	<div id="ssConf" class="input-group-btn btn-group" style="display: none;">
		<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" name="searchOptBtn"
			aria-expanded="false">
			不限
			<span class="fa fa-caret-down"></span>
		</button>
		<ul class="dropdown-menu">
			<li>
				<a href="#" onclick="searchOpt('FLTNO',this)">航班号</a>
			</li>
			<li>
				<a href="#" onclick="searchOpt('ACTTYPE_CODE',this)">机型</a>
			</li>
			<li>
				<a href="#" onclick="searchOpt('AIRCRAFT_NUMBER',this)">机号</a>
			</li>
			<li>
				<a href="#" onclick="searchOpt('ACTSTAND_CODE',this)">机位</a>
			</li>
			<li>
				<a href="#" onclick="searchOpt('',this)">不限</a>
			</li>
		</ul>
	</div>

	<div id="baseTables" style="height:100%">
		<table id="baseTable"></table>
	</div>
	<ul id="context-menu" class="dropdown-menu">
		<li data-item="instructionView"><a>指令查看</a></li>
		<li class="dropdown" data-item="fltDetail"><a href="#"
			class="dropdownMenu">航班详细信息<b class="fa fa-chevron-right"></b></a>
			<ul class="dropdown-menu">
				<li data-item="passenger"><a>旅客信息</a></li>
				<li data-item="baggage"><a>货邮行信息</a></li>
				<li data-item="timeDynamic"><a>时间动态</a></li>
				<!-- <li data-item="guaranteeData"><a>保障资料</a></li> -->
				<li data-item="resourseState"><a>资源状态</a></li>
				<li data-item="massageChange"><a>消息变更轨迹</a></li>
			</ul></li>
		<li data-item="vipInfo"><a>要客详情</a></li>
		<shiro:hasPermission name="sc:error">
			<li data-item="exception"><a>异常查看</a></li>
		</shiro:hasPermission>
		<li role="separator" class="divider" style="visibility: hidden;"></li>
	</ul>

	<form id="printForm" action="${ctx}/scheduling/list/print" method="post"
		style="display: none">
		<textarea id="printTitle" name="title"></textarea>
		<textarea id="printData" name="data"></textarea>
	</form>
	<div id="msgDiv" style="display: none">
		<table id="msgTable"></table>
	</div>
	<%@include file="/WEB-INF/views/prss/flightdynamic/flightDynFilter.jsp"%>
	<script>
		$("#graph").click(function() {
			var schemaId = $('#schemaId').val();
			var form = $("<form id='graphForm' style='display:none' action='"+ctx + "/scheduling/gantt/listGantt'></form>");
			form.append($("<input type='text' name='schemaId' value='"+schemaId+"'>"));
			$("body").append(form);
			$("#graphForm").submit();
		});
	</script>
	<script type="text/javascript"
		src="${ctxStatic}/bootstrap/plugins/bootstrap-table/extensions/bootstrap-table-contextmenu.min.js"></script>
	<script type="text/javascript"
		src="${ctxStatic}/prss/scheduling/schedulingHisList.js?timestemp=${fns:getTimestamp()}"></script>
</body>
</html>