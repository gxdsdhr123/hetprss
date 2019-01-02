<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>指挥调度（甘特图）</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<%@include file="/WEB-INF/views/prss/flightdynamic/flightDynFilter.jsp"%>
<link href="${ctxStatic}/prss/flightdynamic/css/flightDynList.css" rel="stylesheet" />
<style type="text/css">
#stopMenu{
position: absolute;
display:none;
}
#stop{
background-color: rgba(2, 17, 50,0.9);
color: #CFCFCF;
}
#stop:hover{
background-color: rgba(05, 22, 59,0.9);
}
#stop:active{
background-color: rgba(19, 40, 93,0.9);
color: #FFFFFF;
}
#planRangeDate {
	padding: 20px 20px 0px 20px;
}

.planRangeDate {
	display: inline-block !important;
	width: 45% !important;
}

.planRangeDiv {
	width: 140px;
}

.planRangeLable {
	width: 110px;
}
</style>
<script type="text/javascript" src="${ctxStatic}/jquery/plugins/SJgantt/SJgantt-qc.js"></script>
<body>
	<input id="loginName" type="hidden" value="${fns:getUser().loginName}">
	<input type="hidden" id="schemaId" value="${schemaId}">
	<input type="hidden" id="types" value="${types}">
	<div id="tool-box">
		<div class="btn-group">
			<button id="refresh" type="button" class="btn btn-link">刷新</button>
			<button type="button" class="btn btn-link dropdown-toggle"
				data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
				<span class="caret"></span> <span class="sr-only">Toggle
					Dropdown</span>
			</button>
			<ul class="dropdown-menu">
				<li><a href="javascript:void(0)" onclick="autoReload(0.5)">0.5分钟自动刷新</a></li>
				<li><a href="javascript:void(0)" onclick="autoReload(1)">1分钟自动刷新</a></li>
				<li><a href="javascript:void(0)" onclick="autoReload(3)">3分钟自动刷新</a></li>
				<li><a href="javascript:void(0)" onclick="autoReload(5)">5分钟自动刷新</a></li>
				<li role="separator" class="divider"></li>
				<li><a id="refresh-c" href="javascript:void(0)">自定义</a></li>
			</ul>
		</div>
		<button id="filter" type="button" class="btn btn-link">筛选</button>
		<button id="graph" type="button" class="btn btn-link">列表</button>
		<button id="memberPlan" type="button" class="btn btn-link">人员计划</button>
		<button id="divisionBtn" type="button" class="btn btn-link">人员分工</button>
		<button id="autoManual" type="button" class="btn btn-link">任务分配</button>
		<button id="jobManageBtn" type="button" class="btn btn-link">作业管理</button>
		<button id="message" type="button" class="btn btn-link">报文</button>
		<button id="order" type="button" class="btn btn-link">指令</button>
		<!-- <button id="ensure" type="button" class="btn btn-link">保障</button> -->
		<button id="error" type="button" class="btn btn-link">异常</button>
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
	<div id="planRangeDate" style="display:none">
			<div class="layui-form-item">
				<label class="layui-form-label planRangeLable">开始时间</label>
				<div class="layui-input-inline planRangeDiv">
			    	<input class='layui-input rangeDate' type='text' placeholder='开始时间' 
			    		onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'%y-%M-{%d}',maxDate:'#F{$dp.$D(\'stopEnd\')}'})" id="stopStart">
			    </div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label planRangeLable">结束时间</label>
				<div class="layui-input-inline planRangeDiv">
			    	<input class='layui-input rangeDate' type='text' placeholder='结束时间' 
			    		onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'#F{$dp.$D(\'stopStart\')}'})" id="stopEnd">
			    </div>
			</div>
			<div class="layui-form-item">
		    <label class="layui-form-label planRangeLable">停用原因</label>
		    <div class="layui-input-inline planRangeDiv">
		    	<select name="reason" id="reason">
		      		<option value="1">工作原因</option>
		      		<option value="2">非工作原因</option>
		      		<option value="3">其它</option>
	     	  </select>
	     	  </div>
			</div>
	</div>
	<script>
		$("#graph").click(function() {
			var schemaId = $('#schemaId').val();
			var form = $("<form id='graphForm' style='display:none' action='"+ctx + "/scheduling/list/list'></form>")
			form.append($("<input type='text' name='schemaId' value='"+schemaId+"'>"));
			$("body").append(form);
			$("#graphForm").submit();
		});
		$("body").css("overflow","hidden");
	</script>
	<script type="text/javascript"
		src="${ctxStatic}/bootstrap/plugins/bootstrap-table/extensions/bootstrap-table-contextmenu.min.js"></script>
	<script type="text/javascript"
		src="${ctxStatic}/prss/scheduling/schedulingQCGantt.js"></script>
</body>
</html>