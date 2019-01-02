<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>


<!-- end -->
<html>
<head>
<meta name="decorator" content="default" />
<!-- start -->
<%@include file="/WEB-INF/views/include/treeview.jsp"%>
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/fullcalendar/fullcalendar.min.css"/>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<link href="${ctxStatic}/prss/arrange/css/empPlanList.css" rel="stylesheet" />
<style type="text/css">
	.layui-form select {
		display:block;
	}
</style>

</head>
<body>
	<from id="filterDouQKA" class="layui-form" action="" ><!-- style="display: none !important" -->
	<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label"><font color="red">* </font>设备类型</label>
			<div class="layui-input-inline">
				<input type="text" name="typeName" class="layui-input typeName" value="${result.TYPE_NAME}" maxlength="20"/>
			</div>
		</div>
	</div>
		
	<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label"><font color="red">* </font>保障类型</label>
			<div class="layui-input-inline">
			<input type="hidden" value="${result.RESKIND}" class="douxf"/>
				<select name="reskind" class="form-control reskind">
					<option value="">请选择</option>
					<c:forEach var="data" items="${data}">
						<option value="${data.RESKIND}">${data.KINDNAME}</option>
					</c:forEach>
				</select>
			</div>
		</div>
	
	</div>
	
</from>
<script type="text/javascript" src="${ctxStatic}/prss/equipment/equipmentZtreeRevamp.js"></script>
</body>
</html>
