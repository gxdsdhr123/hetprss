<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>限制参数表单</title>
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript" src="${ctxStatic}/prss/workflow/limitParmForm.js"></script>
</head>
<body>
	<form action="" class="layui-form" method="post">
		<input type="hidden" name="id" id="id" value="${id}">
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">保障类型：</label>
				<div class="layui-input-inline">
					<select id="jobKind" lay-search="true" lay-filter="jobKind">
						<option value="">请选择</option>
						<c:forEach items="${jobKind}" var="kind" >
							<option value="${kind.RESKIND}">${kind.KINDNAME}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">作业类型：</label>
				<div class="layui-input-inline">
					<select id="jobType" lay-search="true" lay-filter="jobType">
						<option value="">请选择</option>
					</select>
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">流程模板：</label>
				<div class="layui-input-inline">
					<select id="process" lay-search="true" lay-filter="process">
						<option value="">请选择</option>
					</select>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">流程节点：</label>
				<div class="layui-input-inline">
					<select id="node">
						<option value="">请选择</option>
					</select>
				</div>
			</div>
		</div>
	</form>
</html>