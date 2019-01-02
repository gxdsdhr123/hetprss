<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>区域管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
	<script type="text/javascript">
		layui.use(["form"],function(){
			layui.form;
		});
	</script>
</head>
<body>
	<div class="box box-widget">
		<div class="box-header with-border">
			<h5 class="box-title">
				区域<shiro:hasPermission name="sys:area:edit">${not empty area.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:area:edit">查看</shiro:lacksPermission>
			</h5>
			</div>
		<div class="box-body">
			<form:form id="inputForm" modelAttribute="area" action="${ctx}/sys/area/save" method="post" class="layui-form">
				<form:hidden path="id"/>
				<sys:message content="${message}"/>
				<div class="layui-form-item">
					<label class="layui-form-label">上级区域:</label>
					<div class="layui-input-inline">
						<sys:treeselect id="area" name="parent.id" value="${area.parent.id}" labelName="parent.name" labelValue="${area.parent.name}"
							title="区域" url="/sys/area/treeData" extId="${area.id}" cssClass="layui-input" allowClear="true"/>
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label">区域名称:</label>
					<div class="layui-input-inline">
						<form:input path="name" htmlEscape="false" maxlength="50" class="layui-input" lay-verify="required"/>
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label">区域编码:</label>
					<div class="layui-input-inline">
						<form:input path="code" htmlEscape="false" maxlength="50" class="layui-input"/>
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label">区域类型:</label>
					<div class="layui-input-inline">
						<form:select path="type">
							<form:options items="${fns:getDictList('sys_area_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label">备注:</label>
					<div class="layui-input-block">
						<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="layui-textarea"/>
					</div>
				</div>
				<div class="layui-form-item text-center">
					<shiro:hasPermission name="sys:area:edit">
						<input id="btnSubmit" class="layui-btn layui-btn-primary" type="submit" value="保 存" lay-submit=""/>&nbsp;
					</shiro:hasPermission>
					<input id="btnCancel" class="layui-btn layui-btn-primary" type="button" value="返 回" onclick="redirect('${ctx}/sys/area')"/>
				</div>
			</form:form>
		</div>
	</div>
</body>
</html>