<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>员工某时段内工作量</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript"
	src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css"
	rel="stylesheet" type="text/css">
<body>
	<div id="container" style="padding-top: 30px">
		<form id="listForm" action="${ctx}/timeslotworkload/statistics/exportExcel"	method="post">
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">开始日期</label>
					<div class="layui-input-inline" >
						<input id="dateStart" name="dateStart" class="layui-input" type="text" value="${defaultStart}"
						 onfocus="WdatePicker({dateFmt:'yyyyMMdd',maxDate:'#F{$dp.$D(\'dateEnd\')}'})">
					</div>
					<div class="layui-form-mid">结束日期</div>
					<div class="layui-input-inline" >
						<input id="dateEnd" name="dateEnd" class="layui-input" type="text" value="${defaultEnd}"
						 onfocus="WdatePicker({dateFmt:'yyyyMMdd',minDate:'#F{$dp.$D(\'dateStart\')}'})">
					</div>
					<div class="layui-form-mid">员工姓名</div>
					<div class="layui-input-inline" >
						<input type="text" name="name" id="name" class="layui-input"   value=""/>
					</div>
					<div class="layui-form-mid">作业类型</div>
					<div class="layui-input-inline">
					    <input name="jobKind" type="hidden"/>
						<select name="jobType" id="jobType" class="form-control">
							<option value="">请选择</option>
							<c:forEach items="${jobTypeList}" var="item">
								<option value="${item.code}">${item.name}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="layui-inline" id="toolbar">
					<button id="btnSubmit"
						class="layui-btn layui-btn-small layui-btn-primary search"
						type="button">
						<i class="fa fa-search">&nbsp;</i>查询
					</button>
					<button id="btnChart"
						class="layui-btn layui-btn-small layui-btn-primary print"
						type="button">打印</button>
				</div>
			</div>
		</form>

		<div id="baseTables" style="width: 100%;">
			<table id="baseTable"></table>
		</div>
	</div>
	<script type="text/javascript"
		src="${ctxStatic}/prss/statisticalanalysis/timeSlotWorkloadList.js"></script>
</body>
</html>