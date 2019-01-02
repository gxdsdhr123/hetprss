<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>机号变更</title>
<script type="text/javascript"
	src="${ctxStatic}/prss/flightdynamic/changeActNumber.js"></script>
</head>
<body>
	<form id="form" action="" class="layui-form">
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">机号旧值</label>
				<div class="layui-input-inline" >
					<input type="text" id="oldValue" name="oldValue" class="layui-input" 
					value="${old_aircraft_number}" readonly="readonly">
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">机号新值</label>
				<div class="layui-input-inline" >
				<select name="newValue" id="newValue" lay-search >
					<option value=""></option>
					<c:forEach items="${actNumber}" var="number">
						<option value="${number.acreg_code}">${number.acreg_code}</option>
					</c:forEach>
				</select>
				</div>
			</div>
		</div>
		<input type="hidden" class="layui-input" name="in_fltid" id="in_fltid" value="${in_fltid}" >
		<input type="hidden" class="layui-input" name="out_fltid" id="out_fltid" value="${out_fltid}">
		<input type="hidden" class="layui-input" name="actstand_code" id="actstand_code" value="${actstand_code}">
		<input type="hidden" class="layui-input" name="acttype_code" id="acttype_code" value="${acttype_code}">
	</form>
	<iframe id="frame" name="frame" width="100%" height="200px"
		frameborder="no"> </iframe>
</body>
</html>