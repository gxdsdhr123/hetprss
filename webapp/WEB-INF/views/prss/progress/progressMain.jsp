<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<link href="${ctxStatic}/jquery/plugins/jquery-ui/jquery-ui.min.css" rel="stylesheet">
<link href="${ctxStatic}/prss/scheduling/css/schedulingProgress.css" rel="stylesheet">
<script type="text/javascript" src="${ctxStatic}/jquery/plugins/slimscroll/jquery.slimscroll.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery/plugins/jquery-ui/jquery-ui.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/prss/progress/progressMain.js"></script>
</head>
<body>
	<input id="jobType" type="hidden" value="${jobType}">
	<div id="detailDiv"></div>
	<section class="content">
		<div class="row no-padding" id="tool-box">
			<div class="col-md-12 col-sm-12 col-xs-12 no-padding">
				<form id="searchForm" class="layui-form no-padding" onsubmit="return false">
					 <div class="layui-form-item">
					 	<div class="layui-inline">
					      <label class="layui-form-label">航班号：</label>
					      <div class="layui-input-inline">
					        <input id="fltNo" class="layui-input" type="text" placeholder="输入关键字回车">
					      </div>
					    </div>
					    <div class="layui-inline">
					      <label class="layui-form-label">航空公司：</label>
					      <label class="layui-form-label text-left multi-items">
					      	<a id="airlineGroupText" href="javascript:void(0)">全部</a>
					      </label>
					      <div id="airlineItems" class="text-center" style="display: none;">
					      		<shiro:hasPermission name="PROGRESS:ALN:MU">
					      			<input name="airlineGroup" type="checkbox" title="东航系" value="1">
					      		</shiro:hasPermission>
					      		<shiro:hasPermission name="PROGRESS:ALN:CZ">
					      			<input name="airlineGroup" type="checkbox" title="南航系" value="2">
					      		</shiro:hasPermission>
					      		<shiro:hasPermission name="PROGRESS:ALN:MF">
					      			<input name="airlineGroup" type="checkbox" title="厦航" value="2">
					      		</shiro:hasPermission>
						        <shiro:hasPermission name="PROGRESS:ALN:HU">
					      			<input name="airlineGroup" type="checkbox" title="海航系" value="3">
					      		</shiro:hasPermission>
					      		<shiro:hasPermission name="PROGRESS:ALN:FU">
					      			<input name="airlineGroup" type="checkbox" title="福州航空" value="3">
					      		</shiro:hasPermission>
					      		<shiro:hasPermission name="PROGRESS:ALN:GBS_I">
					      			<input name="airlineGroup" type="checkbox" title="BGS外航" value="4">
					      		</shiro:hasPermission>
					      		<%-- 未航空公司配置权限的情况下全部 --%>
					      		<shiro:hasPermission name="PROGRESS:ALN:ALL">
					      			<input name="airlineGroup" type="checkbox" title="东航系" value="1">
					      			<input name="airlineGroup" type="checkbox" title="南航系" value="2">
					      			<input name="airlineGroup" type="checkbox" title="海航系" value="3">
					      			<input name="airlineGroup" type="checkbox" title="BGS外航" value="4">
					      		</shiro:hasPermission>
					      </div>
					    </div>
					    <div class="layui-inline">
					      <label class="layui-form-label">预警级别：</label>
					      <label class="layui-form-label text-left multi-items">
					      	<a id="alarmLevelText" href="javascript:void(0)">全部</a>
					      </label>
					      <div id="alarmItems" class="text-center" style="display:none">
						      <input name="alarmLevel" type="checkbox" title="正常" value="0">
						      <input name="alarmLevel" type="checkbox" title="一级" value="1">
					          <input name="alarmLevel" type="checkbox" title="二级" value="2">
					          <input name="alarmLevel" type="checkbox" title="三级" value="3">
					      </div>
					    </div>
					    <div class="layui-inline">
					      <label class="layui-form-label">进出港类型：</label>
					      <div class="layui-input-inline">
					        	<select id="ioType" lay-filter="ioType">
					        		<option value="all">全部</option>
					        		<option value="A0">单进</option>
					        		<option value="D0">单出</option>
					        		<option value="A1D1">过站</option>
					        	</select>
					      </div>
					    </div>
					 </div>
				</form>
			</div>
		</div>
		<div class="row">
			<div class="col-md-4 col-sm-4 col-xs-4">
				<div class="box box-solid">
					<div class="box-header with-border" style="color: #999">
						<h4 class="box-title">
							<label>未分配</label>
						</h4>
					</div>
					<div class="box-body">
						<div id="flight4" class="task-list"></div>
					</div>
				</div>
			</div>
			<div class="col-md-4 col-sm-4 col-xs-4">
				<div class="box box-solid">
					<div class="box-header with-border" style="color: #999">
						<h4 class="box-title">
							<label>已排</label>
						</h4>
					</div>
					<div class="box-body">
						<div id="flight2" class="task-list"></div>
					</div>
				</div>
			</div>
			<div class="col-md-4 col-sm-4 col-xs-4">
				<div class="box box-solid">
					<div class="box-header with-border" style="color: #999">
						<h4 class="box-title">
							<label>完成</label>
						</h4>
					</div>
					<div class="box-body">
						<div id="flight3" class="task-list"></div>
					</div>
				</div>
			</div>
		</div>
	</section>
</body>
</html>