<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>模板列表</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<script type="text/javascript">
	var layer;
	layui.use(['laypage','form','upload',"layer"],function(){
		var laypage = layui.laypage;
		var form = layui.form;
		layer = layui.layer;
		laypage.render({
			elem : 'pagination',
			pages : ${total},
			curr:${a},
			skip : true,
			skin:"#ccc",
			jump:function(obj, first){
				if(!first){
					 var pageNo = obj.curr;
					 $("#pageNo").val(pageNo);
					 page();
				}
			}
		});
	});
	function page(){
		 $("#searchForm").attr("action", "${ctx}/message/templ/list");
		 $("#searchForm").submit();
	}
</script>
</head>
<body>
	<table id="contentTable" class="layui-table">
		<thead>
			<tr>
				<th></th>
				<th>变量</th>
				<th>名称</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="vo" varStatus="status">
				<tr onDblClick="select(this);">
					<td>${status.index + 1}</td>
					<td>${vo.code}</td>
					<td>${vo.name}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div id="pagination"></div>
</body>
</html>