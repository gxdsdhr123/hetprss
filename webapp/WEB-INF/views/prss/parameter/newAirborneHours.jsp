<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>新增</title>
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" type="text/css">
<script type="text/javascript"
	src="${ctxStatic}/prss/parameter/newAirborneHours.js"></script>
</head>
<body>
	<form id="form" action="" class="layui-form">
<%-- 		<input type="hidden" name="id" id="id" value="${office.ID }"> --%>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">起飞机场</label>
				<div class="layui-input-inline" >
				 <select name="airport" id="airport" lay-search lay-verify="required">
					<option value=""></option>
					<c:forEach items="${AIRPORT}" var="airport">
						<option value="${airport.id}">${airport.text}</option>
					</c:forEach>
				</select>
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">机型</label> 
				<div class="layui-input-inline" >
				<select name="actType" id="actType" lay-search lay-verify="required">
					<option value="all" selected>通用</option>
					<c:forEach items="${ACT_TYPE}" var="type">
						<option value="${type.id}">${type.text}</option>
					</c:forEach>
				</select>
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">标准航程时间</label>
				<div class="layui-input-inline" >
					<input type="text" id="standardFlightTime" name="standardFlightTime" class="layui-input" lay-verify="required">
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">浮动值±</label>
				<div class="layui-input-inline" >
					<input type="text" id="driftValue" name="driftValue"  class="layui-input" lay-verify="required">
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">开始日期</label>
				<div class="layui-input-inline" >
					<input type="text" id="beginFlightDate" name="beginFlightDate" class="layui-input"  lay-verify="required"
					onfocus="WdatePicker({dateFmt:'yyyyMMdd',startDate:'%y-%M-%d'})">
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">结束日期</label>
				<div class="layui-input-inline" >
					<input type="text" id="endFlightDate" name="endFlightDate" class="layui-input"  lay-verify="required"
					onfocus="WdatePicker({dateFmt:'yyyyMMdd',startDate:'%y-%M-%d'})">
				</div>
			</div>
		</div>
	</form>
	<iframe id="frame" name="frame" width="100%" height="200px"
		frameborder="no"> </iframe>
</body>
</html>