<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>新增单据</title>
<script type="text/javascript"
	src="${ctxStatic}/prss/produce/newbill.js"></script>
</head>
<body>
	<form id="newBill" action="" class="layui-form">
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">项目</label>
				<div class="layui-input-inline">
					<select id="jobType" name="jobType" lay-filter="jobType">
						<option value="">请选择一个项目</option>
						<c:forEach items="${jobType}" var="item">
							<option value="${item.JOB_TYPE}">${item.TYPE_NAME}<option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">单据</label>
				<div class="layui-input-inline">
					<select id="jobCode" name="jobCode" lay-filter="jobCode">
						<option value="">请选择一个单据</option>
						<c:forEach items="${jobCode}" var="item">
							<option value="${item.TYPE_CODE}">${item.TYPE_CODE}<option>
						</c:forEach>
					</select>
				</div>
			</div>
		</div>
	</form>
	<iframe id="frame" name="frame" width="100%"  frameborder="no"> </iframe>
</body>
</html>