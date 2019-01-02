<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>保障类型维护</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/head.jsp"%>
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<body>
	<form id="createForm" class="layui-form"
		action="${ctx}/logistic/saveKind" enctype="multipart/form-data"
		method="post">
		<input path="mainKindId" id="mainKindId" name="mainKindId"
			style="display: none;" value="${mainKindId}" type="text" />
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">保障类型：<font class="required">*</font></label>
				<div class="layui-input-inline">
					<input path="RESKIND" name="RESKIND" htmlEscape="false"
						value="${RESKIND}" placeholder="请输入" class="layui-input"
						type="text" />
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">保障名称：<font class="required">*</font></label>
				<div class="layui-input-inline">
					<input path="KINDNAME" name="KINDNAME" htmlEscape="false"
						value="${KINDNAME}" placeholder="请输入" class="layui-input"
						type="text" />
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">所属部门：<font class="required">*</font></label>
				<div class="layui-input-inline">
					<select path="DEPNAME" name="DEPNAME" items="${dtList}"
						value="${DEPNAME}" class="layui-input">
						<c:forEach items="${depList}" var="item">
							<c:if test="${not empty DEPID and item.ID == DEPID}">
								<option value="${item.ID},${item.NAME}" selected="selected">${item.NAME}</option>
							</c:if>
							<c:if test="${item.ID!=DEPID}">
								<option value="${item.ID},${item.NAME}">${item.NAME}</option>
							</c:if>
						</c:forEach>
					</select>
				</div>
			</div>
		</div>
		<input type="hidden" name="TAB" value="${TAB}"/>
	</form>
	<script type="text/javascript"
		src="${ctxStatic}/prss/workflow/logisticForm.js"></script>
</body>
</html>