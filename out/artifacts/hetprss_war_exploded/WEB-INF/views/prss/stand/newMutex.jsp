<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>机位互斥设置</title>
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${ctxStatic}/prss/stand/newMutex.js"></script>
</head>
<body>
	<form id="aptitudeForm" action="" class="layui-form">
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">机位1</label>
				<div class="layui-input-inline">
					<select name="ACTSTAND_CODE1" id="ACTSTAND_CODE1">
						<option value=""></option>
						<c:forEach items="${ACTSTAND}" var="actStand">
							<option value="${actStand.bay_code}">${actStand.description_cn}</option>
						</c:forEach>
					</select>
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">机型1</label>
				<div class="layui-input-inline">
					<select name="AIRCRAFT_TYPE1" id="AIRCRAFT_TYPE1">
						<option value="*ALL">*ALL</option>
						<c:forEach items="${AIRCRAFT}" var="airCraft">
							<option value="${airCraft.todb_actype_code}">${airCraft.todb_actype_code}</option>
						</c:forEach>
					</select>
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">机位2</label>
				<div class="layui-input-inline">
					<select name="ACTSTAND_CODE2" id="ACTSTAND_CODE2" >
						<option value=""></option>
						<c:forEach items="${ACTSTAND}" var="actStand">
							<option value="${actStand.bay_code}">${actStand.description_cn}</option>
						</c:forEach>
					</select>
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">机型2</label>
				<div class="layui-input-inline">
					<select name="AIRCRAFT_TYPE2" id="AIRCRAFT_TYPE2">
						<option value="*ALL">*ALL</option>
						<c:forEach items="${AIRCRAFT}" var="airCraft">
							<option value="${airCraft.todb_actype_code}">${airCraft.todb_actype_code}</option>
						</c:forEach>
					</select>
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<div class="layui-form-mid">&nbsp;&nbsp;&nbsp;&nbsp;占用时间(分钟)</div>
				<div class="layui-input-inline">
					<input type="text" class="layui-input" name="TIME_MIN" id="TIME_MIN" value="0" >
				</div>
			</div>
		</div>
		
	</form>
	<iframe id="frame" name="frame" width="100%" height="200px"
		frameborder="no"> </iframe>
</body>
</html>