<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>菜单管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/bootstrap.jsp" %>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<script type="text/javascript">
		layui.use(["element","layer"]);
		$(document).ready(function() {
			$("#treeTable").treeTable({expandLevel : 2}).show();
		});
    	function updateSort() {
			layer.load(2);
	    	$("#listForm").attr("action", "${ctx}/sys/menu/updateSort");
	    	$("#listForm").submit();
    	}
    	function removeMenu(menuId){
			layer.confirm('要删除该菜单及所有子菜单项吗？', {
			  btn: ['是','取消'] //按钮
			}, function(){
				document.location.href = ctx+"/sys/menu/delete?id="+menuId;
			});
		}
	</script>
</head>
<body>
	<div class="box box-widget">
		<div class="box-header with-border">
			<h5 class="box-title">
				菜单管理
			</h5>
			<div class="box-tools pull-right">
				<div class="layui-inline">
					<shiro:hasPermission name="sys:menu:edit">
						<button type="button" class="layui-btn layui-btn-small layui-btn-primary" onclick="redirect('${ctx}/sys/menu/form')"><i class="fa fa-plus">&nbsp;</i>添加菜单</button>
						<button type="button" class="layui-btn layui-btn-small layui-btn-primary" onclick="updateSort()">保存排序</button>
					</shiro:hasPermission>
				</div>
			</div>
		</div>
		<div class="box-body">
			<sys:message content="${message}" />
			<form id="listForm" method="post">
				<table id="treeTable" class="layui-table">
					<thead>
						<tr>
							<th>名称</th>
							<th>链接</th>
							<th style="text-align: center;">排序</th>
							<th>可见</th>
							<th>权限标识</th>
							<shiro:hasPermission name="sys:menu:edit">
								<th>操作</th>
							</shiro:hasPermission>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${list}" var="menu">
							<tr id="${menu.id}" pId="${menu.parent.id ne '1'?menu.parent.id:'0'}">
								<td nowrap>
									<i class="fa fa-${not empty menu.icon?menu.icon:' hide'}"></i>
									<a href="${ctx}/sys/menu/form?id=${menu.id}">${menu.name}</a>
								</td>
								<td title="${menu.href}">${fns:abbr(menu.href,30)}</td>
								<td style="text-align: center;"><shiro:hasPermission
										name="sys:menu:edit">
										<input type="hidden" name="ids" value="${menu.id}" />
										<input name="sorts" type="text" value="${menu.sort}"
											style="width: 50px; margin: 0; padding: 0; text-align: center;">
									</shiro:hasPermission>
									<shiro:lacksPermission name="sys:menu:edit">
									${menu.sort}
								</shiro:lacksPermission></td>
								<td>${menu.isShow eq '1'?'显示':'隐藏'}</td>
								<td title="${menu.permission}">${fns:abbr(menu.permission,30)}</td>
								<shiro:hasPermission name="sys:menu:edit">
									<td nowrap class="btn-cell">
										<div class="layui-btn-group">
											<button type="button" class="layui-btn layui-btn-primary layui-btn-small" onclick="redirect('${ctx}/sys/menu/form?id=${menu.id}')">修改</button>
											<button type="button" class="layui-btn layui-btn-primary layui-btn-small" onclick="removeMenu('${menu.id}')">删除</button>
											<button type="button" class="layui-btn layui-btn-primary layui-btn-small" onclick="redirect('${ctx}/sys/menu/form?parent.id=${menu.id}')">添加下级菜单</button>
										</div>
									</td>
								</shiro:hasPermission>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</form>
		</div>
	</div>
</body>
</html>