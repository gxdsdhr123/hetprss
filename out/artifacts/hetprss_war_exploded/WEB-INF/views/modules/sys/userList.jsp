<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>用户管理</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<script type="text/javascript">
	function parentResize(){
		// 父窗口iframe自适应
		var FHeight = parent.document.documentElement.clientHeight;
		$('#officeContent', parent.window.document).height(FHeight-90);
		// 父窗口ztree自适应
		$('#ztree', parent.window.document).height(FHeight - 95);
	}
	
	$(parent.window).on('resize',function(){
		parentResize();
	});
	parentResize();
	
	var layer;
	layui.use(['laypage','form','upload',"layer"],function(){
		var laypage = layui.laypage;
		var form = layui.form;
		layer = layui.layer;
		laypage.render({
			elem : 'pagination',
			count:${page.count},
			limit:${page.pageSize},
			pages : ${page.last},
			curr:${page.pageNo},
			skip : true,
			//skin:"#ccc"
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
		 $("#searchForm").attr("action", "${ctx}/sys/user/list");
		 $("#searchForm").submit();
	}
	function deleteUser(userId){
		parent.layui.layer.confirm('您确定要删除该用户？', {
		  btn: ['是','取消'] //按钮
		}, function(){
			document.location.href = "${ctx}/sys/user/delete?id="+userId;
			parent.layui.layer.closeAll();
		});
	}
</script>
</head>
<body>
	<form:form id="searchForm" modelAttribute="user" action="${ctx}/sys/user/list" method="post" class="layui-form">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();" />
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">归属公司：</label>
				<div class="layui-input-inline">
					<sys:treeselect id="company"
						name="company.id" value="${user.company.id}"
						labelName="company.name" labelValue="${user.company.name}"
						title="公司" url="/sys/office/treeData?type=1"
						cssClass="form-control" allowClear="true" />
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">登录名：</label>
				<div class="layui-input-inline">
					<form:input path="loginName" htmlEscape="false" maxlength="50" class="layui-input"/>
				</div>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">归属部门：</label>
				<div class="layui-input-inline">
					<sys:treeselect id="office"
						name="office.id" value="${user.office.id}"
						labelName="office.name" labelValue="${user.office.name}"
						title="部门" url="/sys/office/treeData?type=2"
						cssClass="form-control" allowClear="true"
						notAllowSelectParent="true" />
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">姓名：</label>
				<div class="layui-input-inline">
					<form:input path="name" htmlEscape="false" maxlength="50" class="layui-input" />
				</div>
				<div class="layui-inline">
					<button id="btnSubmit" type="submit" onclick="return page();" class="layui-btn layui-btn-small layui-btn-primary"><i class="layui-icon">&#xe615;</i>&nbsp;查询</button>
				</div>
			</div>
		</div>
	</form:form>
	<sys:message content="${message}" />
	<table id="contentTable" class="layui-table">
		<thead>
			<tr>
				<th>归属公司</th>
				<th>归属部门</th>
				<th class="sort-column login_name">登录名</th>
				<th class="sort-column name">姓名</th>
				<th>电话</th>
				<th>手机</th>
				<%--<th>角色</th> --%>
				<shiro:hasPermission name="sys:user:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="user">
				<tr>
					<td>${user.company.name}</td>
					<td>${user.office.name}</td>
					<td><a href="javascript:void(0)" onclick="redirect('${ctx}/sys/user/form?id=${user.id}','parent')">${user.loginName}</a></td>
					<td>${user.name}</td>
					<td>${user.phone}</td>
					<td>${user.mobile}</td>
					<%--<td>${user.roleNames}</td> --%>
					<shiro:hasPermission name="sys:user:edit">
						<td nowrap class="btn-cell">
							<div class="layui-btn-group">
								<button type="button" class="layui-btn layui-btn-primary layui-btn-small" onclick="redirect('${ctx}/sys/user/form?id=${user.id}','parent')">修改</button>
								<button type="button" class="layui-btn layui-btn-primary layui-btn-small" onclick="deleteUser('${user.id}')">删除</button>
							</div>
						</td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div id="pagination"></div>
</body>
</html>