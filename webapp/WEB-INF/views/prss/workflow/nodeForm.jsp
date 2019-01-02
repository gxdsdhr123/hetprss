<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>节点属性</title>
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript"
	src="${ctxStatic}/prss/workflow/nodeForm.js"></script>
<style type="text/css">
.layui-form-label {
	width: 130px;
}
</style>
</head>
<body>
	<form:form id="nodeForm" action="${ctx}/workflow/node/save"
		modelAttribute="node" method="post" class="layui-form">
		<input type="hidden" name="id" id="id" value="${node.id}">
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">名称：</label>
				<div class="layui-input-inline">
					<form:input type="text" path="name" placeholder="英文名称"
						autocomplete="off" class="layui-input" readonly="${not empty node.id?'true':'false'}"></form:input>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">中文名：</label>
				<div class="layui-input-inline">
					<form:input type="text" path="label" placeholder="中文名"
						autocomplete="off" class="layui-input"></form:input>
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">保障类型：</label>
				<div class="layui-input-inline">
					<form:select path="jobKind" lay-filter="jobKind" lay-search="true">
						<form:option value="">请选择</form:option>
						<form:option value="">请选择</form:option>
						<c:forEach items="${kind}" var="item">
							<form:option value="${item.RESKIND}">${item.KINDNAME}</form:option>
						</c:forEach>
					</form:select>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">作业类型：</label>
				<div class="layui-input-inline">
					<form:select path="jobType" id="jobType">
						<form:option value="">请选择</form:option>
						<form:option value="">请选择</form:option>
						<c:forEach items="${type}" var="item">
							<form:option value="${item.RESTYPE}">${item.TYPENAME}</form:option>
						</c:forEach>
					</form:select>
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">节点简称：</label>
				<div class="layui-input-inline">
					<form:input type="text" path="icon" placeholder="甘特图保障节点显示名称" autocomplete="off"
						class="layui-input" maxlength="2"></form:input>
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">执行后消息1：</label>
				<div class="layui-input-inline">
					<form:select path="aftMsg1" lay-search="true">
						<form:option value="">请选择</form:option>
						<form:option value="">请选择</form:option>
						<c:forEach items="${template}" var="item">
							<form:option value="${item.ID}">${item.MTITLE}【${item.TEMPNAME}】</form:option>
						</c:forEach>
					</form:select>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">执行后消息2：</label>
				<div class="layui-input-inline">
					<form:select path="aftMsg2" lay-search="true">
						<form:option value="">请选择</form:option>
						<form:option value="">请选择</form:option>
						<c:forEach items="${template}" var="item">
							<form:option value="${item.ID}">${item.MTITLE}【${item.TEMPNAME}】</form:option>
						</c:forEach>
					</form:select>
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">预提醒消息：</label>
				<div class="layui-input-inline">
					<form:select path="notifyMsg" lay-search="true">
						<form:option value="">请选择</form:option>
						<form:option value="">请选择</form:option>
						<c:forEach items="${template}" var="item">
							<form:option value="${item.ID}">${item.MTITLE}【${item.TEMPNAME}】</form:option>
						</c:forEach>
					</form:select>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">预提醒时间：</label>
				<div class="layui-input-inline">
					<form:input type="text" path="notifyTm"
						autocomplete="off" class="layui-input"></form:input>
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">一级预警消息：</label>
				<div class="layui-input-inline">
					<form:select path="alarmMsgLv1" lay-search="true">
						<form:option value="">请选择</form:option>
						<form:option value="">请选择</form:option>
						<c:forEach items="${template}" var="item">
							<form:option value="${item.ID}">${item.MTITLE}【${item.TEMPNAME}】</form:option>
						</c:forEach>
					</form:select>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">提醒时间：</label>
				<div class="layui-input-inline">
					<form:input type="text" path="alarmTm1" 
						autocomplete="off" class="layui-input"></form:input>
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">二级预警消息：</label>
				<div class="layui-input-inline">
					<form:select path="alarmMsgLv2" lay-search="true">
						<form:option value="">请选择</form:option>
						<form:option value="">请选择</form:option>
						<c:forEach items="${template}" var="item">
							<form:option value="${item.ID}">${item.MTITLE}【${item.TEMPNAME}】</form:option>
						</c:forEach>
					</form:select>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">提醒时间：</label>
				<div class="layui-input-inline">
					<form:input type="text" path="alarmTm2" 
						autocomplete="off" class="layui-input"></form:input>
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">三级预警消息：</label>
				<div class="layui-input-inline">
					<form:select path="alarmMsgLv3" lay-search="true">
						<form:option value="">请选择</form:option>
						<form:option value="">请选择</form:option>
						<c:forEach items="${template}" var="item">
							<form:option value="${item.ID}">${item.MTITLE}【${item.TEMPNAME}】</form:option>
						</c:forEach>
					</form:select>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">提醒时间：</label>
				<div class="layui-input-inline">
					<form:input type="text" path="alarmTm3" 
						autocomplete="off" class="layui-input"></form:input>
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">四级预警消息：</label>
				<div class="layui-input-inline">
					<form:select path="alarmMsgLv4" lay-search="true">
						<form:option value="">请选择</form:option>
						<form:option value="">请选择</form:option>
						<c:forEach items="${template}" var="item">
							<form:option value="${item.ID}">${item.MTITLE}【${item.TEMPNAME}】</form:option>
						</c:forEach>
					</form:select>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">提醒时间：</label>
				<div class="layui-input-inline">
					<form:input type="text" path="alarmTm4" 
						autocomplete="off" class="layui-input"></form:input>
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">五级预警消息：</label>
				<div class="layui-input-inline">
					<form:select path="alarmMsgLv5" lay-search="true">
						<form:option value="">请选择</form:option>
						<form:option value="">请选择</form:option>
						<c:forEach items="${template}" var="item">
							<form:option value="${item.ID}">${item.MTITLE}【${item.TEMPNAME}】</form:option>
						</c:forEach>
					</form:select>
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">提醒时间：</label>
				<div class="layui-input-inline">
					<form:input type="text" path="alarmTm5" 
						autocomplete="off" class="layui-input"></form:input>
				</div>
			</div>
		</div>
		<textarea id="btns" name="btns" style="display:none" ></textarea>
	</form:form>
	<div id="toolbar">
		<button id="addBtnBtn" type="button" class="btn btn-link">新增按钮</button>
	</div>
	<table id="nodeBtnGrid"></table>
</body>
</html>