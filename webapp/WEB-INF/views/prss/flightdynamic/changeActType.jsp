<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>机型变更</title>
<script type="text/javascript"
	src="${ctxStatic}/prss/flightdynamic/changeActType.js"></script>
</head>
<body>
	<form id="form" action="" class="layui-form">
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">机型旧值</label>
				<div class="layui-input-inline" >
				<input type="text" class="layui-input" name="oldValue" id="oldValue" value="${old_acttype_code}" readonly>
				</div>
			</div>
		</div>
		<c:if test="${refresh=='Y'}">
			<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">机型新值</label>
				<div class="layui-input-inline" >
				<input type="text" class="layui-input" name="newValue" id="newValue" value="${refreshActNum}" readonly>
				</div>
			</div>
			</div>
		</c:if>
		<c:if test="${refresh=='N'}">
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">机型新值</label>
					<div class="layui-input-inline" >
					<select name="newValue" id="newValue" lay-search >
						<option value=""></option>
						<c:forEach items="${actType}" var="type">
							<option value="${type.todb_actype_code}">${type.todb_actype_code}</option>
						</c:forEach>
					</select>
					</div>
				</div>
			</div>
		</c:if>
		<input type="hidden" class="layui-input" name="refresh" id="refresh" value="${refresh}" >
		<input type="hidden" class="layui-input" name="actstand_code" id="actstand_code" value="${actstand_code}" >
		<input type="hidden" class="layui-input" name="in_fltid" id="in_fltid" value="${in_fltid}" >
		<input type="hidden" class="layui-input" name="out_fltid" id="out_fltid" value="${out_fltid}" >
	</form>
	<iframe id="frame" name="frame" width="100%" height="200px" frameborder="no"> </iframe>
</body>
</html>