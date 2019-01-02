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
#stopMenu,#rightMenu{
position: absolute;
display:none;
}
.ganttBtn,#member{
background-color: rgba(2, 17, 50,0.9);
color: #CFCFCF;
width:130px;
}
.ganttBtn:hover,#member:hover{
background-color: rgba(19, 40, 93,0.9);
color: #FFFFFF;
}
#rightMenu {
width:130px;
}
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
#tool-box button{
	padding:6px 0px;
	margin-left: 5px;
}
</style>
<script type="text/javascript" src="${ctxStatic}/jquery/plugins/SJgantt/SJgantt-fdWTH.js"></script>
<body>
	<input id="loginName" type="hidden" value="${fns:getUser().loginName}">
	<div id="tool-box" style="padding:5px;">
		<div class="btn-group">
			<button id="refresh" type="button" class="btn btn-link">刷新</button>
			<button type="button" class="btn btn-link dropdown-toggle" style="margin-right: 0px;padding: 6px 0px;" 
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
				<li role="separator" class="divider" style="background-color: #006DC0;"></li>
				<li><a id="refresh-c" href="javascript:void(0)">自定义</a></li>
			</ul>
		</div>
<!-- 		<button id="graph" type="button" class="btn btn-link">动态列表</button> -->
		<button id="lock" type="button" class="btn btn-link">锁定航班</button>
		<button id="unlock" type="button" class="btn btn-link">解锁航班</button>
		<button id="stay" type="button" class="btn btn-link">增加驻场</button>
		<button id="delstay" type="button" class="btn btn-link">删除驻场</button>
		<button id="stop" type="button" class="btn btn-link">停用机位</button>
		<button id="toggle" type="button" class="btn btn-link">显示隐藏机位</button>
		<button id="save" type="button" class="btn btn-link">保存机位</button>
		<button id="cancel" type="button" class="btn btn-link">取消调整</button>
		<button id="drag" type="button" class="btn btn-link">拖飞机</button>
		<button id="takeStand" type="button" class="btn btn-link">机位调整</button>
		<button id="hideNonexecution" type="button" class="btn btn-link">隐藏未执行航班</button>
		<select id="quickDate" class="form-control">
			<option value="today">今日图形</option>
			<option value="tomorrow">次日图形</option>
		</select>
		<input id="search" name="search" style="width:160px;display: inline-block;" placeholder="航班号、机号   搜索" class="layui-input" type="text">
		<input id="hisdate" name="hisdate" placeholder="查询历史：YYYYMMDD" onClick="dateFilter();" class="layui-input" readonly="readonly" value="" type="text">
	</div>
	<canvas id="SJgantt" class="SJgantt"></canvas>
	<div id="stopMenu">
		<button id="stop" type="button" class="btn ganttBtn" onClick="doRelease()">恢复停用</button>
	</div>
	<div id="rightMenu">
		<button id="changeStand" type="button" class="btn ganttBtn" onClick="changeStand()">更改机位</button>
		<button id="changeGate" type="button" class="btn ganttBtn" onClick="changeGate()">更改登机口</button>
		<hr style="border-color: #999;margin:0px">
		<button id="gateChangeMsg" type="button" class="btn ganttBtn" data-id="107467" data-io="O" onClick="openMessage(this)">出港登机口变更</button>
		<button id="gateStandChangeMsg" type="button" class="btn ganttBtn" data-id="107427" data-io="I" onClick="openMessage(this)">机位登机口变更</button>
		<button id="ataChangeMsg" type="button" class="btn ganttBtn" data-id="107453" data-io="I" onClick="openMessage(this)">落地时间变更</button>
	</div>
	<div id="editStayDiv" style="display:none">
		<form class="form-horizontal" style="padding:15px 50px 15px 15px" >
		  <div class="form-group">
		    <label for="stayActnum" class="col-sm-4 control-label">机号旧值</label>
		    <div class="col-sm-8">
		      <input type="text" class="form-control" id="stayActnumOld"  >
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="stayStand" class="col-sm-4 control-label">机号新值</label>
		    <div class="col-sm-8">
		      <input type="text" class="form-control" id="stayActnumNew" onkeyup="this.value = this.value.toUpperCase();">
		    </div>
		  </div>
		</form>
	</div>
	<div id="stayDiv" style="display:none">
		<form class="form-horizontal" style="padding:15px 50px 15px 15px" >
		  <div class="form-group">
		    <label for="stayActnum" class="col-sm-2 control-label">机号</label>
		    <div class="col-sm-10">
		      <select class="form-control" id="stayActnum"></select>
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="stayStand" class="col-sm-2 control-label">机位</label>
		    <div class="col-sm-10">
		      <select class="form-control" id="stayStand" ></select>
		    </div>
		  </div>
		</form>
	</div>
	<div id="stopDiv" style="display:none">
		<form class="form-horizontal" style="padding:15px 50px 15px 15px">
		  <div class="form-group">
		    <label for="stopStand" class="col-sm-4 control-label">机位</label>
		    <div class="col-sm-8">
		      <input type="text" class="form-control" id="stopStand">
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="stopStartTime" class="col-sm-4 control-label">停用开始时间</label>
		    <div class="col-sm-8">
		      <input type="text" class="form-control" id="stopStartTime" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})">
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="stopEndTime" class="col-sm-4 control-label">停用结束时间</label>
		    <div class="col-sm-8">
		      <input type="text" class="form-control" id="stopEndTime" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})">
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="stopRemark" class="col-sm-4 control-label">备注</label>
		    <div class="col-sm-8">
		      <textarea rows="3" class="form-control" id="stopRemark" style="background-color: #002F63;border: 1px solid #006DC0;"></textarea>
		    </div>
		  </div>
		</form>
	</div>
	<div id="visibleDiv" style="display:none;">
		<form style="padding:20px">
			<div class="form-group">
		    	<label for="showStand">选择机位</label>
		    	<select id="showStand" class="form-control"></select>
		  	</div>
		</form>
	</div>
	<div id="takeStandDiv" style="display:none;">
		<form style="padding:20px">
			<div class="layui-form-item">
				<div class="layui-inline" style="margin-bottom:20px;">
			    	<label for="showStand" class="layui-form-label">开始时间</label>
					<div class="layui-input-inline">
			    		<input type='text' maxlength="20" name='beginTime' id="beginTime"
								placeholder='请选择日期' class='form-control'
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'endTime\')}'});" />
					</div>
				</div>
				<div class="layui-inline">
			    	<label for="showStand" class="layui-form-label">结束时间</label>
			    	<div class="layui-input-inline">
			    		<input type='text' maxlength="20" name='endTime' id="endTime"
								placeholder='请选择日期' class='form-control'
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'beginTime\')}'});" />
			    	</div>
			    </div>
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
	<script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/fdWalkthroughGantt.js"></script>
</body>
</html>