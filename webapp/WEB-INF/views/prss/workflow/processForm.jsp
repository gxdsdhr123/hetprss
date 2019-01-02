<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/snaker.jsp"%>
<title>流程设计器</title>
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<script type="text/javascript"
	src="${ctxStatic}/prss/workflow/processForm.js"></script>
<script type="text/javascript">
	var model = ${processModel};
</script>
</head>
<body>
	<div style="min-height:1024px;">
		<button type="button" id="save" style="display: none">保存</button>
		<div id="toolbox">
			<div id="toolbox_handle">工具集</div>
			<div>
				<button id="addFlow" type="button" class="btn btn-link" style="font-weight:bold;color:#fff"><i class="fa fa-gears">&nbsp;</i>设置流程属性</button>
			</div>
			<div><hr/></div>
			<div class="node selectable" id="pointer">
				<i class="fa fa-mouse-pointer">&nbsp;</i>选择
			</div>
			<div class="node selectable" id="path">
				<i class="fa fa-long-arrow-right">&nbsp;</i>连线
			</div>
			<div><hr/></div>
			<div class="node state" id="start" type="start" name="start">
				<i class="fa fa-circle-thin">&nbsp;</i>开始
			</div>
			<div class="node state" id="end" type="end" name="end">
				<i class="fa fa-circle">&nbsp;</i>结束
			</div>
			<div><hr/></div>
			<div id="node">
			</div>
		</div>
		<div id="snakerflow" style="position: fixed;"></div>
	</div>
	<form:form id="processForm" action="" modelAttribute="process"
		method="post" class="layui-form">
		<form:hidden path="id"/>
		<div id="flowInput" style="display: none;padding-top:20px;">
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">流程名称:</label>
					<div class="layui-input-inline">
						<form:input path="name" class="layui-input" type="text" placeholder="请输入英文或数字" readonly="${not empty process.id?'true':'false'}"/>
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">流程显示名称:</label>
					<div class="layui-input-inline">
						<form:input path="displayName" class="layui-input" type="text"/>
					</div>
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">保障类型:</label>
				<div class="layui-input-inline">
					<form:select path="jobKindId" lay-filter="jobKindId" lay-search="true">
						<form:option value="">请选择</form:option>
						<c:forEach items="${kindList}" var="kind">
							<form:option value="${kind.RESKIND}">${kind.KINDNAME}
							</form:option>
						</c:forEach>
					</form:select>
				</div>

				<label class="layui-form-label">作业类型:</label>
				<div class="layui-input-inline">
					<form:select path="jobTypeId" lay-search="true">
						<form:option value="">请选择</form:option>
						<c:forEach items="${jobTypeList}" var="jobType">
							<form:option value="${jobType.RESTYPE}">${jobType.TYPENAME}
							</form:option>
						</c:forEach>
					</form:select>
				</div>
			</div>
			<div class="layui-form-item" style="display: none;">
				<label class="layui-form-label">发布提醒消息:</label>
				<div class="layui-input-inline">
					<form:select path="releaseAlarmMsg" lay-search="true">
						<form:option value="">请选择</form:option>
						<form:option value="">请选择</form:option>
						<c:forEach items="${template}" var="message">
							<form:option value="${message.ID}">${message.MTITLE}</form:option>
						</c:forEach>
					</form:select>
				</div>
				<label class="layui-form-label">取消提醒消息:</label>
				<div class="layui-input-inline">
					<form:select path="cancelMsg" lay-search="true">
						<form:option value="">请选择</form:option>
						<form:option value="">请选择</form:option>
						<c:forEach items="${template}" var="message">
							<form:option value="${message.ID}">${message.MTITLE}</form:option>
						</c:forEach>
					</form:select>
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">启动提醒消息:</label>
				<div class="layui-input-inline">
					<form:select path="startMsg" lay-search="true">
						<form:option value="">请选择</form:option>
						<form:option value="">请选择</form:option>
						<c:forEach items="${template}" var="message">
							<form:option value="${message.ID}">${message.MTITLE}</form:option>
						</c:forEach>
					</form:select>
				</div>
				<label class="layui-form-label">终止提醒消息:</label>
				<div class="layui-input-inline">
					<form:select path="terminationAlarmMsg" lay-search="true">
						<form:option value="">请选择</form:option>
						<form:option value="">请选择</form:option>
						<c:forEach items="${template}" var="message">
							<form:option value="${message.ID}">${message.MTITLE}</form:option>
						</c:forEach>
					</form:select>
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">转交提醒消息:</label>
				<div class="layui-input-inline">
					<form:select path="surrogateAlarmMsg" lay-search="true">
						<form:option value="">请选择</form:option>
						<form:option value="">请选择</form:option>
						<c:forEach items="${template}" var="message">
							<form:option value="${message.ID}">${message.MTITLE}</form:option>
						</c:forEach>
					</form:select>
				</div>
				<label class="layui-form-label">删除提醒消息:</label>
				<div class="layui-input-inline">
					<form:select path="removeMsg" lay-search="true">
						<form:option value="">请选择</form:option>
						<form:option value="">请选择</form:option>
						<c:forEach items="${template}" var="message">
							<form:option value="${message.ID}">${message.MTITLE}</form:option>
						</c:forEach>
					</form:select>
				</div>
			</div>
		</div>
	</form:form>
</body>
</html>