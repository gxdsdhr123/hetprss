<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>作业类型维护</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/head.jsp"%>
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<body>
	<form:form id="createForm" class="layui-form"
		action="${ctx}/logistic/saveType" enctype="multipart/form-data"
		method="post">
		<input id="kindcode" name="kindcode" type="text" style="display: none"
			value="${kindcode}">
		<input id="mainTypeId" name="mainTypeId" type="text"
			style="display: none" value="${mainTypeId}">
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">保障类型：<font class="required">*</font></label>
				<div class="layui-input-inline">
					<input path="KINDNAME" name="KINDNAME" htmlEscape="false"
						readonly="readonly" value="${KINDNAME}" placeholder="请选择"
						class="layui-input" type="text" />
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">作业类型：<font class="required">*</font></label>
				<div class="layui-input-inline">
					<input path="RESTYPE" name="RESTYPE" htmlEscape="false"
						value="${RESTYPE}" placeholder="请选择" class="layui-input"
						type="text" />
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">作业名称：<font class="required">*</font></label>
				<div class="layui-input-inline">
					<input path="TYPENAME" name="TYPENAME" htmlEscape="false"
						value="${TYPENAME}" placeholder="请选择" class="layui-input"
						type="text" />
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">绑定车辆：<font class="required">*</font></label>
				<label>
                  <input name="bindCar" id="nobind" title="不需要" value="0" ${('0' eq bindCar) || (empty bindCar)?'checked':''}  type="radio">
                </label>
                <label>
                  <input name="bindCar" id="bind" title="需要绑定" value="1" ${'1' eq bindCar?'checked':''} type="radio">
                 	
                </label>
                <label>
                  <input name="bindCar" id="bindAnd" title="需要绑定车辆,且车辆有资质设置" value="2" ${'2' eq bindCar?'checked':''} type="radio">
                </label>
			</div>
		</div>
		<c:if test="${workFlowList.size()>0}">
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">作业流程：</label>
					<div class="layui-input-inline" style="padding-top: 8px;">
						<ul id="wful">
							<c:forEach items="${workFlowList}" var="item">
								<li value="${item.name}">${item.name}</li>
							</c:forEach>
						</ul>
					</div>
				</div>
			</div>
		</c:if>
	</form:form>
	<script>
		layui.use([ 'layer', 'form' ], function() {
			layer = layui.layer;
		})
	</script>
	<script type="text/javascript"
		src="${ctxStatic}/prss/workflow/workForm.js"></script>
</body>
</html>