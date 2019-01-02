<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
	<%@include file="/WEB-INF/views/include/jquery-validation.jsp"%>
	<script type="text/javascript">
		layui.use(["form","layer"],function(){
			var form = layui.form;
			var layer = layui.layer;
		});
		$(document).ready(function() {
			$("#no").focus();
			$("#inputForm").validate({
				rules: {
					loginName: {remote: "${ctx}/sys/user/checkLoginName?oldLoginName=" + encodeURIComponent('${user.loginName}')}
				},
				messages: {
					loginName: {remote: "用户登录名已存在"},
					confirmNewPassword: {equalTo: "输入与上面相同的密码"}
				},
				submitHandler: function(form){
					if($("input[name=roleIdList]:checked").length<=0){
						layer.msg("请为用户选择角色！",{icon:7});
						return false;
					}
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
				用户<shiro:hasPermission name="sys:user:edit">${not empty user.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:user:edit">查看</shiro:lacksPermission>
			</h5>
		</div>
		<div class="box-body">
			<form:form id="inputForm" modelAttribute="user" action="${ctx}/sys/user/save" method="post" class="layui-form">
				<form:hidden path="id"/>
				<sys:message content="${message}"/>
				<%-- <div class="layui-form-item">
					<label class="layui-form-label">头像:</label>
					<div class="layui-input-inline">
						<form:hidden id="nameImage" path="photo" htmlEscape="false" maxlength="255" class="input-xlarge"/>
						<sys:ckfinder input="nameImage" type="images" uploadPath="/photo" selectMultiple="false" maxWidth="100" maxHeight="100"/>
					</div>
				</div> --%>
				<div class="layui-form-item">
					<label class="layui-form-label"><font color="red">*</font>归属公司:</label>
					<div class="layui-input-inline">
		                <sys:treeselect id="company" name="company.id" value="${user.company.id}" labelName="company.name" labelValue="${user.company.name}"
							title="公司" url="/sys/office/treeData?type=1" cssClass="form-control required"/>
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label"><font color="red">*</font>归属部门:</label>
					<div class="layui-input-inline">
		                <sys:treeselect id="office" name="office.id" value="${user.office.id}" labelName="office.name" labelValue="${user.office.name}"
							title="部门" url="/sys/office/treeData?type=2" cssClass="form-control required" notAllowSelectParent="true"/>
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label"><font color="red">*</font>工号:</label>
					<div class="layui-input-inline">
						<form:input path="no" htmlEscape="false" maxlength="50" class="layui-input required"/>
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label"><font color="red">*</font>姓名:</label>
					<div class="layui-input-inline">
						<form:input path="name" htmlEscape="false" maxlength="50" class="layui-input required"/>
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label"><font color="red">*</font>登录名:</label>
					<div class="layui-input-inline">
						<input id="oldLoginName" name="oldLoginName" type="hidden" value="${user.loginName}">
						<form:input path="loginName" htmlEscape="false" maxlength="50" class="layui-input required loginName"/>
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label"><font color="red">*</font>密码:</label>
					<div class="layui-input-inline">
						<input class="layui-input ${empty user.id?'required':''}" id="newPassword" name="newPassword" type="password" value="" maxlength="50" minlength="3"/>
					</div>
					<c:if test="${not empty user.id}"><span class="layui-form-mid layui-word-aux">若不修改密码，请留空。</span></c:if>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label"><font color="red">*</font>确认密码:</label>
					<div class="layui-input-inline">
						<input class="layui-input" id="confirmNewPassword" name="confirmNewPassword" type="password" value="" maxlength="50" minlength="3" equalTo="#newPassword"/>
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label">职务:</label>
					<div class="layui-input-inline">
						<form:input path="duty" htmlEscape="false" maxlength="100" class="layui-input"/>
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label">邮箱:</label>
					<div class="layui-input-inline">
						<form:input path="email" htmlEscape="false" maxlength="100" class="layui-input"/>
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label">电话:</label>
					<div class="layui-input-inline">
						<form:input path="phone" htmlEscape="false" maxlength="100" class="layui-input"/>
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label">手机:</label>
					<div class="layui-input-inline">
						<form:input path="mobile" htmlEscape="false" maxlength="100" class="layui-input"/>
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label"><font color="red">*</font>是否允许登录:</label>
					<div class="layui-input-inline">
						<form:select path="loginFlag" class="layui-input layui-unselect">
							<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
					<div class="layui-form-mid layui-word-aux"> “是”代表此账号允许登录，“否”则表示此账号不允许登录</div>
				</div>
				
                 <!--add 20180608 by peig -->
				<div class="layui-form-item">
					<label class="layui-form-label">是否接收报文提醒:</label>
				  <div class="layui-input-inline">
				  <form:radiobutton path="asupMsgFlag" value="1" title="是"/>
				  <form:radiobutton path="asupMsgFlag" value="0" title="否"/>			  
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label">用户类型:</label>
					<div class="layui-input-inline">
						<form:select path="userType">
							<form:option value="" label="请选择"/>
							<form:options items="${fns:getDictList('sys_user_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label"><font color="red">*</font>用户角色:</label>
					<div class="layui-input-block">
						<c:forEach items="${allRoles}" var="role">
							<input name="roleIdList" title="${role.name}" value="${role.id}" type="checkbox" ${user.roleIdList.indexOf(role.id)>-1?'checked':''} class="required">
						</c:forEach>
					</div>
				</div>
				<div class="layui-form-item layui-form-text">
					<label class="layui-form-label">备注:</label>
					<div class="layui-input-block">
						<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="layui-textarea"/>
					</div>
				</div>
				<c:if test="${not empty user.id}">
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label">创建时间:</label>
							<label class="layui-form-label text-left" style="width:240px;"><fmt:formatDate value="${user.createDate}" type="both" dateStyle="full"/></label>
						</div>
					</div>
					<div class="layui-form-item">
						<div class="layui-inline">
							<label class="layui-form-label">最后登陆:</label>
							<label class="layui-form-label text-left" style="width:500px;">IP: ${user.loginIp}&nbsp;&nbsp;&nbsp;&nbsp;时间：<fmt:formatDate value="${user.loginDate}" type="both" dateStyle="full"/></label>
						</div>
					</div>
				</c:if>
				<div class="layui-form-item text-center">
					<shiro:hasPermission name="sys:user:edit">
					<button class="layui-btn layui-btn-primary" type="submit">保存</button>
					</shiro:hasPermission>
					<button class="layui-btn layui-btn-primary" type="button" onclick="redirect('${ctx}/sys/user/index')">返回</button>
				</div>
			</form:form>
		</div>
	</div>
</body>
</html>