<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>${fns:getConfig('productName')}</title>
<meta name="decorator" content="default"/>
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<link href="${ctxStatic}/modules/sys/home.css" type="text/css" rel="stylesheet" />
<script type="text/javascript">
	var menuPosition = "${fns:getConfig('menu.position')}";
	var notifyRemindInterval = "${fns:getConfig('oa.notify.remind.interval')}";
	var server = "${fns:getConfig('cp.server')}";
	var appkey = "${fns:getConfig('cp.appId')}";
	var userId="${fns:getUser().id}";
	var uid = "P_"+userId;
	var jobKind="${fn:length(fns:getCurrentJobKind())>0?fns:getCurrentJobKind()[0].kindCode:""}";
	var currentAirport = "${fns:getConfig('airport_code3')}";
</script>
</head>
<body>
  	<div id="msgs"></div>
	<div class="layui-layout layui-layout-admin">
		<div class="layui-header header" id="header">
			<div class="layui-main">
				<a class="logo">
					<c:if test="${not empty fns:getConfig('logo.img.name')}">
						<img src="${ctxStatic}/images/${fns:getConfig('logo.img.name')}" width="40" height="40" >
					</c:if>
					<span style="color:#ffffff;font-size:24px;">呼和浩特机场</span>
					<span style="color:#ffffff;font-size:14px;">${fns:getConfig('productName')}</span>
				</a>
				<div class="top_admin_user" id="userInfo">
					<c:choose>
						<c:when test="${fns:getUser().loginName eq 'anonymous'}">
							<a href="${ctx}/logout" id="logOutBtn"><i class="fa fa-sign-out">&nbsp;</i>退出</a>
						</c:when>
						<c:otherwise>
							<div class="btn-group">
						  		<a href="javascript:void(0)" target="mainFrame" class="btn btn-link" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
						  			${fns:getUser().name}
						  			<i class="fa fa-caret-down"></i>
						  		</a>
								<ul class="dropdown-menu">
									<li><a href="${ctx}/sys/user/info" target="mainFrame"><i class="fa fa-user">&nbsp;</i>用户信息</a></li> 
								  	<li><a href="${ctx}/sys/user/modifyPwd" target="mainFrame"><i class="fa fa-lock">&nbsp;</i>修改密码</a></li>
								  	<li><a href="javascript:void(0)" id="switchLogin"><i class="fa fa-exchange">&nbsp;</i>切换用户</a></li>
								  	<li><a href="${ctx}/logout" id="logOutBtn"><i class="fa fa-sign-out">&nbsp;</i>退出</a></li>
								</ul>
							</div>
						</c:otherwise>
					</c:choose>
					<c:if test="${fns:getUser().loginName ne 'anonymous'}">
						<a href="javascript:void(0)" 
							id="notifyNum" 
							style="position: relative;" 
							onclick="openMess();" 
							title="消息">
			              <i class="fa fa-bell-o"></i>消息
			              <span class="label label-warning messageWarn"></span>
			            </a>
					</c:if>
		            <a  href="javascript:void(0)" onclick="full_screen();" id="full_sreen" isfull="0">[全屏]</a>
				</div>
			</div>
		</div>
		<div class="layui-body iframe-container layui-body-full" id="iframeContainer">
			<div class="layui-tab" lay-filter="mainTab" lay-allowclose="true">
				<div id="menuToggle">
					<a href="javascript:void(0)">
						<i class="fa fa-th-list">&nbsp;</i>
					</a>
				</div>
				<ul class="layui-tab-title" id="tabTitle" style="display: inline-block;">
				</ul>
				<div class="layui-tab-content">
				</div>
			</div>
		</div>
		<%-- <div
			class="layui-body iframe-container ${(fns:getConfig('menu.position') eq 'T')?'layui-body-full':''}">
			<iframe class="admin-iframe" id="mainFrame" name="mainFrame" src="${firstLink}" ></iframe>
		</div> --%>
		<div class="layui-footer" id="footer">
			<div id="channel_path" style="float:left;margin-left:10px;"></div>
			<div style="float:right;"><span id="onlineUserCount"></span>当前登录：${fns:getUser().name}，<span id="currentTime"></span></div>
		</div>
	</div>
	<script src="${ctxStatic}/prss/message/cloud-push/cloudpush.js" type="text/javascript" ></script>
	<script src="${ctxStatic}/modules/sys/homeMessage.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/sys/homeTime.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/sys/home.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/sys/keepStandGantt.js" type="text/javascript"></script>
</body>
</html>