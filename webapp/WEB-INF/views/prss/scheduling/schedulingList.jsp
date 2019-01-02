<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>指挥调度（列表）</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
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
#error .label{
	position: absolute;
	top: 1px;
	right: -8px;
	text-align: center;
	font-size: 11px;
	padding: 2px 3px;
	line-height: .9;
	z-index: 1;
}
</style>
</head>
<body>
	<input id="loginName" type="hidden" value="${fns:getUser().loginName}">
	<input type="hidden" id="schemaId" value="${schemaId}">
	<input type="hidden" id="user" value="${fns:getUser()}">
	<div id="tool-box">
		<div class="btn-group">
			<button id="refresh" type="button" class="btn btn-link">刷新</button>
			<button type="button" class="btn btn-link dropdown-toggle"
				data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
				<span class="caret"></span> <span class="sr-only">Toggle
					Dropdown</span>
			</button>
			<ul class="dropdown-menu">
				<li><a href="javascript:void(0)" onclick="autoReload(10)">10秒钟自动刷新</a></li>
				<li><a href="javascript:void(0)" onclick="autoReload(30)">30秒钟自动刷新</a></li>
				<li><a href="javascript:void(0)" onclick="autoReload(60)">1分钟自动刷新</a></li>
				<li><a href="javascript:void(0)" onclick="autoReload(180)">3分钟自动刷新</a></li>
				<li><a href="javascript:void(0)" onclick="autoReload(300)">5分钟自动刷新</a></li>
				<li role="separator" class="divider"></li>
				<li><a id="refresh-c" href="javascript:void(0)">自定义</a></li>
			</ul>
		</div>
		<button id="filter" type="button" class="btn btn-link">筛选</button>
		<shiro:hasPermission name="sc:graph">
			<button id="graph" type="button" class="btn btn-link">图形</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="sc:setting">
			<button id="setting" type="button" class="btn btn-link">设置</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="sc:print">
			<button id="print" type="button" class="btn btn-link">打印</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="sc:memberPlan">
			<button id="memberPlan" type="button" class="btn btn-link">人员计划</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="sc:divisionBtn">
			<button id="divisionBtn" type="button" class="btn btn-link">人员分工</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="sc:autoManual">
			<button id="autoManual" type="button" class="btn btn-link">任务分配</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="sc:jobManageBtn">
			<button id="jobManageBtn" type="button" class="btn btn-link">作业管理</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="sc:message">
			<button id="message" type="button" class="btn btn-link">报文</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="sc:order">
			<button id="order" type="button" class="btn btn-link">指令</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="sc:ensure">
			<button id="ensure" type="button" class="btn btn-link">保障</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="sc:error">
			<button id="error" type="button" class="btn btn-link">异常</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="sc:mission">
			<button id="mission" type="button" class="btn btn-link">任务指派单</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="sc:serviceConfirm">
			<button id="serviceConfirm" type="button" class="btn btn-link">服务项目确认书</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="sc:editMaintenance">
			<input type="hidden" value="1" id="editMaintenanceBtn" title="机务备注">
		</shiro:hasPermission>
		<select name="timeList" id="timeList" class="form-control" style="width: 150px;display: inline-block;" onChange="changeFltInfo(this)">
			<option value="">全部航班列表</option>
			<option value="1">今日航班列表</option>
			<option value="2">未执行航班列表</option>
			<option value="3">最近航班列表</option>
			<option value="4">次日航班列表</option>
			<option value="5">进港航班列表</option>
			<option value="6">出港航班列表</option>
		</select>
		<form class="layui-form checkBoxes" action="">
			
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
		<li class="dropdown" data-item="video"><a href="#"
			class="dropdownMenu">视频<b class="fa fa-chevron-right"></b></a>
			<ul class="dropdown-menu">
				<li data-item="seat"><a>机位</a></li>
				<li data-item="gate"><a>登机口</a></li>
				<li data-item="carousel"><a>行李转盘</a></li>
				<li data-item="counter"><a>值机柜台</a></li>
				<li data-item="runway"><a>跑道</a></li>
			</ul></li>
		<shiro:hasPermission name="scheduling:sendMessage">
			<li data-item="sendMessage"><a>发送报文</a></li>
		</shiro:hasPermission>
		<li data-item="vipInfo"><a>要客详情</a></li>
		<shiro:hasPermission name="sc:error">
			<li data-item="exception"><a>异常查看</a></li>
		</shiro:hasPermission>
		<shiro:hasPermission name="sc:dGateInput">
			<li data-item="dGateInput"><a>修改登机口</a></li>
		</shiro:hasPermission>
		<shiro:hasPermission name="sc:inputDelayInfo">
			<li data-item="delayInfo"><a>航延信息</a></li>
		</shiro:hasPermission>
		<li role="separator" class="divider" style="visibility: hidden;"></li>
		<!-- <li data-item="cancle"><a>取消(限手工)</a></li>
		<li data-item="delFlt"><a>删除(限手工)</a></li> -->
		<!-- <li role="separator" class="divider"></li> -->
	</ul>

	<form id="printForm" action="${ctx}/scheduling/list/print" method="post"
		style="display: none">
		<textarea id="printTitle" name="title"></textarea>
		<textarea id="printData" name="data"></textarea>
		<input type="hidden" name="hasSheet2" id="hasSheet2"/>
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
	<!-- 导入货邮行报文数据 -->
	<div id="import" style="overflow-x: auto; overflow-y: auto; height: 100%; width:100%;display: none">
		<table style="width: 100%">
			<tr>
				<td style="width: 50%;height: 380px">
					<div>
						<span style="font-size: 18px">货邮信息</span><br>
						<textarea id="fltMail" style="background-color: black;width: 100%;height: 380px;resize:none"></textarea>
					</div>
				</td>
				<td style="width: 50%;height: 400px">
					<div>
						<span style="font-size: 18px">行李信息</span><br>
						<textarea id="fltPackage" style="background-color: black;width: 100%;height: 380px;resize:none"></textarea>
					</div>
				</td>
			</tr>
		</table>
	</div>
	<!-- 打印服务项目确认书 -->
	<form id="exportForm" method="post">
	</form>
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
		src="${ctxStatic}/prss/scheduling/schedulingList.js?timestemp=${fns:getTimestamp()}"></script>
	<script type="text/javascript"
			src="${ctxStatic}/prss/common/videoPlugin.js"></script>
	<script type="text/javascript">
        $(document).ready(function() {
            if($("#error").length > 0){
                $("#error").click(function(){
                    $("#error .label").remove();
                });
                var user = $("#user").val();
                if(!localStorage["latestErrorTimeOf"+user]){
                    localStorage["latestErrorTimeOf"+user] = "1970-01-01 00:00:00";
                }
                var time = localStorage["latestErrorTimeOf"+user];
                $.ajax({
                    type:'post',
                    url:ctx+"/taskmonitor/getUnreadErrorNum",
                    data:{
                        time:time
                    },
                    success:function(num){
                        if(num == 0){
                            $("#error .label").remove();
                        }else{
                            $("#error").append("<span class=\"label label-danger\">"+num+"</span>");
                        }
                    }
                });
            }
        });
	</script>
</body>
</html>