<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>消息变更轨迹</title>
</head>
<body>
	<table class="layui-table">
		<thead>
			<tr>
				<th width="100">类型</th>
				<th>变更记录</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>时间</td>
				<td>${result1 }</td>
			</tr>
			<tr>
				<td>机场资源</td>
				<td>${result2 }</td>
			</tr>
			<tr>
				<td>航班信息</td>
				<td>${result3 }</td>
			</tr>
		</tbody>
	</table>
</body>
</html>