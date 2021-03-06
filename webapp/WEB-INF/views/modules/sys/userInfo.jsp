<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>个人信息</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/jquery-validation.jsp"%>
	<script type="text/javascript">
		layui.use(['element','layer'], function(){
		  var element = layui.element; 
		});
		$(document).ready(function() {
			$("#inputForm").validate({
				submitHandler: function(form){
					layer.load(2);
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
	<div class="layui-tab">
		<%-- 	  
		<ul class="layui-tab-title">
		    <li class="layui-this"><a href="${ctx}/sys/user/info">个人信息</a></li>
		    <li><a href="${ctx}/sys/user/modifyPwd">修改密码</a></li>
		  </ul>
	   --%>	  
	   <div class="layui-tab-content">
	    <div class="layui-tab-item layui-show">
	    	<form:form id="inputForm" modelAttribute="user" action="${ctx}/sys/user/info" method="post" class="layui-form"><%--
				<form:hidden path="email" htmlEscape="false" maxlength="255" class="input-xlarge"/>
				<sys:ckfinder input="email" type="files" uploadPath="/mytask" selectMultiple="false"/> --%>
				<sys:message content="${message}"/>
				<%-- <div class="layui-form-item">
					<label class="layui-form-label">头像：</label>
					<div class="layui-input-inline">
						<form:hidden id="nameImage" path="photo" htmlEscape="false" maxlength="255" class="input-xlarge"/>
						<sys:ckfinder input="nameImage" type="images" uploadPath="/photo" selectMultiple="false" maxWidth="100" maxHeight="100"/>
					</div>
				</div> --%>
				<div class="layui-form-item">
					<label class="layui-form-label">归属公司：</label>
					<div class="layui-input-inline">
						<label class="layui-form-label text-left">${user.company.name}</label>
					</div>
					<label class="layui-form-label">归属部门：</label>
					<div class="layui-input-inline">
						<label class="layui-form-label text-left">${user.office.name}</label>
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label">用户类型：</label>
					<div class="layui-input-inline">
						<label class="layui-form-label text-left">${fns:getDictLabel(user.userType, 'sys_user_type', '无')}</label>
					</div>
					<label class="layui-form-label">用户角色：</label>
					<div class="layui-input-inline">
						<label class="layui-form-label text-left">${user.roleNames}</label>
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label">姓名：</label>
					<div class="layui-input-inline">
						<form:input path="name" htmlEscape="false" maxlength="50" class="required layui-input layui-disabled" readonly="true"/>
					</div>
					<label class="layui-form-label">邮箱：</label>
					<div class="layui-input-inline">
						<form:input path="email" htmlEscape="false" maxlength="50" class="email layui-input"/>
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label">电话：</label>
					<div class="layui-input-inline">
						<form:input path="phone" htmlEscape="false" maxlength="50" class="layui-input"/>
					</div>
					<label class="layui-form-label">手机：</label>
					<div class="layui-input-inline">
						<form:input path="mobile" htmlEscape="false" maxlength="50" class="layui-input"/>
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label">备注：</label>
					<div class="layui-input-block">
						<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="layui-textarea"/>
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label">上次登录：</label>
					<div class="layui-input-block">
						<label class="layui-form-label text-left" style="width:80%;">IP: ${user.oldLoginIp}&nbsp;&nbsp;&nbsp;&nbsp;时间：<fmt:formatDate value="${user.oldLoginDate}" type="both" dateStyle="full"/></label>
					</div>
				</div>
				<div class="layui-form-item text-center" id="btn-group">
					<button id="btnSubmit" class="layui-btn layui-btn-primary" type="submit" >保 存</button>
				</div>
			</form:form>
	    </div>
	  </div>
	</div>
</body>
</html>