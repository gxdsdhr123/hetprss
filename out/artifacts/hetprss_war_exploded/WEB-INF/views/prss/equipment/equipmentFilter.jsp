<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<form id="filterDou123" class="layui-form" action="" 
	style="display: none !important"><!-- style="display: none !important" -->
	<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label">设备编号</label>
			<div class="layui-input-inline">
				<select name="deviceNo" class="deviceNo">
					<option value="">请选择</option>
					<option value="">请选择</option>
					<c:forEach var="result" items="${result}">
						<option value="${result.DEVICE_NO}">${result.DEVICE_NO}</option>
					</c:forEach>
				</select>
			</div>
		</div>
	</div>
		
	<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label">设备型号</label>
			<div class="layui-input-inline">
				<select name="deviceModel" class="deviceModel">
					<option value="">请选择</option>
					<option value="">请选择</option>
					<c:forEach var="resultI" items="${resultI}">
						<option value="${resultI.DEVICE_MODEL}">${resultI.DEVICE_MODEL}</option>
					</c:forEach>
				</select>
			</div>
		</div>
	</div>
	<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label">车牌号</label>
			<div class="layui-input-inline">
				<select name="carNumber" class="carNumber">
					<option value="">请选择</option>
					<option value="">请选择</option>
					<c:forEach var="resultII" items="${resultII}">
						<option value="${resultII.CAR_NUMBER}">${resultII.CAR_NUMBER}</option>
					</c:forEach>
				</select>
			</div>
		</div>
	
	</div>
	
		
		
		
		
		
		
		
		
		
		
		
</form>