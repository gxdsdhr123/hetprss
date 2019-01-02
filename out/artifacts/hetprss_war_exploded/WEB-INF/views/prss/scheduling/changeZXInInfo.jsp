<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title></title>
<script type="text/javascript"
	src="${ctxStatic}/prss/scheduling/changeZXInInfo.js"></script>
</head>
<body>
	<form id="form" action="" class="layui-form">
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">进港特货信息</label>
				<div class="layui-input-inline" >
					<input type="text" id="inSpecialCargo" name="inSpecialCargo" class="layui-input" 
					value="${plusInfo.IN_SPECIAL_CARGO}" >
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">进港装卸备注</label>
				<div class="layui-input-block">
				<textarea  id="inZxRemark" name="inZxRemark"
					class="layui-textarea" >${plusInfo.IN_ZX_REMARK}</textarea>
			</div>
			</div>
		</div>
		
		<input type="hidden" class="layui-input" name="fltid" id="fltid" value="${fltid}" >
		<input type="hidden" class="layui-input" name="inSpecialCargoOld" id="inSpecialCargoOld" value="${plusInfo.IN_SPECIAL_CARGO}" >
		<input type="hidden" class="layui-input" name="inZxRemarkOld" id="inZxRemarkOld" value="${plusInfo.IN_ZX_REMARK}" >
	</form>
</body>
</html>