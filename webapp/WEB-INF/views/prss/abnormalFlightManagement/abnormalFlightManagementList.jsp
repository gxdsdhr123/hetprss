<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>不正常航班管理</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<style type="text/css">
#cellDetailInfo {
    width: 23%;
    position: absolute;
    z-index: 9;
    background: rgba(0, 0, 0, 0.35);
    margin-left: 77%;
}
</style>
<body>
	<div id="tool-box" style="padding-top: 5px;">
		<div style="float: left;padding-left:15px" id="toolbar">
			<shiro:hasPermission name="abnormal:flight:search">
				<button id="screenAbnormalBtn" type="button" class="btn btn-link">筛选</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="abnormal:flight:add">
				<button id="addabnormalFlightBtn" type="button" class="btn btn-link">新增</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="abnormal:flight:show">
				<button id="showAbnormalFlightBtn" type="button" class="btn btn-link">查看</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="abnormal:flight:del">
				<button id="delAbnormalFlightBtn" type="button" class="btn btn-link">删除</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="abnormal:flight:rep">
				<button id="surveyFeedBackReportBtn" type="button" class="btn btn-link">反馈调查报告</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="abnormal:flight:cdm">
				<button id="CDMContractorResponsibleBtn" type="button" class="btn btn-link">CDM判责</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="abnormal:flight:print">
				<button id="printSelectedFlightBtn" type="button" class="btn btn-link">打印</button>
			</shiro:hasPermission>
		</div>
	</div>
	<div id="createDetailTableDiv" style="width:100%;position: relative;" onselectstart="return false" style="-moz-user-select:none;">
		<table id="createDetailTable" style="table-layout: fixed;"></table>
	</div>
	
	<form id="printForm" action="${ctx}/abnormal/abnormalFlightManagement/print" method="post" style="display: none">
		<textarea id="printTitle" name="title"></textarea>
		<textarea id="printData" name="data"></textarea>
		<input type="hidden" name="searchTime"  value="${searchTime }"/>
	</form>
	
	
	
<script type="text/javascript" src="${ctxStatic}/prss/abnormalFlightManagement/abnormalFlightManagementList.js"></script>
</body>
</html>