<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
<title></title>
<meta name="decorator" content="default" />
<style type="text/css">
.a{
	cursor: pointer;
	color: white;
	text-decoration: underline;
}
</style>
<script type="text/javascript">
</script>
</HEAD>
<BODY>
		<input type="hidden" value="${port }" id="port"  />
		<input type="hidden" value="${ip }" id="ip"  />
		<table id="contentTable" class="layui-table">
			<thead>
				<tr>
					<th>通信人</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${list}" var="vo">
					<tr>
						<td>${vo.name}</td>
						<td><input type="button" value="挂断" class="layui-btn layui-btn-small layui-btn-normal messagefile"
							onClick="stop(this,'${vo.phone}')" /></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
<script src="${ctxStatic}/prss/message/talk.js" type="text/javascript" ></script>
</BODY>
</HTML>
