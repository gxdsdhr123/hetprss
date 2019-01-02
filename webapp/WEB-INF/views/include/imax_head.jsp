<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta http-equiv="Expires" content="0">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Cache-Control" content="no-store">

<c:if test="${not empty fns:getConfig('shortcut.icon')}">
	<link rel="Shortcut Icon" href="${ctxStatic}/images/${fns:getConfig('shortcut.icon')}"/>
</c:if>
<link href="${ctxStatic}/bootstrap/fonts/font-awesome.min.css" type="text/css" rel="stylesheet" />
<link href="${ctxStatic}/bootstrap/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
<link href="${ctxStatic}/bootstrap/AdminLTE/css/AdminLTE.min.css" type="text/css" rel="stylesheet"/>
<link href="${ctxStatic}/layui/css/layui.css" rel="stylesheet" />
<link href="${ctxStatic}/layui/css/global.css" rel="stylesheet" />
<link href="${ctxStatic}/prss/imax/common/global.css" rel="stylesheet" />

<script src="${ctxStatic}/jquery/jquery.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/bootstrap/AdminLTE/js/app.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/layui/layui.js" type="text/javascript"></script>
<script src="${ctxStatic}/prss/imax/common/global.js" type="text/javascript"></script>
<script type="text/javascript">
	 var path = '${path}', ctxI = '${ctxI}', ctxStatic = '${ctxStatic}';
</script>
