<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>任务分配监控图</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/jquery/plugins/jquery-ui/jquery-ui.min.css" rel="stylesheet" />
<link href="${ctxStatic}/prss/taskmonitor/css/taskmonitor-${schemaId}.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/prss/message/talk.js"></script>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<style type="text/css">
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
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	var jobTypes = ${jobTypes};
</script>
</head>
<body>
	<input type="hidden" id="schemaId" value="${schemaId }"/>
	<input type="hidden" id="user" value="${fns:getUser()}">
	
	<div class="taskmonitor" style="width:100%;overflow: auto;">
		<!-- 按钮菜单 -->
		<div style="margin:5px 0; height:35px;">
		
			<div id="tool-box" class="pull-left">
				<form action="" class="layui-form" style="padding:0 !important;">
					<div class="btn-group">
						<button id="refresh" type="button" class="btn btn-link">刷新</button>
						<button type="button" class="btn btn-link dropdown-toggle"
							data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
							<span class="caret"></span> <span class="sr-only">Toggle
								Dropdown</span>
						</button>
						<ul class="dropdown-menu">
							<li><a href="javascript:void(0)" onclick="autoReload(0.16)">10秒自动刷新</a></li>
							<li><a href="javascript:void(0)" onclick="autoReload(0.5)">30秒自动刷新</a></li>
							<li><a href="javascript:void(0)" onclick="autoReload(1)">1分钟自动刷新</a></li>
							<li><a href="javascript:void(0)" onclick="autoReload(3)">3分钟自动刷新</a></li>
							<li><a href="javascript:void(0)" onclick="autoReload(5)">5分钟自动刷新</a></li>
							<li role="separator" class="divider"></li>
							<li><a id="refresh-c" href="javascript:void(0)">自定义</a></li>
						</ul>
					</div>
					<shiro:hasPermission name="sc:monitor:memberPlan">
						<button id="memberPlan" type="button" class="btn btn-link">人员计划</button>
					</shiro:hasPermission>
					<shiro:hasPermission name="sc:monitor:divisionBtn">
						<button id="divisionBtn" type="button" class="btn btn-link">人员分工</button>
					</shiro:hasPermission>
					<shiro:hasPermission name="sc:autoManual">
						<button id="autoManual" type="button" class="btn btn-link">任务分配</button>
					</shiro:hasPermission>
					<shiro:hasPermission name="sc:monitor:jobManageBtn">
						<button id="jobManageBtn" type="button" class="btn btn-link">作业管理</button>
					</shiro:hasPermission>
					<shiro:hasPermission name="sc:monitor:error">
						<button id="error" type="button" class="btn btn-link">异常</button>
					</shiro:hasPermission>
					<button id="work" type="button" class="btn btn-link">加班</button>
					<%-- 单选按钮定义 --%>
					<c:choose>
						<c:when test="${schemaId == '9'}">
							<div class="btn-group" style="margin-left:20px;">
								<select id="ioTag"  lay-filter="ioTag">
									<option value="">全部航班</option>
									<option value="flag_A">单进</option>
									<option value="flag_D">单出</option>
									<option value="flag_G">过站</option>
								</select>
							</div>
							<div class="btn-group" style="margin-left:20px;">
								<select id="ynTag"  lay-filter="ynTag">
									<option value="">全部机位</option>
									<option value="Y">远机位</option>
									<option value="N">近机位</option>
								</select>
							</div>
						</c:when>
						<c:otherwise>
							<span style="margin-left:20px;">
								<input type="radio" name="dayNight" value="" lay-filter="dayNight" title="全部" checked >
								<input type="radio" name="dayNight" value="1" lay-filter="dayNight" title="白班" >
								<input type="radio" name="dayNight" value="0" lay-filter="dayNight" title="夜班" >
							</span>
						</c:otherwise>
					</c:choose>
					<%-- 全部/部分 定义 --%>
					<span style="margin-left:20px;">
						<input type="checkbox" id="switch" lay-skin="switch" lay-text="全部|部分" lay-filter="switch" value="1">
					</span>
					<%-- 下拉菜单定义 --%>
					<c:choose>
						<c:when test="${schemaId == '7'}">
							<span style="margin-left:20px;width:100px;display: block; float:right;" >
								<select id="y_or_n"   lay-filter="yOrN">
									<option value="ALL">全部</option>
									<option value="Y">远机位</option>
									<option value="N">近机位</option>
								</select>
							</span>
							<span style="margin-left:20px;width:100px;display: block; float:right;" >
								<select id="in_or_out"   lay-filter="inOrOut">
									<option value="ALL">全部</option>
									<option value="A">进港航班</option>
									<option value="D">出港航班</option>
								</select>
							</span>
						</c:when>
						<c:when test="${schemaId == '13'}">
							<span style="margin-left:20px;width:100px;display: block; float:right;" >
								<select id="in_or_out"   lay-filter="inOrOut">
									<option value="1">进港调度</option>
									<option value="2" 
										<shiro:hasRole name="DFKYcgfwdd">
											selected = "selected"
										</shiro:hasRole>
									>出港调度</option>
								</select>
							</span>
						</c:when>
					</c:choose>
				</form>
			</div>
			
			
			<div class="pull-right form-inline" style="margin-right:20px;">
				<select id="search_type" class="form-control">
					<option value="F">航班号</option>
					<option value="P">人员</option>
				</select>
				<input id="search_text" class="form-control"/>
				<a class="legend" href="javascript:void(0)" onclick="showLegend()" title="图例"><i class="fa fa-question-circle"></i></a>
			</div>
		</div>
		<!-- 分配监控图 -->
		<div id="monitor" style="width:100%; position: relative;overflow: hidden;">
			
		</div>
	</div>
	<%--弹窗 --%>
	<jsp:include page="include/inc_pop${schemaId}.jsp"></jsp:include>
	<%--停用员工 --%>
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
	<%--车辆轨迹时间选择 --%>
	<div id="trajectory" style="display:none">
			<div class="layui-form-item">
				<label class="layui-form-label planRangeLable">开始时间</label>
				<div class="layui-input-inline planRangeDiv">
			    	<input class='layui-input rangeDate' type='text' placeholder='开始时间' 
			    		onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',maxDate:'%y-%M-{%d}'})" id="carStart">
			    </div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label planRangeLable">结束时间</label>
				<div class="layui-input-inline planRangeDiv">
			    	<input class='layui-input rangeDate' type='text' placeholder='结束时间' 
			    		onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'#F{$dp.$D(\'carStart\')}'})" id="carEnd">
			    </div>
			</div>
	</div>
	
	<div id="workDiv"  style="display:none">
		<div class="leftDiv">
			<button type="button" class="btn btn-primary btn-block chooseAll">全选</button>
			<ul class="list-group person-list">
<%-- 					<li class="list-group-item operator" data-code="${worker.id}">${worker.name}</li> --%>
			</ul>
		</div>
		<div class="rightDiv">
			<form>
			  <div class="form-group">
			    <label for="overWorkStart">开始时间</label>
			    <input type="text" class="form-control" id="overWorkStart" placeholder="格式：HHmm(+)">
			  </div>
			  <div class="form-group">
			    <label for="overWorkEnd">结束时间</label>
			    <input type="text" class="form-control" id="overWorkEnd" placeholder="格式：HHmm(+)">
			  </div>
			</form>
		</div>
	</div>
	<div id="shiftsDiv" style="display:none;width:798px;height:200px">
		<table id="shiftsTable"></table>
	</div>
	<script type="text/javascript" src="${ctxStatic}/jquery/plugins/jquery-ui/jquery-ui.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/prss/taskmonitor/chart/task-chart-${schemaId}.js"></script>
	<script type="text/javascript" src="${ctxStatic}/prss/taskmonitor/taskmonitor-${schemaId}.js"></script>
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