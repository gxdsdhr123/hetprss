<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>旅客流程监控</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" type="text/css">
<style type="text/css">
	.layui-layer-page .layui-layer-content{
		overflow: hidden !important;
	}
	.table th, .table td {  
		text-align: center;  
		vertical-align: middle!important;  
	} 
		
</style>
<body>
	<div id="container" style="padding-top:30px">
<!-- 	class="layui-form" -->
		<form  id="listForm" >
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">航班日期</label>
					<div class="layui-input-inline">
						<input id="flightDate" class="layui-input" type="text" 
								onfocus="WdatePicker({dateFmt:'yyyyMMdd',maxDate:'%y-%M-%d',startDate:'%y-%M-{%d-1}'})" value="${FLIGHT_DATE}">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">ETD</label>
					<div class="layui-input-inline" style="width: 100px;">
						<input id="etdStart" class="layui-input" type="text" onfocus="WdatePicker({dateFmt:'HH:mm'})">
					</div>
					<div class="layui-form-mid">-</div>
					<div class="layui-input-inline" style="width: 100px;" >
						<input id="etdEnd" class="layui-input" type="text" onfocus="WdatePicker({dateFmt:'HH:mm'})">
					</div>
				</div>
<!-- 			</div> -->
<!-- 			<div class="layui-form-item"> -->
				<div class="layui-inline">
					<label class="layui-form-label">航班号</label>
					<div class="layui-input-inline">
						<input id="flightNumber" class="layui-input" type="text">
					</div>
				</div>
			
				<div class="layui-inline">
					<label class="layui-form-label">航空公司</label>
					<div class="layui-input-inline">
						<select name="airline" id="airline" class="select2 form-control telegraphType" data-type="telegraphType" multiple="multiple">
								<option value="" ></option>
								<c:forEach items="${AIRLINES}" var="airline">
									<option value="${airline.AIRLINE_CODE}">${airline.DESCRIPTION_CN}</option>
								</c:forEach>
						</select>
					</div>	
				</div>
				
				<div class="layui-inline">
					<label class="layui-form-label">下站机场</label>
					<div class="layui-input-inline">
						<select name="airport" id="airport" class="select2 form-control telegraphType" data-type="telegraphType" multiple="multiple" >
								<option value="" ></option>
								<c:forEach items="${AIRPORTS}" var="airport">
									<option value="${airport.ICAO_CODE}">${airport.DESCRIPTION_CN}</option>
								</c:forEach>
						</select>
					</div>
						
				</div>
				
				<div class="layui-inline  pull-right" id="toolbar">
					<button id="btnSubmit" class="layui-btn layui-btn-small layui-btn-primary search" type="button">
					 	<i class="fa fa-search">&nbsp;</i>查询
					</button>
				</div>
			</div>
		</form>
		<div id="baseTables">
			<table id="baseTable"></table>
		</div>
	</div>
	<script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/passengerMonitorList.js"></script>
</body>
</html>