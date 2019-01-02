<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>编辑</title>
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" type="text/css">
<script type="text/javascript"
	src="${ctxStatic}/prss/parameter/editAirborneHours.js"></script>
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
					<option value=""></option>
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
					<input type="text" id="standardFlightTime" name="standardFlightTime" class="layui-input" 
					value="${ROW.STANDARD_FLT_TIME}" lay-verify="required">
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">浮动值±</label>
				<div class="layui-input-inline" >
					<input type="text" id="driftValue" name="driftValue"  class="layui-input" 
					value="${ROW.DRIFT_VALUE}" lay-verify="required">
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">开始日期</label>
				<div class="layui-input-inline" >
					<input type="text" id="beginFlightDate" name="beginFlightDate" class="layui-input"  lay-verify="required"
					onfocus="WdatePicker({dateFmt:'yyyyMMdd',startDate:'%y-%M-%d'})" value="${ROW.BEGIN_FLT_DATE}">
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">结束日期</label>
				<div class="layui-input-inline" >
					<input type="text" id="endFlightDate" name="endFlightDate" class="layui-input"  lay-verify="required"
					onfocus="WdatePicker({dateFmt:'yyyyMMdd',startDate:'%y-%M-%d'})" value="${ROW.END_FLT_DATE}">
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">计算航程时间</label>
				<div class="layui-input-inline" >
					<input type="text" id="calcFltTime" name="calcFltTime"  class="layui-input" 
					value="${ROW.CALC_FLT_TIME}" lay-verify="required">
				</div>
			</div>
		</div>
		<input type="hidden" class="layui-input" name="id" id="id" value="${ROW.ID}" >
		<input type="hidden" class="layui-input" name="airportValue" id="airportValue" value="${ROW.DEPART_APT3CODE}" >
		<input type="hidden" class="layui-input" name="actTypeValue" id="actTypeValue" value="${ROW.ACTTYPE_CODE}" >
	</form>
	<iframe id="frame" name="frame" width="100%" height="200px"
		frameborder="no"> </iframe>
</body>
</html>