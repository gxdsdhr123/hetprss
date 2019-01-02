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
<link href="${ctxStatic}/perfect-scrollbar/css/perfect-scrollbar.css" rel="stylesheet" />
<link href="${ctxStatic}/layui/css/layui.css" rel="stylesheet" />
<link href="${ctxStatic}/layui/css/global.css" rel="stylesheet" />
<link href="${ctxStatic}/layui/css/skin-blue.css" rel="stylesheet" />

<script src="${ctxStatic}/jquery/jquery.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/plugins/jquery-validation/jquery.validate.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/plugins/jquery-validation/validate-messages-zh-CN.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/plugins/jquery-validation/jquery.form.js" type="text/javascript"></script>
<script src="${ctxStatic}/layui/layui.js" type="text/javascript"></script>
<script src="${ctxStatic}/perfect-scrollbar/perfect-scrollbar.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/common/scrollbar.js" type="text/javascript"></script>
<script src="${ctxStatic}/common/fullScreen.js" type="text/javascript"></script>
<script src="${ctxStatic}/common/common.js" type="text/javascript"></script>
<script type="text/javascript">
	 var path = '${path}', ctx = '${ctx}', ctxStatic = '${ctxStatic}';
</script>
<script type="text/javascript">
    //防止页面后退
    history.pushState(null, null, document.URL);
    window.addEventListener('popstate', function () {
        history.pushState(null, null, document.URL);
    });
</script>