<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>字典管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/bootstrap.jsp" %>
	<%@include file="/WEB-INF/views/include/jquery-validation.jsp"%>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#value").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
	</script>
</head>
<body>
<div class="box box-widget">
	<div class="box-header with-border">
		<h5 class="box-title">
			字典<shiro:hasPermission name="sys:dict:edit">${not empty dict.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:dict:edit">查看</shiro:lacksPermission>
		</h5>
	</div>
	<div class="box-body">
		<form:form id="inputForm" modelAttribute="dict" action="${ctx}/sys/dict/save" method="post" class="form-horizontal">
			<form:hidden path="id"/>
			<sys:message content="${message}"/>
			<div class="layui-form-item">
				<label class="layui-form-label">键值：</label>
				<div class="layui-input-inline">
					<form:input path="value" htmlEscape="false" maxlength="50" class="required layui-input"/>
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">标签：</label>
				<div class="layui-input-inline">
					<form:input path="label" htmlEscape="false" maxlength="50" class="required layui-input"/>
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">类型：</label>
				<div class="layui-input-inline">
					<form:input path="type" htmlEscape="false" maxlength="50" class="required abc layui-input"/>
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">描述：</label>
				<div class="layui-input-inline">
					<form:input path="description" htmlEscape="false" maxlength="50" class="required layui-input"/>
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">排序：</label>
				<div class="layui-input-inline">
					<form:input path="sort" htmlEscape="false" maxlength="11" class="required digits layui-input"/>
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">备注：</label>
				<div class="layui-input-block">
					<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="layui-textarea"/>
				</div>
			</div>
			<div class="layui-form-item text-center">
				<shiro:hasPermission name="sys:dict:edit">
					<button class="layui-btn layui-btn-primary" id="btnSubmit"  type="submit">保存</button>
				</shiro:hasPermission>
				<button id="btnCancel" class="layui-btn layui-btn-primary" type="button" onclick="redirect('${ctx}/sys/dict')">返回</button>
			</div>
		</form:form>
	</div>
</div>
</body>
</html>