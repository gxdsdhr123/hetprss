<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>分配角色</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
	<script type="text/javascript">
		layui.use("layer",function(){
			$("#assignButton").click(function(){
				layer.open({
					title:"选择人员",
					type:2,
					btn:["确定分配","清除已选","关闭"],
					content:["${ctx}/sys/role/usertorole?id=${role.id}","no"],
					area:["800px","420px"],
					yes:function(index,layero){
						var iframeWin = window[layero.find('iframe')[0]['name']];
						var pre_ids = iframeWin.pre_ids;
						var ids = iframeWin.ids;
						// 删除''的元素
						if(ids[0]==''){
							ids.shift();
							pre_ids.shift();
						}
						if(pre_ids.sort().toString() == ids.sort().toString()){
							layer.alert("未给角色【${role.name}】分配新成员！");
							return false;
						};
				    	// 执行保存
				    	layer.load(1);
				    	var idsArr = "";
				    	for (var i = 0; i<ids.length; i++) {
				    		idsArr = (idsArr + ids[i]) + (((i + 1)== ids.length) ? '':',');
				    	}
				    	$('#idsArr').val(idsArr);
				    	$('#assignRoleForm').submit();
				    	layer.closeAll();
					},
					btn2:function(index,layero){
						var iframeWin = window[layero.find('iframe')[0]['name']];
						iframeWin.clearAssign();
						return false;
					}
				});
			});
		});
	</script>
</head>
<body>
	<div class="box box-widget">
		<div class="box-header with-border">
			<h3 class="box-title">
				<shiro:hasPermission name="sys:role:edit">角色分配</shiro:hasPermission><shiro:lacksPermission name="sys:role:edit">人员列表</shiro:lacksPermission>
			</h3>
			<div class="box-tools pull-right">
				<div class="layui-inline">
					<button type="button" class="layui-btn layui-btn-small layui-btn-primary" id="assignButton">选择人员</button>
					<button type="button" class="layui-btn layui-btn-small layui-btn-primary"
							onclick="redirect('${ctx}/sys/role/list')">返回列表</button>
				</div>
			</div>
		</div>
		<div class="box-body">
			<sys:message content="${message}"/>
			<form id="assignRoleForm" action="${ctx}/sys/role/assignrole" method="post" class="layui-form">
				<input type="hidden" name="id" value="${role.id}"/>
				<input id="idsArr" type="hidden" name="idsArr" value=""/>
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label">角色名称：</label>
						<label class="layui-form-label"><b>${role.name}</b></label>
					</div>
					<div class="layui-inline">
						<label class="layui-form-label">归属机构：</label>
						<label class="layui-form-label"><b>${role.office.name}</b></label>
					</div>
					<div class="layui-inline">
						<label class="layui-form-label">英文名称：</label>
						<label class="layui-form-label"><b>${role.enname}</b> </label>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label">角色类型：</label>
						<label class="layui-form-label"><b>${role.roleType}</b></label>
					</div>
					<c:set var="dictvalue" value="${role.dataScope}" scope="page" />
					<div class="layui-inline">
						<label class="layui-form-label">数据范围：</label>
						<label class="layui-form-label"> <b>${fns:getDictLabel(dictvalue, 'sys_data_scope', '')}</b></label>
					</div>
				</div>
			</form>
			<table id="contentTable" class="layui-table">
				<thead>
					<tr>
						<th>归属公司</th>
						<th>归属部门</th>
						<th>登录名</th>
						<th>姓名</th>
						<th>电话</th>
						<th>手机</th>
						<shiro:hasPermission name="sys:user:edit">
							<th>操作</th>
						</shiro:hasPermission>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${userList}" var="user">
						<tr>
							<td>${user.company.name}</td>
							<td>${user.office.name}</td>
							<td><a href="${ctx}/sys/user/form?id=${user.id}">${user.loginName}</a></td>
							<td>${user.name}</td>
							<td>${user.phone}</td>
							<td>${user.mobile}</td>
							<shiro:hasPermission name="sys:role:edit">
								<td><a
									href="${ctx}/sys/role/outrole?userId=${user.id}&roleId=${role.id}"
									onclick="return confirmx('确认要将用户<b>[${user.name}]</b>从<b>[${role.name}]</b>角色中移除吗？', this.href)">移除</a>
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
