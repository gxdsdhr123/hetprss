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
<script type="text/javascript" src="${ctxStatic}/jquery/plugins/SJgantt/SJgantt-fd.js"></script>
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
		<!-- <button id="filter" type="button" class="btn btn-link">筛选</button> -->
		<shiro:hasPermission name="fd:gantt:graph">
			<button id="graph" type="button" class="btn btn-link">列表</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:gantt:importvip">
			<button id="importvip" type="button" class="btn btn-link">要客</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:gantt:message">
			<button id="message" type="button" class="btn btn-link">报文</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:gantt:order">
			<button id="order" type="button" class="btn btn-link">指令</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:gantt:ensure">
			<button id="ensure" type="button" class="btn btn-link">保障</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="fd:gantt:error">
			<button id="error" type="button" class="btn btn-link">异常</button>
		</shiro:hasPermission>
	</div>
	<canvas id="SJgantt" class="SJgantt"></canvas>
	<form id="printForm" action="${ctx}/flightDynamic/print" method="post"
		style="display: none">
		<textarea id="printTitle" name="title"></textarea>
		<textarea id="printData" name="data"></textarea>
	</form>
	<div id="msgDiv" style="display: none">
		<table id="msgTable"></table>
	</div>
	<div id="recycleBox" style="display: none">
		<form>
			<div class="radio">
				<label> <input type="radio" name="optionsRadios"
					data-type="2" value="option1"> 进港航班取消
				</label>
			</div>
			<div class="radio">
				<label> <input type="radio" name="optionsRadios"
					data-type="3" value="option2"> 出港航班取消
				</label>
			</div>
			<div class="radio">
				<label> <input type="radio" name="optionsRadios"
					data-type="1" value="option3" checked> 进出港航班取消
				</label>
			</div>
		</form>
	</div>
	<script>
		$(function(){
			$("body").css("overflow","hidden");
			$("#graph").click(function(){
				var form = $("<form id='graphForm' style='display:none' action='"+ctx + "/flightDynamic/list'></form>")
				$("body").append(form);
				$("#graphForm").submit();
			});
		})
	</script>
	<script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/flightDynGantt.js"></script>
</body>
</html>