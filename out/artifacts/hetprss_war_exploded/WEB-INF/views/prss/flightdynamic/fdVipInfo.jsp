<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<title>要客详情</title>
<script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/fdVipInfo.js"></script>
<script type="text/javascript">
	var ioFlag = ${ioFlag};
	var vipFlag = ${vipFlag};
</script>
</head>
<body>
	<shiro:hasPermission name="fd:vipInfo:edit">
		<button id="editBtn" type="button" style="display: none;" class="btn btn-link">编辑</button>
	</shiro:hasPermission>
	<shiro:hasPermission name="scheduling:vipInfo:edit">
		<button id="scheEditBtn" type="button" style="display: none;" class="btn btn-link">编辑</button>
	</shiro:hasPermission>
	<input type="hidden" name="inFltid" id="inFltid" value="${inFltid}">
	<input type="hidden" name="outFltid" id="outFltid" value="${outFltid}">
	<input type="hidden" name="inFltNo" id="inFltNo" value="${inFltNo}">
	<input type="hidden" name="outFltNo" id="outFltNo" value="${outFltNo}">
	<input type="hidden" name="hisFlag" id="hisFlag" value="${hisFlag}">
	<div id="toolbar">
		<button id="addVipInfoBtn" type="button" class="btn btn-link">新增行</button>
	</div>
	<table id="fdVipInfoGrid"></table>
</body>
</html>