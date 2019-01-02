<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>修改密码</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/jquery-validation.jsp"%>
	<script type="text/javascript">
		layui.use('element', function(){
		  var element = layui.element; 
		});
		$(document).ready(function() {
			$("#oldPassword").focus();
			$("#inputForm").validate({
				rules: {
				},
				messages: {
					confirmNewPassword: {equalTo: "输入与上面相同的密码"}
				},
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
	<div class="layui-tab">
		<%-- 
		  <ul class="layui-tab-title">
		    <li><a href="${ctx}/sys/user/info">个人信息</a></li>
		    <li class="layui-this"><a href="${ctx}/sys/user/modifyPwd">修改密码</a></li>
		  </ul>
	  	--%>
		<div class="layui-tab-content">
	    <div class="layui-tab-item layui-show">
	    	<form:form id="inputForm" modelAttribute="user" action="${ctx}/sys/user/modifyPwd" method="post" class="layui-form">
				<form:hidden path="id"/>
				<sys:message content="${message}"/>
				<div class="layui-form-item">
					<label class="layui-form-label">旧密码:</label>
					<div class="layui-input-inline">
						<input id="oldPassword" name="oldPassword" type="password" value="" maxlength="50" minlength="3" class="required layui-input"/>
					</div>
					<span class="layui-form-mid layui-word-aux"><font color="red">*</font> </span>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label">新密码:</label>
					<div class="layui-input-inline">
						<input id="newPassword" name="newPassword" type="password" value="" maxlength="50" minlength="3" class="required layui-input"/>
					</div>
					<span class="layui-form-mid layui-word-aux"><font color="red">*</font> </span>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label">确认新密码:</label>
					<div class="layui-input-inline">
						<input id="confirmNewPassword" name="confirmNewPassword" type="password" value="" maxlength="50" minlength="3" class="required layui-input" equalTo="#newPassword"/>
					</div>
					<span class="layui-form-mid layui-word-aux"><font color="red">*</font> </span>
				</div>
				<div class="layui-form-item" style="padding-left:20%;">
					<button type="submit" id="btnSubmit" class="layui-btn layui-btn-primary">保存</button>
				</div>
			</form:form>
	    </div>
	  </div>
	</div>
</body>
</html>