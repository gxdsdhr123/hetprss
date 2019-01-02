<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>航班动态（列表）</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/prss/flightdynamic/css/flightDynList.css" rel="stylesheet" />
</head>
<body>
	<input id="loginName" type="hidden" value="${fns:getUser().loginName}">
	<div id="tool-box">
		<div class="btn-group">
			<button id="refresh" type="button" class="btn btn-link">刷新</button>
			<button type="button" class="btn btn-link dropdown-toggle"
				style="margin-right: 0px; padding: 6px 0px;" data-toggle="dropdown" aria-haspopup="true"
				aria-expanded="false">
				<span class="caret"></span>
				<span class="sr-only">Toggle Dropdown</span>
			</button>
			<ul class="dropdown-menu">
				<li>
					<a href="javascript:void(0)" onclick="autoReload(10)">10秒钟自动刷新</a>
				</li>
				<li>
					<a href="javascript:void(0)" onclick="autoReload(30)">30秒钟自动刷新</a>
				</li>
				<li>
					<a href="javascript:void(0)" onclick="autoReload(60)">1分钟自动刷新</a>
				</li>
				<li>
					<a href="javascript:void(0)" onclick="autoReload(180)">3分钟自动刷新</a>
				</li>
				<li>
					<a href="javascript:void(0)" onclick="autoReload(300)">5分钟自动刷新</a>
				</li>
				<li role="separator" class="divider" style="background-color: #006DC0;"></li>
				<li>
					<a id="refresh-c" href="javascript:void(0)">自定义</a>
				</li>
			</ul>
		</div>
		<button id="filter" type="button" class="btn btn-link">筛选</button>
		<shiro:hasPermission name="fd:graph">
			<button id="graph" type="button" class="btn btn-link">甘特图</button>
		</shiro:hasPermission>
<%-- 		<shiro:hasPermission name="fd:ydjk"> --%>
<!-- 			<button id="ydjk" type="button" class="btn btn-link">远登机口</button> -->
<%-- 		</shiro:hasPermission> --%>
		<shiro:hasPermission name="fd:cancelFlt">
		<button id="fd:cancelFlt" type="button" class="btn btn-link" onclick="cancelFlt()">取消</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:fdRel">
			<button id="fdRel" type="button" class="btn btn-link">机号拆分</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:create">
		<button id="create" type="button" class="btn btn-link">新增</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:delFlt">
<!-- 		<button id="delFlt" type="button" class="btn btn-link" onclick="delFlt()">删除</button> -->
		<button id="delFlt" type="button" class="btn btn-link" >删除</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:ensure">
			<button id="ensure" type="button" class="btn btn-link">保障</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:message">
			<button id="message" type="button" class="btn s">报文</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:order">
			<button id="order" type="button" class="btn btn-link">指令</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:importvip">
			<button id="importvip" type="button" class="btn btn-link">要客</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:error">
			<button id="error" type="button" class="btn btn-link">异常</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:printPassenger">
			<button id="printPassenger" type="button" class="btn btn-link">离港导出</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:modify">
			<input type="hidden" value="1" id="editBtn" title="修改">
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:editAptNo">
			<input type="hidden" value="1" id="editAptNoBtn" title="修改机号">
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:editActType">
			<input type="hidden" value="1" id="editActTypeBtn" title="修改机型">
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:editProperty">
			<input type="hidden" value="1" id="editPropertyBtn" title="修改性质">
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:editAttrCode">
			<input type="hidden" value="1" id="editAttrCodeBtn" title="修改属性">
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:editFltDate">
			<input type="hidden" value="1" id="editFltDateBtn" title="修改航班日期">
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:editDelayDate">
			<input type="hidden" value="1" id="editDelayBtn" title="修改延误信息">
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:editDiversion">
			<input type="hidden" value="1" id="editDiversionBtn" title="修改备降信息">
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:departSort">
			<input type="hidden" value="1" id="editSortBtn" title="修改出港排序">
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:pushoutTm">
			<input type="hidden" value="1" id="editPushoutBtn" title="修改许可推出时间">
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:editGate">
			<input type="hidden" value="1" id="editGateBtn" title="修改登机口">
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:editBagCrsl">
			<input type="hidden" value="1" id="editBagCrslBtn" title="修改行李转盘">
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:editCounter">
			<input type="hidden" value="1" id="editCounter" title="修改值机柜台">
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:editCheckinStatus">
			<input type="hidden" value="1" id="editCheckinStatus" title="修改值机状态">
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:editHandOverTm">
			<input type="hidden" value="1" id="editHDTimeBtn" title="修改移交时间">
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:editBrdStatus">
			<input type="hidden" value="1" id="editBrdBtn" title="修改出港状态">
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:editBrdBtm">
			<input type="hidden" value="1" id="editBrdBtmBtn" title="修改登机开始时间">
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:editBrdEtm">
			<input type="hidden" value="1" id="editBrdEtmBtn" title="修改登机结束时间">
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:editTZDJKTm">
			<input type="hidden" value="1" id="editTZDJKTmBtn" title="通知登机口时间">
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:initialFlt">
			<button id="initialFlt" type="button" class="btn btn-link">初始航班</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:initialGate">
			<button id="initialGate" type="button" class="btn btn-link">初始登机口</button>
		</shiro:hasPermission>
		<div class="btn-group pull-right">
			<shiro:hasPermission name="fd:setting">
				<button type="button" class="btn btn-box-tool" id="setting" aria-expanded="false">
					<i class="fa fa-fw fa-gear"></i>
				</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="fd:print">
				<button type="button" class="btn btn-box-tool" id="print"
					aria-expanded="false">
					<i class="fa fa-fw fa-print"></i>
				</button>
			</shiro:hasPermission>
			<select name="timeList" id="timeList" class="form-control" style="width: 150px;margin-left:20px;display:inline-block" onChange="changeFltInfo(this)">
				<option value="">全部航班列表</option>
				<option value="1" selected>今日航班列表</option>
				<option value="2">未执行航班列表</option>
				<option value="3">最近航班列表</option>
				<option value="4">次日航班列表</option>
				<option value="5">进港航班列表</option>
				<option value="6">出港航班列表</option>
			</select>
		</div>
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
				<a href="#" onclick="searchOpt('GATE',this)">登机口</a>
			</li>
			<li>
				<a href="#" onclick="searchOpt('CHECKIN_COUNTER',this)">值机柜台</a>
			</li>
			<li>
				<a href="#" onclick="searchOpt('',this)">不限</a>
			</li>
		</ul>
	</div>
	<div id="baseTables">
		<table id="baseTable"></table>
	</div>
	<div id="totalBox">
		共<span id="total">0</span>条记录
	</div>
	<ul id="context-menu" class="dropdown-menu">
		<li data-item="instructionView">
			<a>指令查看</a>
		</li>
		<li class="dropdown" data-item="fltDetail">
			<a href="#" class="dropdownMenu">
				航班详细信息
				<b class="fa fa-chevron-right"></b>
			</a>
			<ul class="dropdown-menu">
				<li data-item="departPsg">
					<a>离港信息录入</a>
				</li>
				<li data-item="timeDynamic">
					<a>时间动态</a>
				</li>
				<!-- <li data-item="guaranteeData"><a>保障资料</a></li> -->
				<li data-item="resourseState">
					<a>资源状态</a>
				</li>
				<li data-item="massageChange">
					<a>消息变更轨迹</a>
				</li>
			</ul>
		</li>
		<li class="dropdown" data-item="video"><a href="#"
												  class="dropdownMenu">视频<b class="fa fa-chevron-right"></b></a>
			<ul class="dropdown-menu">
				<li data-item="seat"><a>机位</a></li>
				<li data-item="gate"><a>登机口</a></li>
				<li data-item="carousel"><a>行李转盘</a></li>
				<li data-item="counter"><a>值机柜台</a></li>
				<li data-item="runway"><a>跑道</a></li>
			</ul></li>
		<li role="separator" class="divider" style="visibility: hidden;"></li>
	</ul>
	<form id="printForm" action="${ctx}/flightDynamic/print" method="post" style="display: none">
		<textarea id="printTitle" name="title"></textarea>
		<textarea id="printData" name="data"></textarea>
	</form>
	<form id="printPassengerForm" action="${ctx}/flightDynamic/printPassenger" method="post" style="display: none">
	</form>
	<!-- 导出文件form -->
	<form id="exportForm" method="post" style="display: none">
	</form>
	<div id="msgDiv" style="display: none">
		<table id="msgTable"></table>
	</div>
	<div id="recycleBox" style="display: none">
		<form>
			<div class="radio">
				<label>
					<input type="radio" name="optionsRadios" value="1">
					进港航班取消
				</label>
			</div>
			<div class="radio">
				<label>
					<input type="radio" name="optionsRadios" value="2">
					出港航班取消
				</label>
			</div>
			<div class="radio">
				<label>
					<input type="radio" name="optionsRadios" value="3">
					进出港航班取消
				</label>
			</div>
		</form>
		<form>
			<div class="radio">
				<label>
					<input type="radio" name="cancleFilterRadio" value="1">
					进港6小时
				</label>
			</div>
			<div class="radio">
				<label>
					<input type="radio" name="cancleFilterRadio" value="2">
					出港6小时
				</label>
			</div>
			<div class="radio">
				<label>
					<input type="radio" name="cancleFilterRadio" value="3">
					进出港6小时
				</label>
			</div>
		</form>
		<span>(勾选后此航班不做正常性统计)</span>
	</div>
	<%@include file="/WEB-INF/views/prss/flightdynamic/flightDynFilter.jsp"%>
	<script>
		$("#graph").click(function() {
			var form = $("<form id='graphForm' style='display:none' action='"+ctx + "/flightDynamic/listWalkthroughGantt'></form>")
			$("body").append(form);
			$("#graphForm").submit();
		});
// 		$("#ydjk").click(function() {
// 			var form = $("<form id='ydjkForm' style='display:none' action='"+ctx + "/flightDynamic/listGateGantt'></form>")
// 			$("body").append(form);
// 			$("#ydjkForm").submit();
// 		});
	</script>
	<script type="text/javascript"
		src="${ctxStatic}/bootstrap/plugins/bootstrap-table/extensions/bootstrap-table-contextmenu.min.js"></script>
	<script type="text/javascript"
		src="${ctxStatic}/prss/flightdynamic/flightDynList.js?timestemp=${fns:getTimestamp()}"></script>
	<script type="text/javascript"
			src="${ctxStatic}/prss/common/videoPlugin.js"></script>
</body>
</html>