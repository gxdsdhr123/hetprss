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
<body class="sortable">
	<from id="filterDou789" class="layui-form" action="" ><!-- style="display: none !important" -->
	<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label"><font color="red">* </font>设备类型</label>
			<div class="layui-input-inline">
				<input type="text" name="typeName" value="${device.TYPE_NAME}" class="layui-input typeName" id="typeName" readonly="readonly"/>
			</div>
		</div>
	</div>
	
	<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label"><font color="red">* </font>设备编号</label>
			<div class="layui-input-inline">
				<input type="text" name="deviceNo" value="${device.DEVICE_NO}" class="layui-input deviceNo" maxlength="50"/>
			</div>
		</div>
	</div>
	
	<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label"><font color="red">* </font>设备型号</label>
			<div class="layui-input-inline">
				<input type="text" name="deviceModel" value="${device.DEVICE_MODEL}"  class="layui-input deviceModel" maxlength="50"/>
			</div>
		</div>
	</div>
	
	
	<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label"><font color="red">* </font>车牌号</label>
			<div class="layui-input-inline">
				<input type="text" name="carNumber" value="${device.CAR_NUMBER}" class="layui-input carNumber" maxlength="50"/>
			</div>
		</div>
	</div>
	

	<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label"><font color="red">* </font>状态</label>
			<div class="layui-input-inline">
			<input type="hidden" value="${device.DEVICE_STATUS}" id="deviceId"/>
				<select name="deviceStatus" class="form-control deviceStatus">
					<option value="">请选择</option>
					<option value="1">可用</option>
					<option value="2">停用</option>
					<option value="3">维修中</option>
				</select>
			</div>
		</div>
	
	</div>
	
	<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label">自编号</label>
			<div class="layui-input-inline">
				<input type="text" name="innerNumber" value="${device.INNER_NUMBER}" class="layui-input innerNumber" maxlength="50"/>
			</div>
		</div>
	</div>
	
	<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label">核载人数</label>
			<div class="layui-input-inline">
				<input type="text" name="seatingNum" value="${device.SEATING_NUM}" class="layui-input seatingNum" maxlength="3" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^0-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"/>
			</div>
		</div>
	</div>
	
	<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label">备注</label>
			<div class="layui-input-inline">
				<input type="text" name="remark" value="${device.REMARK}" class="layui-input remark" maxlength="200"/>
			</div>
		</div>
	</div>
</from>
<script type="text/javascript" src="${ctxStatic}/prss/equipment/equipmentRevamp.js"></script>
</body>
</html>
