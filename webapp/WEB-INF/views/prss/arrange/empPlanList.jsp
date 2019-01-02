<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>员工排班计划</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<link href="${ctxStatic}/prss/arrange/css/empPlanList.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/fullcalendar/fullcalendar.min.css"/>
<script type="text/javascript" src="${ctxStatic}/fullcalendar/moment.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/fullcalendar/fullcalendar.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/fullcalendar/zh-cn.js"></script>
<script type="text/javascript">
var currDate = "${currDate}";
</script>
<body>
	<div id="toolbar" >
		<div class="btn-float" style="margin-left:5px;margin-right:5px">
			<input class='planDate layui-input' type='text' placeholder='选择月份' id="planDate" readonly
				onfocus="WdatePicker({onpicked:function(dp){doCheckMonth()},dateFmt:'yyyy-MM'})" />
		</div>
		<div class="btn-float">
			<button id="prevMonBtn" type="button" class="btn btn-link">上一月</button>
			<button id="nextMonBtn" type="button" class="btn btn-link">下一月</button>
		</div>
		<button id="planBtn" type="button" class="btn btn-link">人员计划</button>
		<shiro:hasPermission name="arrange:empPlanList:unfixedBtn">
			<button id="unfixedBtn" type="button" class="btn btn-link">生成非固定班制计划</button>
		</shiro:hasPermission>
		<button id="batchAddBtn" type="button" class="btn btn-link">批量生成</button>
		<button id="delBtn" type="button" class="btn btn-link">删除计划</button>
		<button id="batchDelBtn" type="button" class="btn btn-link">批量删除</button>
		<button id="printBtn" type="button" class="btn btn-link">打印</button>
		<button id="showLogBtn" type="button" class="btn btn-link">人员停用日志</button>
	</div>
	<div id='planCal'></div>
	<div id="planRangeDate" style="display:none">
		<input class='planRangeDate layui-input' type='text' placeholder='计划开始时间' id="planStartTime"
			onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'${currDate}',maxDate:'#F{$dp.$D(\'planEndTime\')}'})"><span class='dao'>&nbsp;到&nbsp;</span><input class='planRangeDate layui-input' type='text' 
			placeholder='计划结束时间' onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'planStartTime\')}'})" id="planEndTime">
	</div>
	<div id="contentDiv" style="display:none">
		<div style="float:left">
		<div class="layui-form-item" id="searchDiv">
			<div class="layui-form-item">
				<label class="layui-form-label" style="font-weight:600;">计划来源</label>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label unfixed-lable">日期段从</label>
				<div class="layui-input-inline" style="width:120px;">
					<div class="input-group">
						<input type='text' maxlength="20" id="timeStart"
							placeholder='请选择日期' class='layui-input' value=""
							onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'timeEnd\')}'});" />
					</div>
				</div>
			</div>
			<div class="layui-form-item" >
				<label class="layui-form-label unfixed-lable">到</label>
				<div class="layui-input-inline" style="width:120px;">
					<div class="input-group">
						<input type='text' maxlength="20" id="timeEnd"
							placeholder='请选择日期' class='layui-input' value=""
							onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'timeStart\')}'});" />
					</div>
				</div>
			</div>
		</div>
		</div>
		<div style="float:left">
		<div class="layui-form-item" id="searchDiv">
			<div class="layui-form-item">
				<label class="layui-form-label" style="font-weight:600;">生成计划</label>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label unfixed-lable">日期段从</label>
				<div class="layui-input-inline" style="width:120px;">
					<div class="input-group">
						<input type='text' maxlength="20" id="timeStart2"
							placeholder='请选择日期' class='layui-input' value=""
							onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'%y-%M-{%d}',maxDate:'#F{$dp.$D(\'timeEnd2\')}'});" />
					</div>
				</div>
			</div>
			<div class="layui-form-item" >
				<label class="layui-form-label unfixed-lable">到</label>
				<div class="layui-input-inline" style="width:120px;">
					<div class="input-group">
						<input type='text' maxlength="20" id="timeEnd2"
							placeholder='请选择日期' class='layui-input' value=""
							onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'timeStart2\' )}'});" />
					</div>
				</div>
			</div>
		</div>
		</div>
	</div>
	
<form id="printForm" action="${ctx}/arrange/empplan/exportPlan">
	<input type="hidden" name="exportMonth" id="exportMonth" value="${currDate}">
</form>
<iframe style="display: none;" id="hideFrame" name="hideFrame"></iframe>
<script type="text/javascript" src="${ctxStatic}/prss/arrange/empPlanList.js"></script>
</body>
</html>