<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<form id="filterDouJQK" class="layui-form" action="" style="display: none !important">
	<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label"><font color="red">* </font>设备类型</label>
			<div class="layui-input-inline">
				<input type="text" name="typeName" class="layui-input typeName" id="gufanglong123" maxlength="20"/>
			</div>
		</div>
	</div>
	
	<div class="layui-form-item">
		<div class="layui-inline">
			<label class="layui-form-label"><font color="red">* </font>保障类型</label>
			<div class="layui-input-inline">
				<select name="reskind" class="reskind" id="gufanglong456">
					<option value="">请选择</option>
					<option value="">请选择</option>
					<c:forEach var="data" items="${data}">
						<option value="${data.RESKIND}">${data.KINDNAME}</option>
					</c:forEach>
				</select>
			</div>
		</div>
	</div>
</form>