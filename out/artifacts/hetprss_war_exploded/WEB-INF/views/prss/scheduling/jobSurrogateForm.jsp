<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>任务代点原因</title>
<meta name="decorator" content="default" />
</head>
<body>
	<form action="" class="layui-form" style="height:200px">
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">代点原因：</label>
				<div class="layui-input-inline">
					<select id="reason">
						<option value="机器故障">机器故障</option>
						<option value="其他">其他</option>
					</select>
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">原因描述：</label>
			<div class="layui-input-block">
				<textarea id="remark" class="layui-textarea" placeholder="请输入"></textarea>
			</div>
		</div>
	</form>
	<script type="text/javascript"
		src="${ctxStatic}/prss/scheduling/jobSurrogateForm.js"></script>
</body>
</html>