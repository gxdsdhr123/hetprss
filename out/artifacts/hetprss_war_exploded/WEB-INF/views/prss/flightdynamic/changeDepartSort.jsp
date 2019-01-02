<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>出港排序变更</title>
<script type="text/javascript"
	src="${ctxStatic}/prss/flightdynamic/changeDepartSort.js"></script>
</head>
<body>
	<form id="form" action="" class="layui-form">
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">出港排序旧值</label>
				<div class="layui-input-inline" >
					<input type="text" id="oldValue" name="oldValue" class="layui-input" 
					value="${DEPART_SORT}" readonly="readonly">
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">出港排序新值</label>
				<div class="layui-input-inline" >
					<input type="text" id="newValue" name="newValue" class="layui-input" >
				</div>
			</div>
		</div>
		<input type="hidden" class="layui-input" name="fltid" id="fltid" value="${fltid}" >
	</form>
</body>
</html>