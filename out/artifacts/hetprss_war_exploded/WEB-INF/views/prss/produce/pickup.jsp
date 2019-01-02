<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>失物招领单</title>
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript" src="${ctxStatic}/prss/produce/pickup.js"></script>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
</head>
<body>
	<form id="pickupForm" class="layui-form" action="">
		<input type="hidden" name="id" id="id" value="${pickupJson.id }">
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">航班号</label>
				<div class="layui-input-inline">
					<c:choose>
						<c:when test="${pickupJson.id != null }">
							<label class="layui-form-label">${pickupJson.flightNumber }</label>
						</c:when>
						<c:otherwise>
							<input type="text" autocomplete="off" class="layui-input"
								name="flightNumber" id="flightNumber" value="">
						</c:otherwise>
					</c:choose>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">航班日期</label>
				<div class="layui-input-inline">
					<c:choose>
						<c:when test="${pickupJson.id != null }">
							<label class="layui-form-label">${pickupJson.flightDate }</label>
						</c:when>
						<c:otherwise>
							<input type="text" autocomplete="off" class="layui-input" name="flightDate" id="flightDate" 
								onClick="WdatePicker({dateFmt:'yyyyMMdd'})" value="" readonly="readonly">
						</c:otherwise>
					</c:choose>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">捡拾人单位</label>
				<div class="layui-input-inline">
					<label class="layui-form-label">地服公司</label>
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">兹收到姓名</label>
				<div class="layui-input-inline">
					<c:choose>
						<c:when test="${pickupJson.id != null }">
							<label class="layui-form-label">${pickupJson.operatorName }</label>
						</c:when>
						<c:otherwise>
							<select name="operator" id="operator" >
								<option value="">请选择</option>
								<c:forEach items="${userList }" var="user">
									<option value="${user.ID }">${user.NAME }</option>
								</c:forEach>
							</select>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">捡拾人</label>
				<div class="layui-input-inline">
					<c:choose>
						<c:when test="${pickupJson.id != null }">
							<label class="layui-form-label">${pickupJson.puUserName }</label>
						</c:when>
						<c:otherwise>
							<select name="puUser" id="puUser" lay-filter="puUser">
								<option value="">请选择</option>
								<c:forEach items="${userList }" var="user">
									<option value="${user.ID }" data-phone="${user.MOBILE }">${user.NAME }</option>
								</c:forEach>
							</select>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">捡拾人电话</label>
				<div class="layui-input-inline">
					<c:choose>
						<c:when test="${pickupJson.id != null }">
							<label class="layui-form-label">${pickupJson.puPhone }</label>
						</c:when>
						<c:otherwise>
							<input type="text" autocomplete="off" class="layui-input"
								name="puPhone" id="puPhone" value="">
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
		<div id="toolbar">
			<button id="addGoodsBtn" type="button" class="btn btn-link">新增</button>
		</div>
		<table id="pickupGoodsTable"></table>
	</form>
</body>
</html>