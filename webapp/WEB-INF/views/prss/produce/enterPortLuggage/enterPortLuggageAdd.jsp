<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>进港行李交接单</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/treeview.jsp"%>
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/prss/plan/css/longTermPlanForm.css" rel="stylesheet" />
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${ctxStatic}/modules/sys/maintain/maintainCommon.js"></script>
<style type="text/css">

#flightDate {
    padding: 2px 0px 3px 8px;
}

.layui-form-item .layui-input-inline {
    margin-right: 10%;
}

.layui-select-title {
		display:none;
}
</style>
</head>
<body>
	
	<button id="refreshDetailTable" type="button" style="display: none"></button>
	<form id="createForm" class="layui-form">
		<div >
			
			<div class="layui-form-item" style="display: flex;"><!--  -->
				<input type="hidden" value="${newData }" id="newData"/>
				<label class="layui-form-label"><font color="red">* </font>日期</label>
				<div class="layui-input-inline">
					<input  name="flightDate" placeholder="请输入" id="flightDate" class="layui-input " type="text" onClick="WdatePicker()"/>
				</div>
<!-- 			</div> -->

<!-- 			<div class="layui-inline"> -->
				<label class="layui-form-label"><font color="red">* </font>航班号</label>
				<div class="layui-input-inline">
					<input  name="flightNumber" placeholder="请输入"class="layui-input flightNumber" type="text" id="douxf" />
				</div>
<!-- 			</div> -->
			
<!-- 			<div class="layui-inline"> -->
				<label class="layui-form-label"><font color="red"></font>机号</label>
				<div class="layui-input-inline">
					<input  name="aircraftNumber" placeholder="请输入"  class="layui-input aircraftNumber" type="text" readonly="readonly"/>
				</div>
			</div>
			
			<div class="layui-form-item" style="display: flex;">
				<label class="layui-form-label"><font color="red"></font>机位</label>
				<div class="layui-input-inline">
					<input  name="actstandCode" placeholder="请输入" class="layui-input actstandCode" type="text" readonly="readonly"/>
																				    
				</div>
<!-- 			</div> -->
			
<!-- 			<div class="layui-inline"> -->
				<label class="layui-form-label"><font color="red"></font>ATA</label>
				<div class="layui-input-inline">
					<input  name="ata" placeholder="请输入"class="layui-input ata"  type="text" readonly="readonly"/>
				</div>
<!-- 			</div> -->
			
			
<!-- 			<div class="layui-inline"> -->
				<label class="layui-form-label"><font color="red">* </font>查理</label>
				<div class="layui-input-inline">
					<select name="chali"  id="douxuefeng" class="select2 form-control chali">
						<option value="">请选择</option>
						<option value="">请选择</option>
						<c:forEach var="data" items="${data}">
							<option value="${data.ID}">${data.NAME}</option>
						</c:forEach>
					</select>
					
				</div>
			</div>
			
		</div>
	</form>
	
	<div id="toolbar" style="padding-right:10px">
		<button id="nextRow" type="button" class="layui-btn layui-btn-normal layui-btn-small pull-right">增加</button>
	</div>
	<div id="createDetailTables">
		<table id="createDetailTable"></table>
	</div>
	<script type="text/javascript" src="${ctxStatic}/prss/produce/enterPortLuggage/enterPortLuggageAdd.js"></script>
</body>
</html>