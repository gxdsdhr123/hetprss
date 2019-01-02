<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>航班报文</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" type="text/css">
<style type="text/css">
	.layui-layer-page .layui-layer-content{
		overflow: hidden !important;
	}
		
</style>
<body>
	<div id="container">
		<form  id="listForm" class="layui-form">
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">接收日期</label>
					<div class="layui-input-inline">
						<input id="acceptDate" class="layui-input" type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d',startDate:'%y-%M-{%d-1}'})">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">接收时间</label>
					<div class="layui-input-inline" >
						<input id="acceptTimeStart" class="layui-input" type="text" onfocus="WdatePicker({dateFmt:'HH:mm'})">
					</div>
					<div class="layui-form-mid">-</div>
					<div class="layui-input-inline" >
						<input id="acceptTimeEnd" class="layui-input" type="text" onfocus="WdatePicker({dateFmt:'HH:mm'})">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">航班号</label>
					<div class="layui-input-inline">
						<input id="flightNumber" class="layui-input" type="text">
					</div>
						
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">报文类型</label>

					<div class="layui-input-inline"> 
<!-- 					class="select2 form-control departType" data-type="departType" multiple="multiple" -->
                       <select name="telType" id="telType" >
								<option value="" ></option>
								<c:forEach items="${TEL_TYPE}" var="type">
									<option value="${type.TEL_CODE}">${type.TEL_CODE}</option>
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
	<script type="text/javascript" src="${ctxStatic}/prss/telegraph/flightTelegraphList.js"></script>
</body>
</html>