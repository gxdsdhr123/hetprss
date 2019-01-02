<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>备降外场变更</title>
<script type="text/javascript"
	src="${ctxStatic}/prss/flightdynamic/changeDiversion.js"></script>
	<script type="text/javascript"
	src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
</head>
<body>
<!-- class="layui-form" -->
	<form id="form" action="" class="layui-form">
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">航班号</label>
				<div class="layui-input-inline" >
					<input type="text" id="fltNumber" name="fltNumber" class="layui-input" 
					value="${FLIGHT_NUMBER}" readonly="readonly">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">航班日期</label>
				<div class="layui-input-inline" >
					<input type="text" id="fltDate" name="fltDate" class="layui-input" 
					value="${FLIGHT_DATE}" readonly="readonly">
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">机号</label>
				<div class="layui-input-inline" >
					<input type="text" id="actNumber" name="actNumber" class="layui-input" 
					value="${AIRCRAFT_NUMBER}" readonly="readonly">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">机型</label>
				<div class="layui-input-inline" >
					<input type="text" id="actType" name="actType" class="layui-input" 
					value="${ACTTYPE_CODE}" readonly="readonly">
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">备降场</label>
				<div class="layui-input-inline" >
				<select name="diversionPort" id="diversionPort" class="select2 form-control telegraphType" data-type="telegraphType" lay-search>
								<option value="" ></option>
								<c:forEach items="${airportCodeSource}" var="airport">
									<option value="${airport.id}" <c:if test="${airport.id==DIVERSION_PORT}">selected</c:if>>${airport.text}</option>
								</c:forEach>
				</select>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">备降原因</label>
				<div class="layui-input-inline" >
				<select name="diversionRes" id="diversionRes" lay-search >
					<option value=""></option>
					<c:forEach items="${diversionSource}" var="reason">
						<option value="${reason.id}" <c:if test="${reason.id==DIVERSION_RES}">selected</c:if>>${reason.text}</option>
					</c:forEach>
				</select>
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">备降详情</label>
			<div class="layui-input-block">
				<textarea id="diversionResDetail" name="diversionResDetail"
					class="layui-textarea" >${DIVERSION_RES_DETAIL}</textarea>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">备降实起</label>
				<div class="layui-input-inline" >
					<input type="text" id="diversionATD" name="diversionATD" class="layui-input" 
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});"
					value="${DIVERSION_ATD}" >
				</div>
			</div>
		</div>
		<input type="hidden" class="layui-input" name="fltid" id="fltid" value="${fltid}" >
		<input type="hidden" class="layui-input" name="diversionPortOld" id="diversionPortOld" value="${DIVERSION_PORT}" >
		<input type="hidden" class="layui-input" name="diversionResOld" id="diversionResOld" value="${DIVERSION_RES}" >
		<input type="hidden" class="layui-input" name="diversionATDOld" id="diversionATDOld" value="${DIVERSION_ATD}" >
	</form>
</body>
</html>