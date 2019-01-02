<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>属性变更</title>
<script type="text/javascript"
	src="${ctxStatic}/prss/flightdynamic/changeAttrCode.js"></script>
</head>
<body>
	<form id="form" action="" class="layui-form">
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">航班属性旧值</label>
				<div class="layui-input-inline" >
				<select disabled="disabled" id="oldValue">
						<option <c:if test="${oldValue==''}">selected</c:if> value="${oldValue}"></option>
						<option <c:if test="${oldValue=='D'}">selected</c:if> value="${oldValue}">国内</option>
						<option <c:if test="${oldValue=='I'}">selected</c:if> value="${oldValue}">国际</option>
						<option <c:if test="${oldValue=='M'}">selected</c:if> value="${oldValue}">混合</option>
				</select>
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">航班属性新值</label>
				<div class="layui-input-inline" >
				<select name="newValue" id="newValue" lay-search >
					<option value=""></option>
						<option value="D">国内</option>
						<option value="I">国际</option>
						<option value="M">混合</option>
				</select>
				</div>
			</div>
		</div>
		<input type="hidden" class="layui-input" name="fltid" id="fltid" value="${fltid}" >

	</form>
	<iframe id="frame" name="frame" width="100%" height="200px" frameborder="no"> </iframe>
</body>
</html>