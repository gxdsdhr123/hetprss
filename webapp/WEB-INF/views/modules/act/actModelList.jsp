<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>模型管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
	<script type="text/javascript">
		layui.use(["form","laypage","layer"],function(){
			var laypage = layui.laypage;
			var form = layui.form;
			laypage.render(({
				elem : 'pagination',
				pages : ${page.last},
				curr:${page.pageNo},
				count:${page.count},
				skip : true,
				//skin:"#ccc"
				jump:function(obj, first){
					if(!first){
						 var pageNo = obj.curr;
						 query(pageNo);
					}
				}
			});
		});
		function page(n,s){
        	location = '${ctx}/act/model/?pageNo='+n+'&pageSize='+s;
        }
		function updateCategory(id, category){
			layer.open({
				type:1,
				title:"设置分类",
				area:["200px","200px"],
				content:$("#categoryBox").html()
			});
			$("#categoryBoxId").val(id);
			$("#categoryBoxCategory").val(category);
		}
	</script>
	<script type="text/template" id="categoryBox">
		<form id="categoryForm" action="${ctx}/act/model/updateCategory" method="post" enctype="multipart/form-data"
			style="text-align:center;" class="form-search" onsubmit="loading('正在分类，请稍等...');"><br/>
			<input id="categoryBoxId" type="hidden" name="id" value="" />
			<select id="categoryBoxCategory" name="category">
				<option value="">无分类</option>
				<c:forEach items="${fns:getDictList('act_category')}" var="dict">
					<option value="${dict.value}">${dict.label}</option>
				</c:forEach>
			</select>
			<br/><br/>　　
			<input id="categorySubmit" class="btn btn-primary" type="submit" value="   保    存   "/>　　
		</form>
	</script>
</head>
<body>
	<div class="box box-widget">
		<div class="box-body">
			<form id="searchForm" action="${ctx}/act/model/" method="post" class="layui-form">
				<div class="layui-form-item">
					<div class="layui-inline">
						<div class="layui-input-inline">
							<select id="category" name="category">
								<option value="">全部分类</option>
								<c:forEach items="${fns:getDictList('act_category')}" var="dict">
									<option value="${dict.value}" ${dict.value==category?'selected':''}>${dict.label}</option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="layui-inline">
						<button id="btnSubmit" class="layui-btn layui-btn-small layui-btn-primary" type="submit" onclick="query(1)">
						 <i class="fa fa-search">&nbsp;</i>查询
						</button>
						&nbsp;
						<button id="btnSubmit" class="layui-btn layui-btn-small layui-btn-primary" type="button" onclick="redirect('${ctx}/act/model/create')">
						 <i class="fa fa-plus">&nbsp;</i>新建模型
						</button>
					</div>
				</div>
			</form>
			<sys:message content="${message}"/>
			<table class="layui-table">
				<thead>
					<tr>
						<th>流程分类</th>
						<th>模型ID</th>
						<th>模型标识</th>
						<th>模型名称</th>
						<th>版本号</th>
						<th>创建时间</th>
						<th>最后更新时间</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${page.list}" var="model">
						<tr>
							<td><a href="javascript:updateCategory('${model.id}', '${model.category}')" title="设置分类">${fns:getDictLabel(model.category,'act_category','无分类')}</a></td>
							<td>${model.id}</td>
							<td>${model.key}</td>
							<td>${model.name}</td>
							<td><b title='流程版本号'>V: ${model.version}</b></td>
							<td><fmt:formatDate value="${model.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							<td><fmt:formatDate value="${model.lastUpdateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							<td>
								<a href="${pageContext.request.contextPath}/act/process-editor/modeler.jsp?modelId=${model.id}" target="_blank">编辑</a>
								<a href="${ctx}/act/model/deploy?id=${model.id}" onclick="return confirmx('确认要部署该模型吗？', this.href)">部署</a>
								<a href="${ctx}/act/model/export?id=${model.id}" target="_blank">导出</a>
			                    <a href="${ctx}/act/model/delete?id=${model.id}" onclick="return confirmx('确认要删除该模型吗？', this.href)">删除</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div id="pagination">${page}</div>
		</div>
	</div>
</body>
</html>
