<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>计划筛选</title>
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
					<label class="layui-form-label">日期</label>
					<div class="layui-input-inline" style="width: 100px;">
						<input id="planDateStart" class="layui-input" type="text" 
						 onfocus="WdatePicker({dateFmt:'yyyyMMdd',startDate:'%y-%M-{%d-1}'})" value="${FLIGHT_DATE}">
					</div>
					<div class="layui-form-mid">-</div>
					<div class="layui-input-inline" style="width: 100px;" >
						<input id="planDateEnd" class="layui-input" type="text" 
						 onfocus="WdatePicker({dateFmt:'yyyyMMdd',startDate:'%y-%M-{%d-1}'})" value="${FLIGHT_DATE}">
					</div>
				</div>
				
				<div class="layui-inline">
					<label class="layui-form-label">时间</label>
					<div class="layui-input-inline" style="width: 100px;">
						<input id="planTimeStart" class="layui-input" type="text" onfocus="WdatePicker({dateFmt:'HHmm'})" value="${planTimeStart}">
					</div>
					<div class="layui-form-mid">-</div>
					<div class="layui-input-inline" style="width: 100px;" >
						<input id="planTimeEnd" class="layui-input" type="text" onfocus="WdatePicker({dateFmt:'HHmm'})"value="${planTimeEnd}">
					</div>
				</div>

				<div class="layui-inline">
						<label class="layui-form-label">机场</label>
						<div class="layui-input-inline">
							<select name="airport" id="airport" class="select2 form-control telegraphType" data-type="telegraphType" multiple="multiple" >
									<option value="" ></option>
									<c:forEach items="${AIRPORTS}" var="airport">
										<option value="${airport.ICAO_CODE}">${airport.DESCRIPTION_CN}</option>
									</c:forEach>
							</select>
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
				
				
				
				<div class="layui-inline  pull-right" id="toolbar">
					<button id="btnSubmit" class="layui-btn layui-btn-small layui-btn-primary search" type="button">
					 	<i class="fa fa-search">&nbsp;</i>筛选
					</button>
					
					<button id="btnChart" class="layui-btn layui-btn-small layui-btn-primary" type="button">
					 	<i>&nbsp;</i>分析
					</button>
				</div>
			</div>
		</form>
		

		<div id="baseTables" style="width:100%;">
			<div style="width:49%;display:inline-block;float:left;margin-right:2px;">
				<table id="baseTableLeft"></table>
			</div>
			<div style="width:50%;display:inline-block;float:left;">
				<table id="baseTableRight"></table>
			</div>
		</div>
		<div style="clear:both;"></div>
	</div>
	<script type="text/javascript" src="${ctxStatic}/prss/plan/planFilterList.js"></script>
</body>
</html>