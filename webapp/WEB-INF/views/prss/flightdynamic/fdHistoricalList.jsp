<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>历史航班动态（列表）</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<link href="${ctxStatic}/prss/flightdynamic/css/flightDynList.css" rel="stylesheet" />
<body>
	<input type="hidden" id="hisdate">
	<div id="tool-box">
		<!-- <button id="refresh" type="button" class="btn btn-link">刷新</button> -->
		<button id="filter" type="button" class="btn btn-link">筛选</button>
		<shiro:hasPermission name="fd:hist:message">
			<button id="message" type="button" class="btn btn-link">报文</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:hist:order">
			<button id="order" type="button" class="btn btn-link">指令</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:hist:ensure">
			<button id="ensure" type="button" class="btn btn-link">保障</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:hist:delay">
			<button id="delay" type="button" class="btn btn-link">延误</button>
		</shiro:hasPermission>
		<!-- 设置 -->
		<%-- <shiro:hasPermission name="fd:hist:setting"> --%>
			<button type="button" class="btn btn-box-tool" id="setting" aria-expanded="false">
				<i class="fa fa-fw fa-gear"></i>
			</button>
		<%-- </shiro:hasPermission> --%>
		<!-- 打印 -->
		<shiro:hasPermission name="fd:hist:print">
			<button type="button" class="btn btn-box-tool" id="print" aria-expanded="false">
				<i class="fa fa-fw fa-print"></i>
			</button>
		</shiro:hasPermission>
		<div class="btn-group">
			<a class="btn btn-default dropdown-toggle" data-toggle="dropdown" id="searchOptBtn"
				aria-expanded="false">
				不限
				<span class="fa fa-caret-down"></span>
			</a>
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
		<div class="layui-input-inline">
			<input id="searchTab" type="text" class="layui-input" style="border:none;">
		</div>
	</div>
	<div id="baseTables">
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
		<shiro:hasPermission name="fd:delay">
			<li data-item="delay"><a>延误录入</a></li>
		</shiro:hasPermission>
		<li role="separator" class="divider" style="visibility: hidden;"></li>
		<!-- <li data-item="cancle"><a>取消(限手工)</a></li>
		<li data-item="delFlt"><a>删除(限手工)</a></li> -->
		<!-- <li role="separator" class="divider"></li> -->
	</ul>
	<form id="printForm" action="${ctx}/fdHistorical/print" method="post" style="display: none">
		<textarea id="printTitle" name="title"></textarea>
		<textarea id="printData" name="data"></textarea>
		<textarea id="schema" name="schema"></textarea>
		<textarea id="sortName" name="sortName"></textarea>
		<textarea id="sortOrder" name="sortOrder"></textarea>
	</form>
	<div id="msgDiv" style="display: none">
		<table id="msgTable"></table>
	</div>
	<%@include file="/WEB-INF/views/prss/flightdynamic/fdHistoricalFilter.jsp"%>
	<script type="text/javascript"
		src="${ctxStatic}/bootstrap/plugins/bootstrap-table/extensions/bootstrap-table-contextmenu.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/fdHistoricalList.js"></script>
</body>
</html>