<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>性质变更</title>
<script type="text/javascript"
	src="${ctxStatic}/prss/flightdynamic/changeAlnProperty.js"></script>
</head>
<body>
	<form id="form" action="" class="layui-form">
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">航班性质旧值</label>
				<div class="layui-input-inline" >
				<select  disabled="disabled" id="oldValue">
					 <c:if test="${property_code==''}">
					 	<option value=""></option>
					 </c:if>
					 <c:if test="${property_code!=''}">
					<c:forEach items="${alnProperty}" var="pro">
						<option <c:if test="${property_code==pro.value}">selected</c:if> value="${pro.value}">${pro.text}</option>
					</c:forEach>
				    </c:if>
				</select>
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">航班性质新值</label>
				<div class="layui-input-inline" >
				<select name="newPropertyCode" id="newValue" lay-search >
					<option value=""></option>
					<c:forEach items="${alnProperty}" var="pro">
						<option value="${pro.value}">${pro.text}</option>
					</c:forEach>
				</select>
				</div>
			</div>
		</div>
		<input type="hidden" class="layui-input" name="fltid" id="fltid" value="${fltid}" >
	</form>
	<iframe id="frame" name="frame" width="100%" height="200px" frameborder="no"> </iframe>
</body>
</html>