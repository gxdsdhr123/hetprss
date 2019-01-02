<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/prss/plan/css/longTermPlanForm.css" rel="stylesheet" />
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${ctxStatic}/modules/sys/maintain/maintainCommon.js"></script>
<script type="text/javascript">
window.detailList = ${detailList};
</script>
<style type="text/css">

#createForm input {
    height: 31px !important;
    width: 137px !important;
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
<form id="createForm" class="layui-form" enctype="multipart/form-data">
	<div>
		<div class="layui-form-item" style="display: flex;">
				<label class="layui-form-label"><font color="red">* </font>日期</label>
				<div class="layui-input-inline">
					<input type="hidden" value="${mianData.ID}" class="mainId"/>
					
					<input  name="flightDate" placeholder="请输入" value="${mianData.FLIGHT_DATE}" class="layui-input flightDate" type="text"  readonly="readonly"/>
				</div>
			<!-- </div>

			<div class="layui-inline"> -->
				<label class="layui-form-label"><font color="red">* </font>航班号</label>
				<div class="layui-input-inline">
					<input  name="flightNumber" placeholder="请输入" value="${mianData.FLIGHT_NUMBER}" class="layui-input flightNumber" type="text" id="douxf" readonly="readonly"/>
				</div>
			<!-- </div>
			
			<div class="layui-inline"> -->
				<label class="layui-form-label"><font color="red"></font>机号</label>
				<div class="layui-input-inline">
					<input  name="aircraftNumber" placeholder="请输入" value="${mianData.AIRCRAFT_NUMBER }" class="layui-input aircraftNumber" type="text" readonly="readonly"/>
				</div>
			</div>
			
			<div class="layui-form-item" style="display: flex;">
				<label class="layui-form-label"><font color="red"> </font>机位</label>
				<div class="layui-input-inline">
					<input  name="actstandCode" placeholder="请输入" value="${mianData.ACTSTAND_CODE }" class="layui-input actstandCode" type="text" readonly="readonly"/>
																				    
				</div>
		<!-- 	</div>
			
			<div class="layui-inline"> -->
				<label class="layui-form-label"><font color="red">	 </font>ATA</label>
				<div class="layui-input-inline">
					<input  name="ata" placeholder="请输入" value="${mianData.ATA }" class="layui-input ata"  type="text" readonly="readonly"/>
				</div>
			<!-- </div>
			
			
			<div class="layui-inline"> -->
				<label class="layui-form-label"><font color="red">* </font>查理</label>
				<div class="layui-input-inline">
				<input type="hidden" value="${mianData.OPERATOR }" class="gufl"/>
					<select name="chali" class="select2 form-control chali" id="douxuefeng">
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
	<script type="text/javascript" src="${ctxStatic}/prss/produce/enterPortLuggage/enterPortLuggageRevamp.js"></script>
</body>
</html>