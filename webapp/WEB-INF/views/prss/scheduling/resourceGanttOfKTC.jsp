<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>客梯车资源甘特图</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<link href="${ctxStatic}/prss/flightdynamic/css/flightDynList.css" rel="stylesheet" />
<style type="text/css">
#carMenu{
position: absolute;
display:none;
}
#reset{
background-color: rgba(2, 17, 50,0.9);
color: #CFCFCF;
}
#reset:hover{
background-color: rgba(05, 22, 59,0.9);
}
#reset:active{
background-color: rgba(19, 40, 93,0.9);
color: #FFFFFF;
}
</style>
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery/plugins/SJgantt/SJgantt-res-ktc.js"></script>
<body>
	<input id="loginName" type="hidden" value="${fns:getUser().loginName}">
	<input type="hidden" id="schemaId" value="${schemaId}">
	<div id="tool-box">
<%-- 		<shiro:hasPermission name="sc:resourcegantt:refresh"> --%>
			<button id="refresh" type="button" class="btn btn-link">刷新</button>
<%-- 		</shiro:hasPermission> --%>
<%-- 		<shiro:hasPermission name="sc:resourcegantt:setting"> --%>
			<button id="setting" type="button" class="btn btn-link">设置</button>
<%-- 		</shiro:hasPermission> --%>
<%-- 		<shiro:hasPermission name="sc:resourcegantt:area"> --%>
			<button id="area" type="button" class="btn btn-link">车辆保障区域</button>
<%-- 		</shiro:hasPermission> --%>
<%-- 		<shiro:hasPermission name="sc:resourcegantt:truncate"> --%>
			<button id="truncate" type="button" class="btn btn-link">释放车辆</button>
<%-- 		</shiro:hasPermission> --%>
<%-- 		<shiro:hasPermission name="sc:resourcegantt:walkthrough"> --%>
			<button id="walkthrough" type="button" class="btn btn-link">车辆预排</button>
<%-- 		</shiro:hasPermission> --%>
<%--		<shiro:hasPermission name="sc:resourcegantt:allocation"> --%>
			<button id="allocation" type="button" class="btn btn-link">人员分配</button>
<%-- 		</shiro:hasPermission> --%>
		<div class="layui-inline">
			<label class="layui-form-label">车辆预排时间</label>
			<div class="layui-input-inline">
				<input id="dateB" name="dateB" placeholder="航班ETA或ETD时间" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'dateE\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'});" class="layui-input" readonly="readonly"
					value="${start}" type="text">
			</div>
			<label>-</label>
			<div class="layui-input-inline">
				<input id="dateE" name="dateE" placeholder="航班ETA或ETD时间" onclick="WdatePicker({minDate:'#F{$dp.$D(\'dateB\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'});" class="layui-input" readonly="readonly"
					value="${end}" type="text">
			</div>
		</div>
		<form id="switch" class="layui-form switch" action="">
			<input type="checkbox" lay-skin="switch" lay-text="全部|机动"
				lay-filter="switch">
		</form>
	</div>
	<canvas id="SJgantt" class="SJgantt"></canvas>
	<div id="carMenu">
		<button id="reset" type="button" class="btn" onclick="reset($(this))">恢复默认</button>
	</div>
	<script type="text/javascript" src="${ctxStatic}/prss/scheduling/resourceGanttOfKTC.js"></script>
</body>
</html>