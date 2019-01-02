<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>活动流程实例</title>
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<script type="text/javascript"
	src="${ctxStatic}/prss/workflow/orderList.js"></script>
</head>
<body>
	<form id="searchForm" action="${ctx}/workflow/order" method="post"
		class="layui-form">
		<input type="hidden" id="pageNo" name="pageNo" value="${page.pageNo}">
		<input type="hidden" id="totalCount" value="${page.totalCount}">
		<input type="hidden" id="pageSize" value="${page.pageSize}">
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">实例状态：</label>
				<div class="layui-input-inline">
					<select name="orderState" lay-filter="orderState">
						<option value="-1">全部</option>
						<option value="1" ${orderState==1?'selected':''}>运行中</option>
						<option value="0" ${orderState==0?'selected':''}>已结束</option>
					</select>
				</div>
			</div>
		</div>
	</form>
	<table id="contentTable" class="layui-table">
		<thead>
			<tr>
				<th>实例编号</th>
				<th>实例创建者</th>
				<th>实例创建时间</th>
				<th>实例结束时间</th>
				<th>实例状态</th>
				<th width="150">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.result}" var="hisOrder">
				<tr>
					<td>${hisOrder.orderNo}</td>
					<td>${hisOrder.creator}</td>
					<td>${hisOrder.createTime}</td>
					<td>${hisOrder.endTime}</td>
					<td>${hisOrder.orderState==0?'已结束':'<font color=green>运行中</font>'}</td>
					<td>
						<button onclick="view('${hisOrder.processId}','${hisOrder.id}')" type="button" class="btn btn-link">查看</button>
						<button onclick="removeOrder('${hisOrder.id}')" type="button" class="btn btn-link">删除</button>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div id="pagination"></div>
</body>
</html>