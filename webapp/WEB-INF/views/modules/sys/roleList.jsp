]<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>角色管理</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<script type="text/javascript">
	var layer;
	layui.use(["layer"],function(){
		layer = layui.layer;
	});
	function delRole(roleId){
		layer.confirm('确认要删除该角色吗？', {
		  btn: ['是','取消'], //按钮
		  icon:3
		}, function(){
			document.location.href = ctx+"/sys/role/delete?id="+roleId;
		});
	}
</script>
</head>
<body>
	<div class="box box-widget">
		<div class="box-header with-border">
			<h3 class="box-title">角色管理</h3>
			<div class="box-tools pull-right">
				<div class="layui-inline">
					<shiro:hasPermission name="sys:role:edit">
						<button type="button" class="layui-btn layui-btn-small layui-btn-primary"
							onclick="redirect('${ctx}/sys/role/form')">添加角色</button>
					</shiro:hasPermission>
				</div>
			</div>
		</div>
		<div class="box-body">
			<sys:message content="${message}" />
			<table id="contentTable" class="layui-table" lay-skin="line">
				<thead>
					<tr>
						<th>角色名称</th>
						<th>英文名称</th>
						<th>归属机构</th>
						<th>数据范围</th>
						<shiro:hasPermission name="sys:role:edit">
							<th>操作</th>
						</shiro:hasPermission>
					</tr>
				</thead>
				<c:forEach items="${list}" var="role">
					<tr>
						<td><a href="${ctx}/sys/role/form?id=${role.id}">${role.name}</a></td>
						<td><a href="${ctx}/sys/role/form?id=${role.id}">${role.enname}</a></td>
						<td>${role.office.name}</td>
						<td>${fns:getDictLabel(role.dataScope, 'sys_data_scope','无')}</td>
						<shiro:hasPermission name="sys:role:edit">
							<td nowrap class="btn-cell">
								<div class="layui-btn-group">
									<button type="button" class="layui-btn layui-btn-primary layui-btn-small" onclick="redirect('${ctx}/sys/role/assign?id=${role.id}')">分配人员</button>
									<c:if test="${(role.sysData eq fns:getDictValue('是', 'yes_no', '1') && fns:getUser().admin)||!(role.sysData eq fns:getDictValue('是', 'yes_no', '1'))}">
										<a href="${ctx}/sys/role/form?id=${role.id}">修改</a>
										<button type="button" class="layui-btn layui-btn-primary layui-btn-small" onclick="redirect('${ctx}/sys/role/form?id=${role.id}')">修改</button>
									</c:if> 
									<button type="button" class="layui-btn layui-btn-primary layui-btn-small" onclick="delRole('${role.id}')">删除</button>
								</div>
							</td>
						</shiro:hasPermission>
					</tr>
				</c:forEach>
			</table>
		</div>
	</div>
</body>
</html>