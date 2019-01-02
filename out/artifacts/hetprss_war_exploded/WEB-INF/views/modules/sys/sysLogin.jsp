<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:getConfig('productName')} 登录</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
	<%@include file="/WEB-INF/views/include/jquery-validation.jsp"%>
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/modules/sys/sysLogin.css">
	<!--[if lt IE 9]>
	<script>
		alert("您的浏览器版本过低，请使用IE9+、火狐、Chrome等高级浏览器！")
	</script>
	<![endif]-->
	<script type="text/javascript">
		var serverIp = '${serverIp}';
		var port = ${port};
	</script>
</head>
<body>
	<div class="layui-layout layui-layout-admin">
		<div class="layui-header" >
			<div class="layui-main">
				<h1 class="productName">
					<img src="${ctxStatic}/images/logoText.png" width="800">
				</h1>
			</div>
		</div>
		<div>
			<form id="loginForm" action="${ctx}/login" class="layui-form" method="post">
				<h1 class="form-title">用户登录</h1>
				<div class="layui-form-item">
					<div class="layui-input-linline">
						<div class="input-group">
			                <span class="input-group-addon fa fa-user"></span>
			                <input id="username" name="username" class="form-control input-lg required" type="text" placeholder="用户名">
			             </div>
					</div>
				</div>
				<br>
				<div class="layui-form-item">
					<div class="layui-input-linline">
						<div class="input-group">
			                <span class="input-group-addon fa fa-key"></span>
			                <input id="password" name="password" class="form-control input-lg required" type="password" placeholder="密码">
			             </div>
					</div>
				</div>
				<br>
				<c:if test="${isValidateCodeLogin}">
					<div class="layui-form-item">
						<div class="layui-input-linline">
							<label class="layui-form-label text-left" style="width:85px;">验证码</label>
							<sys:validateCode name="validateCode" imageCssStyle="cursor: pointer;"/>
						</div>
					</div>
				</c:if>
				<div class="layui-form-item">
					<div class="layui-input-linline">
						<button type="submit" class="layui-btn layui-btn-big" style="width:100%;">登&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;录</button>
					</div>
				</div>
				<div class="layui-form-item text-center ${empty message ? 'layui-hide' : ''}">
					<span style="color:red;font-size: 14px;">${message}</span>
				</div>
				<a href="JavaScript:void(0);" id="anonymousBtn" style="color:#efefef"><i class="fa fa-users">&nbsp;</i>匿名登录</a>
			</form>
			<!-- <div class="layui-footer" style="background: #3781B9;color: #ffffff;">
				版权所有：包头机场 &copy; 2018-
			</div> -->
		</div>
	</div>
	<script type="text/javascript" src="${ctxStatic}/modules/sys/sysLogin.js"></script>
</body>
</html>