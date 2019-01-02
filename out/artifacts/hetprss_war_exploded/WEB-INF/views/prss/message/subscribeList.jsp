<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>消息订阅</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css"
	rel="stylesheet" />
<script type="text/javascript"
	src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
</head>
<body>
	<div class="row">
		<div class="col-md-12 col-xs-12 col-sm-12">
			<div class="box-body">
			
				<div class="layui-inline">
					<label class="layui-form-label">触发事件</label>
					<div class="layui-input-inline">
						<input class="layui-input" type="text" name="event">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">航班号</label>
					<div class="layui-input-inline">
						<input class="layui-input" type="text" name="flightnumber">
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">创建时间</label>
					<div class="layui-input-inline">
						<input type='text' maxlength="20" name='crtime'
							placeholder='请选择日期' class='form-control'
							onclick="WdatePicker({dateFmt:'yyyyMMdd'});" />
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">启用状态</label>
					<div class="layui-input-inline">
						<select name="disable" id="disable" class="form-control" style="width: 155px;">
							<option value="-1">全部</option>
							<option value="1">启用</option>
							<option value="0">停用</option>
						</select>
					</div>
				</div>
				<div class="layui-inline">
					<label class="layui-form-label">保障类型</label>
					<div class="layui-input-inline">
						<select name="jobKind" id="jobKind" class="form-control" style="width: 155px;">
							<option value="all">全部</option>
							<c:forEach items="${jobKindList}" var="item">
								<option value="${item.CODE }">${item.NAME}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="bs-bars pull-right">
					<div id="toolbar">
						<button class="btn btn-link search" type="button">查询</button>
						<button class="btn btn-link " type="button" id="create">新增</button>
						<button id="remove" type="button" class="btn btn-link">删除</button>
					</div>
				</div>
				<table id="baseTable"
					class="table table-striped table-bordered table-hover"></table>
			</div>
		</div>
	</div>
	<script type="text/javascript"
		src="${ctxStatic}/prss/message/subscribeList.js"></script>

</body>
</html>