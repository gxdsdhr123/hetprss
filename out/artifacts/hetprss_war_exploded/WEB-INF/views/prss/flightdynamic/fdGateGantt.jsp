<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>航班动态（甘特图）</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<%@include file="/WEB-INF/views/prss/flightdynamic/flightDynFilter.jsp"%>
<link href="${ctxStatic}/jquery/plugins/perfectScroll/perfect-scrollbar.min.css" rel="stylesheet" />
<link href="${ctxStatic}/prss/flightdynamic/css/flightDynList.css" rel="stylesheet" />
<style type="text/css">
#loading {
	height: 65px;
	width: 300px;
	text-align: center;
	position: absolute;
	left: calc(50% - 150px);
	top: calc(50% - 32.5px);
	z-index: 999;
	background-color: #141B28;
	padding: 21px;
	border: 1px solid RGBA(95, 123, 180, 0.6);
	border-top-right-radius: 4px;
	border-bottom-left-radius: 4px;
	box-shadow: 0px 0px 4px -2px;
}
#loading::before{
	width: 30px;
	height: 30px;
	z-index: 1000;
	position: absolute;
	content: '';
	border-bottom: 4px solid #5F7BB4;
	display: block;
	border-left: 4px solid #5F7BB4;
	border-bottom-left-radius: 3px;
	bottom: 0px;
	left: 0px;
}
#loading::after{
	width: 30px;
	height: 30px;
	z-index: 1000;
	position: absolute;
	content: '';
	border-top: 4px solid #5F7BB4;
	display: block;
	border-right: 4px solid #5F7BB4;
	border-top-right-radius: 3px;
	top: 0px;
	right: 0px;
}
#startTime,#endTime{
	text-align: center;
}
#startTime *,#endTime * {
	margin:10px;
	display:inline-block;
}
</style>
<script type="text/javascript" src="${ctxStatic}/jquery/plugins/SJgantt/SJgantt-bdGate.js"></script>
<body>
	<div id="tool-box" style="padding:5px;">
		<div class="btn-group">
			<button id="refresh" type="button" class="btn btn-link">刷新</button>
			<button type="button" class="btn btn-link dropdown-toggle" style="margin-right: 0px;padding: 6px 0px;" 
				data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
				<span class="caret"></span> <span class="sr-only">Toggle
					Dropdown</span>
			</button>
			<ul class="dropdown-menu">
				<li><a href="javascript:void(0)" onclick="autoReload(0.5)">0.5分钟自动刷新</a></li>
				<li><a href="javascript:void(0)" onclick="autoReload(1)">1分钟自动刷新</a></li>
				<li><a href="javascript:void(0)" onclick="autoReload(3)">3分钟自动刷新</a></li>
				<li><a href="javascript:void(0)" onclick="autoReload(5)">5分钟自动刷新</a></li>
				<li role="separator" class="divider" style="background-color: #006DC0;"></li>
				<li><a id="refresh-c" href="javascript:void(0)">自定义</a></li>
			</ul>
		</div>
		<button id="save" type="button" class="btn btn-link">保存登机口</button>
		<button id="cancel" type="button" class="btn btn-link">取消调整</button>
		<shiro:hasPermission name="fd:gateSetting">
			<button id="setting" type="button" class="btn btn-link">配置</button>
		</shiro:hasPermission>
	</div>
	<canvas id="SJgantt" class="SJgantt"></canvas>
	<div style="display:none" id="saveData">
		<table id="dataTable">
		</table>
	</div>
	<div id="settingDiv" style="display:none">
		<div id="startTime">
			<span>开始时间</span>
			<select class="form-control startAttr" style="width:100px">
				<option value="CETD">计算ETD</option>
				<option value="ETD">ETD</option>
			</select>
			<select class="form-control startRel" style="width:50px">
				<option value="-">-</option>
				<option value="+">+</option>
			</select>
			<input type="number" min="0" class="form-control startMinute" style="width:100px">
			<span>分钟</span>
		</div>
		<div id="endTime">
			<span>结束时间</span>
			<select class="form-control endAttr" style="width:100px">
				<option value="CETD">计算ETD</option>
				<option value="ETD">ETD</option>
			</select>
			<select class="form-control endRel" style="width:50px">
				<option value="-">-</option>
				<option value="+">+</option>
			</select>
			<input type="number" min="0" class="form-control endMinute" style="width:100px">
			<span>分钟</span>
		</div>
	</div>
	<script>
		$(function(){
			$("body").css("overflow","hidden");
		})
	</script>
	<script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/fdGateGantt.js"></script>
</body>
</html>