<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page
	import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>${fns:getConfig('productName')}登录</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta http-equiv="Expires" content="0">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Cache-Control" content="no-store">
<link href="${ctxStatic}/bootstrap/fonts/font-awesome.min.css" type="text/css" rel="stylesheet" />
<link href="${ctxStatic}/layui/css/layui.css" rel="stylesheet" />
<link href="${ctxStatic}/layui/css/global.css" rel="stylesheet" />
<link href="${ctxStatic}/layui/css/skin-blue.css" rel="stylesheet" />
<script src="${ctxStatic}/layui/layui.js" type="text/javascript"></script>
<script type="text/javascript">
	layui.use([ "layer", "form" ]);
</script>
</head>
<body>
	<form id="loginForm" action="${ctx}/switchLogin" class="layui-form" method="post" style="height: 50px;">
		<div class="layui-form-item">
			<label class="layui-form-label">用户名：</label>
			<div class="layui-input-inline">
				<input id="username" name="username" class="layui-input" type="text" placeholder="请输入用户名" >
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">密码：</label>
			<div class="layui-input-inline">
				<input id="password" name="password" class="layui-input" type="password" placeholder="请输入密码" >
			</div>
		</div>
		<div class="layui-form-item text-center">
			<span style="font-size: 14px;" id="message"></span>
		</div>
	</form>
</body>
</html>