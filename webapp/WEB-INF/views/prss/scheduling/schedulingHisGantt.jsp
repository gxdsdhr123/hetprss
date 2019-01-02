<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>指挥调度历史（甘特图）</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<%@include file="/WEB-INF/views/prss/flightdynamic/flightDynFilter.jsp"%>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<link href="${ctxStatic}/prss/flightdynamic/css/flightDynList.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/jquery/plugins/SJgantt/SJgantt-sd-his.js"></script>
<body>
	<input id="loginName" type="hidden" value="${fns:getUser().loginName}">
	<input type="hidden" id="schemaId" value="${schemaId}">
	<div id="tool-box">
		<shiro:hasPermission name="sc:gantthist:graph">
			<button id="graph" type="button" class="btn btn-link">列表</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="sc:gantthist:message">
			<button id="message" type="button" class="btn btn-link">报文</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="sc:gantthist:order">
			<button id="order" type="button" class="btn btn-link">指令</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="sc:gantthist:ensure">
			<button id="ensure" type="button" class="btn btn-link">保障</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="sc:gantthist:jobManageBtn">
			<button id="jobManageBtn" type="button" class="btn btn-link">作业管理</button>
		</shiro:hasPermission>
		<div class="layui-inline">
			<label class="layui-form-label" style="padding: 10px 10px !important;margin-bottom: 0px;">数据日期</label>
			<div class="layui-input-inline" style="padding: 5px 0px;">
				<input id="hisdate" name="hisdate" onClick="dateFilter();" class="layui-input" readonly="readonly"
					value="" type="text">
			</div>
		</div>
	</div>
	<canvas id="SJgantt" class="SJgantt"></canvas>
	<div id="stopMenu">
		<button id="stop" type="button" class="btn" onclick="stopPeople($(this))">停用</button>
	</div>
	<ul id="context-menu" class="dropdown-menu">
		<li data-item="instructionView"><a>指令查看</a></li>
		<li class="dropdown" data-item="fltDetail"><a href="#"
			class="dropdownMenu">航班详细信息<b class="fa fa-chevron-right"></b></a>
			<ul class="dropdown-menu">
				<li data-item="baggage"><a>旅客行李</a></li>
				<li data-item="timeDynamic"><a>时间动态</a></li>
				<li data-item="guaranteeData"><a>保障资料</a></li>
				<li data-item="resourseState"><a>资源状态</a></li>
				<li data-item="massageChange"><a>消息变更轨迹</a></li>
			</ul></li>
		<li class="dropdown" data-item="video"><a href="#"
			class="dropdownMenu">视频<b class="fa fa-chevron-right"></b></a>
			<ul class="dropdown-menu">
				<li data-item="seat"><a>机位</a></li>
				<li data-item="gate"><a>登机口</a></li>
				<li data-item="carousel"><a>行李转盘</a></li>
				<li data-item="counter"><a>值机柜台</a></li>
				<li data-item="runway"><a>跑道</a></li>
			</ul></li>
		<li data-item="sendMessage"><a>发送报文</a></li>
		<li role="separator" class="divider"></li>
		<li data-item="cancle"><a>取消</a></li>
		<li data-item="alternate"><a>备降场</a></li>
		<li data-item="ssgDetail"><a>要客详情</a></li>
		<li role="separator" class="divider"></li>
	</ul>
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
		$("#graph").click(function() {
			var schemaId = $('#schemaId').val();
			var form = $("<form id='graphForm' style='display:none' action='"+ctx + "/schedulingHisList/list'></form>")
			form.append($("<input type='text' name='schemaId' value='"+schemaId+"'>"));
			$("body").append(form);
			$("#graphForm").submit();
		});
		$("body").css("overflow","hidden");
	</script>
	<script type="text/javascript"
		src="${ctxStatic}/prss/scheduling/schedulingHisGantt.js"></script>
</body>
</html>