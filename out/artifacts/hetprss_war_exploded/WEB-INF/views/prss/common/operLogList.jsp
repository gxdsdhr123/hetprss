<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css"
	rel="stylesheet" />
<script type="text/javascript"
	src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="${ctxStatic}/prss/common/operLogList.js"></script>
<title>操作日志查询</title>
</head>
<body>
	<div id="toolBox">
		<form action="" class="layui-form">
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">日志时间：</label>
					<div class="layui-input-inline">
						<input id="startDate" value="${fns:getDate('yyyy-MM-dd')}"
							class="layui-input" type="text" onclick="WdatePicker()"
							readonly="readonly">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label" style="width: 5px">-</label>
					<div class="layui-input-inline">
						<input id="endDate" value="${fns:getDate('yyyy-MM-dd')}"
							class="layui-input" type="text" onclick="WdatePicker()"
							readonly="readonly">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">关键字：</label>
					<div class="layui-input-inline" style="width:110px">
						<select id="colName">
							<option value="-1">不限</option>
							<option value="module">目录名称</option>
							<option value="funcName">模块名称</option>
							<option value="fltNo">航班号</option>
							<option value="fltDate">航班日期</option>
							<option value="userName">操作人姓名</option>
							<option value="loginName">操作人账号</option>
							<option value="officeName">操作人部门</option>
							<option value="remark">备注</option>
						</select>
					</div>
					<div class="layui-input-inline">
						<input id="keyword" class="layui-input" type="text" placeholder="请输入关键字">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label" style="width: 5px">&nbsp;</label>
					<div class="layui-input-inline">
						<button class="layui-btn layui-btn-small layui-btn-primary"
							type="button" onclick="query()">
							<i class="fa fa-search">&nbsp;</i>查询
						</button>
					</div>
				</div>
			</div>
		</form>
	</div>
	<table id="dataGrid"></table>
</body>
</html>