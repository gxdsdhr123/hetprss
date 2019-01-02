<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp" %>
<link href="${ctxStatic}/prss/message/css/messageTypeInfo.css" rel="stylesheet" />
<title>${flag=='add'?"新增":"修改" }类型</title>
</head>
<script type="text/javascript">
</script>
<body>
	<form id="createForm" class="layui-form" action="${ctx}/message/type/save">
		<input type="hidden" name="flag" id="flag" value="${flag }"/>
		<input type="hidden" name="id" id="id" value="${id }"/>
		<div class="layui-form-item">
		    <div class="layui-inline">
		    	<label class="layui-form-label">类型代码</label>
		    	<div class="layui-input-inline">
		    		<input name="tcode"  class="layui-input" type="text" value="${vo.tcode }">
		      	</div>
		    </div>
		    <div class="layui-inline">
		      	<label class="layui-form-label">类型名称</label>
		      	<div class="layui-input-inline">
		        	<input name="tname" class="layui-input" type="text" value="${vo.tname }">
		      	</div>
		    </div>
		    <div class="layui-inline" style="display:none">
				<label class="layui-form-label">是否需要回复</label>
				<div class="layui-input-inline">
					<input name="ifreply" type="checkbox" id = "ifreply" ${vo.ifreply==1?'checked':''}>
				</div>
		    </div>
		    <div class="layui-inline" style="display:none">
				<label class="layui-form-label">是否与航班关联</label>
				<div class="layui-input-inline">
					<input name="ifflight" type="checkbox" id = "ifflight"  ${vo.ifflight==1?'checked':''}>
				</div>
		    </div>
		</div>
		
		
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">描述</label>
				<div class="layui-input-inline">
					<input name="disc" class="layui-input" type="text" value="${vo.disc }">
				</div>
		    </div>
		</div>
	</form>
	<script type="text/javascript" src="${ctxStatic}/prss/message/messageTypeInfo.js"></script>
	<script type="text/javascript" src="${ctxStatic}/prss/message/ChkUtil.js"></script>
</body>
</html>