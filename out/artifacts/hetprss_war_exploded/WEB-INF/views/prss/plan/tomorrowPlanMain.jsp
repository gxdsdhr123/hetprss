<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>次日计划</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript" src="${ctxStatic}/bootstrap/plugins/bootstrap-table/extensions/bootstrap-table-contextmenu.min.js"></script>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<link href="${ctxStatic}/prss/plan/css/planMain.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
var airportCodeSource = ${airportCodeSource};
var airlinesCodeSource = ${airlinesCodeSource};
var propertyCodeSource = ${propertyCodeSource};
/* var aircraftNumberSource = ${aircraftNumberSource}; */
var actTypeSource = ${actTypeSource};
var type = "${type}";
var checkPublish = ${checkPublished};
</script>
</head>
<body style="width: 2048px;">
	<div id="tool-box">
			<button id="refresh" type="button" class="btn btn-link">
				<!-- <i class="fa fa-cloud-upload">&nbsp;</i> -->
				刷新
			</button>
<%-- 		<shiro:hasPermission name="tomorrow:plan:import"> --%>
			<button id="import" type="button" class="btn btn-link">
				<!-- <i class="fa fa-cloud-upload">&nbsp;</i> -->
				计划导入
			</button>
<%-- 		</shiro:hasPermission> --%>
<%-- 		<shiro:hasPermission name="tomorrow:plan:create"> --%>
			<button id="create" type="button" class="btn btn-link">
				<!-- <i class="fa fa-plus">&nbsp;</i> -->
				新增
			</button>
<%-- 		</shiro:hasPermission> --%>
<%--        <shiro:hasPermission name="tomorrow:plan:send"> --%>
            <button id="publish" type="button" class="btn btn-link">
                <!-- <i class="fa fa-print">&nbsp;</i> -->
                                发布
            </button>
<%--        </shiro:hasPermission> --%>
<%-- 		<shiro:hasPermission name="tomorrow:plan:remove"> --%>
			<button id="remove" type="button" class="btn btn-link">
				<!-- <i class="fa fa-remove">&nbsp;</i> -->
				删除
			</button>
<%--        </shiro:hasPermission> --%>
<%--        <shiro:hasPermission name="tomorrow:plan:tomorrow"> --%>
            <button id="forecast" type="button" class="btn btn-link">
                <!-- <i class="fa fa-print">&nbsp;</i> -->
                                次日预测
            </button>
<%-- 		</shiro:hasPermission> --%>
<%-- 		<shiro:hasPermission name="tomorrow:plan:print"> --%>
			<button id="print" type="button" class="btn btn-link">
				<!-- <i class="fa fa-print">&nbsp;</i> -->
				打印
			</button>
<%-- 		</shiro:hasPermission> --%>
			<!-- 由于修改为一次全部发布，筛选条件无用，暂时保留后台逻辑<select id="planType" onchange="choosePlanType()">
				<option value="0,1,2">全部</option>
				<option value="2">计划已发布</option>
				<option value="0,1">计划未发布</option>
			</select> -->
			<input type="hidden" id="planType" value="0,1,2">
		<!-- 检索功能 -->
		<div class="btn-group">
			<a class="btn btn-default dropdown-toggle" data-toggle="dropdown" id="searchOptBtn" aria-expanded="false">
				不限
				<span class="fa fa-caret-down"></span>
			</a>
			<ul class="dropdown-menu">
					<li>
					<a href="#" onclick="searchOpt('flt_no',this)">航班号</a>
				</li>
				<li>
					<a href="#" onclick="searchOpt('aircraft_number',this)">机号</a>
				</li>
				<li>
					<a href="#" onclick="searchOpt('act_type',this)">机型</a>
				</li>
				<li>
					<a href="#" onclick="searchOpt('airports',this)">起场</a>
				</li>
				<li>
					<a href="#" onclick="searchOpt('airports',this)">落场</a>
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
	<!-- 进出港计划列表 -->
    <div class="row">
		<div class="col-xs-6 col-sm-6 col-md-6">
			<table id="baseTableLeft"></table>
		</div>
		<div class="col-xs-6 col-sm-6 col-md-6">
			 <table id="baseTableRight"></table>
		</div>
	</div>
	<!-- 右键查看报文 -->
	<ul id="context-menu" class="dropdown-menu">
		<li data-item="msgView">
			<a>查看报文</a>
		</li>
	</ul>
	<!-- 导出excel -->
	<form id="exportForm" method="post">
	</form>
	<!-- 上传导入中航信报文文件 -->
	<div id="upload" style="display: none">
	 	 <div class="layui-upload-list">
	    	<table class="layui-table">
		      	<thead>
		        	<tr>
		        		<th>文件名</th>
		        		<th>文件大小</th>
		        		<th>操作</th>
		      		</tr>
		      	</thead>
				<tbody>
					<tr>
						<td align="center"><span id="fileName"></span></td>
						<td align="center"><span id="fileSize"></span></td>
			      		<td align="center" style="width: 10%"><button class="layui-btn layui-btn-normal" id="chooseFile" type="button" onclick="fileInput.click()">选择文件</button></td>
		      		</tr>
		      		<tr>
						<td align="center"><span id="fileName1"></span></td>
						<td align="center"><span id="fileSize1"></span></td>
			      		<td align="center" style="width: 10%"><button class="layui-btn layui-btn-normal" id="chooseFile1" type="button" onclick="fileInput1.click()">选择文件</button></td>
		      		</tr>
		      		<tr>
						<td colspan="3" align="right">
							<button class="layui-btn" type="button" onclick="resetUpload()">重置</button>
							<button class="layui-btn" type="button" onclick="fileOnChange()">开始上传</button>
						</td>
		      		</tr>
		      	</tbody>
		    </table>
	  	</div>
	</div>
	<form id="fileList" action="${ctx}/tomorrow/plan/importPlan" method="post" enctype="multipart/form-data" style="display:none;">
		<input id="fileInput" name="file" type="file" multiple="multiple" onchange="uploadOnChange(this,'fileName','fileSize')">
		<input id="fileInput1" name="file" type="file" multiple="multiple" onchange="uploadOnChange(this,'fileName1','fileSize1')">
	</form>
	<!-- 发布前段校验计划数据是否存在空值提示窗 -->
	<div id="errMsg" style="display: none;overflow-x: auto; overflow-y: auto; height: 210px; width:450px;" ></div>
    <script type="text/javascript" src="${ctxStatic}/prss/plan/tomorrowPlanCommon.js"></script>
    <script type="text/javascript" src="${ctxStatic}/prss/plan/tomorrowPlanMain.js"></script>
</body>
</html>