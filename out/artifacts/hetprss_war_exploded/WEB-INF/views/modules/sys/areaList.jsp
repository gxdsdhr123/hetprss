<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>区域管理</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/treetable.jsp"%>
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<script type="text/javascript">
		var layer;
		layui.use(["layer"],function(){
			layer = layui.layer;
		});
		$(document).ready(function() {
			$("#treeTable").treeTable({expandLevel : 2}).show();
		});
		function removeArea(areaId){
			layer.confirm('您确定要删除该区域？', {
			  btn: ['是','取消'] //按钮
			}, function(){
				document.location.href = ctx+"/sys/area/delete?id="+areaId;
			});
		}
	</script>
</head>
<body>
	<div class="box box-widget">
		<div class="box-header with-border">
			<h5 class="box-title">
				区域管理
			</h5>
			<div class="box-tools pull-right">
				<div class="layui-inline">
					<shiro:hasPermission name="sys:area:edit">
						<button type="button" class="layui-btn layui-btn-small layui-btn-primary" onclick="redirect('${ctx}/sys/area/form')"><i class="fa fa-plus">&nbsp;</i>添加区域</button>
					</shiro:hasPermission>
				</div>
			</div>
		</div>
		<div class="box-body">
			<sys:message content="${message}"/>
			<table id="treeTable" class="layui-table">
				<thead>
					<tr>
						<th>区域名称</th>
						<th>区域编码</th>
						<th>区域类型</th>
						<th>备注</th>
						<shiro:hasPermission name="sys:area:edit">
							<th>操作</th>
						</shiro:hasPermission>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${list}" var="area">
						<tr id="${area.id}" pId="${area.parent.id}">
							<td nowrap>
								<a href="${ctx}/sys/area/form?id=${area.id}">${area.name}</a>
							</td>
							<td>${area.code}</td>
							<td>${fns:getDictLabel(area.type, "sys_area_type", area.type)}</td>
							<td>${area.remarks}</td>
							<shiro:hasPermission name="sys:area:edit">
								<td nowrap class="btn-cell">
									<div class="layui-btn-group">
										<button type="button" class="layui-btn layui-btn-primary layui-btn-small" onclick="redirect('${ctx}/sys/area/form?id=${area.id}')">修改</button>
										<button type="button" class="layui-btn layui-btn-primary layui-btn-small" onclick="removeArea('${area.id}')">删除</button>
										<button type="button" class="layui-btn layui-btn-primary layui-btn-small" onclick="redirect('${ctx}/sys/area/form?parent.id=${area.id}')">添加下级区域</button>
									</div>
								</td>
							</shiro:hasPermission>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>