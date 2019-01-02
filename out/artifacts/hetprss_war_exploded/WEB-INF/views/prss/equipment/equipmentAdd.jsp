<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<form id="filterDou456" class="layui-form" action="" style="display: none !important">
	<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label"><font color="red">* </font>设备类型</label>
			<div class="layui-input-inline">
				<input type="text" name="typeName" class="layui-input typeName" readonly="readonly"/>
			</div>
		</div>
	</div>
	
	<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label"><font color="red">* </font>设备编号</label>
			<div class="layui-input-inline">
				<input type="text" name="deviceNo" class="layui-input deviceNo" maxlength="50"/>
			</div>
		</div>
	</div>
	
	<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label" ><font color="red">* </font>设备型号</label>
			<div class="layui-input-inline">
				<input type="text" name="deviceModel" class="layui-input deviceModel" maxlength="50"/>
			</div>
		</div>
	</div>
	
	
	<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label" ><font color="red">* </font>车牌号</label>
			<div class="layui-input-inline">
				<input type="text" name="carNumber" class="layui-input carNumber" maxlength="50"/>
			</div>
		</div>
	</div>				
	<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label" ><font color="red">* </font>状态</label>
			<div class="layui-input-inline">
				<select name="deviceStatus" id="douxf" class="deviceStatus">
					<option value="">请选择</option>
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
				<input type="text" name="innerNumber" class="layui-input innerNumber" maxlength="50"/>
			</div>
		</div>
	</div>
	
	<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label">核载人数</label>
			<div class="layui-input-inline">
				<input type="text" name="seatingNum" class="layui-input seatingNum" maxlength="3" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^0-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"/>
			</div>
		</div>
	</div>
	
	<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label">备注</label>
			<div class="layui-input-inline">
				<input type="text" name="remark" class="layui-input remark" maxlength="200"/>
			</div>
		</div>
	</div>
</form>