<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${ctxStatic}/modules/sys/maintain/maintainCommon.js"></script>
<script type="text/javascript">
	var currentAirport = "${currentAirport}";//本场机场代码
	var airportCodeSource = ${airportCodeSource};//机场
	var actTypeSource = ${actTypeSource};//机型
	var airlinesCodeSource = ${airlinesCodeSource};//航空公司
	var aircraftNumberSource = ${aircraftNumberSource};//
	var alnDelaySource = ${alnDelaySource};//延误原因
	var releaseDelaySource = ${releaseDelaySource};//放行原因
	var statusSource = ${statusSource};//状态
</script>
</head>
<body>
	<input type="hidden" id="inFltId" value="${inFltId}">
	<input type="hidden" id="outFltId" value="${outFltId}">
	<input type="hidden" id="ifShow" value="${ifShow}">
	<input type="hidden" id="isNew" value="${isNew}">
	<input type="hidden" id="isHis" value="${his}">
	<input type="hidden" id="dataSource" value="${dataSource}">
	<input type="hidden" id="sourceId" value="${sourceId}">
	<form id="baseForm" action="" class="layui-form" lay-filter="baseForm">
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">进港航班：</label>
				<div class="layui-input-inline">
					<input id="aFltNo" name="aFltNo" class="layui-input" type="text"  onkeyup="this.value = this.value.toUpperCase();">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">进港性质：</label>
				<div class="layui-input-inline">
					<select id="aProperty" name="aProperty" lay-filter="aProperty">
					<option value="">请选择</option>
					<c:forEach items="${flightPropertyList}" var="item">
							 <option value="${item.id}">${item.text}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">机位：</label>
				<div class="layui-input-inline">
					<select id="actstandCode" name="actstandCode" >
						<option value="">请选择</option>
						<c:forEach items="${bayList}" var="item">
							<option  value="${item.id}" >${item.text}</option>
						</c:forEach>
					</select>
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">出港航班：</label>
				<div class="layui-input-inline">
					<input id="dFltNo" name="dFltNo" class="layui-input" type="text"  value="${outFltNo}"  
					onkeyup="this.value = this.value.toUpperCase();">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">出港性质：</label>
				<div class="layui-input-inline">
					<select id="dProperty" name="dProperty" lay-filter="dProperty">
					<option value="">请选择</option>
					<c:forEach items="${flightPropertyList}" var="item">
							 <option value="${item.id}">${item.text}</option>
						</c:forEach>
					</select>
				</div>
			</div>
		</div>
	</form>
	<div id="tool-box">
		<button id="actTypeBtn" type="button" class="btn btn-link">添加机型</button>
		<button id="aircraftBtn" type="button" class="btn btn-link">添加机号</button>
		<button id="airportBtn" type="button" class="btn btn-link">添加机场</button>
		<button id="alnBtn" type="button" class="btn btn-link">添加航空公司</button>
	</div>
	<div>
		<table id=formGrid></table>
	</div>
	<script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/flightDynForm.js"></script>
</body>
</html>