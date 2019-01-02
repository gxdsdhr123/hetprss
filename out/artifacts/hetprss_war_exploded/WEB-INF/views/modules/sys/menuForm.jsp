<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>菜单管理</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/jquery-validation.jsp"%>
<script type="text/javascript">
	layui.use(["element","form"],function(){
		var element = layui.element;
		
	});
	$(document).ready(function() {
		$("#name").focus();
		$("#inputForm").validate({
			submitHandler: function(form){
				loading('正在提交，请稍等...');
				form.submit();
			},
			errorContainer: "#messageBox",
			errorPlacement: function(error, element) {
				$("#messageBox").text("输入有误，请先更正。");
				if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
					error.appendTo(element.parent().parent());
				} else {
					error.insertAfter(element);
				}
			}
		});
	});
</script>
</head>
<body>
	<div class="box box-widget">
		<div class="box-header with-border">
			<h5 class="box-title">
				菜单<shiro:hasPermission name="sys:menu:edit">${not empty menu.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:menu:edit">查看</shiro:lacksPermission>
			</h5>
		</div>
		<div class="box-body">
			<form:form id="inputForm" modelAttribute="menu" action="${ctx}/sys/menu/save" method="post" class="layui-form">
			<form:hidden path="id" />
			<sys:message content="${message}" />
			<div class="layui-form-item">
				<label class="layui-form-label">上级菜单:</label>
				<div class="layui-input-inline">
					<sys:treeselect id="menu" name="parent.id" value="${menu.parent.id}"
						labelName="parent.name" labelValue="${menu.parent.name}" title="菜单"
						url="/sys/menu/treeData" extId="${menu.id}" cssClass="form-control"/>
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">名称:</label>
				<div class="layui-input-inline">
					<form:input path="name" htmlEscape="false" maxlength="50" class="required layui-input" />
				</div>
				<span class="layui-form-mid layui-word-aux"><font color="red">*</font> </span>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">链接:</label>
				<div class="layui-input-inline" style="width:500px">
					<form:input path="href" htmlEscape="false" maxlength="2000" class="layui-input"/>
				</div>
				<div class="layui-form-mid layui-word-aux">点击菜单跳转的页面</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">目标:</label>
				<div class="layui-input-inline">
					<form:input path="target" htmlEscape="false" maxlength="10" class="layui-input" />
				</div>
				<div class="layui-form-mid layui-word-aux">链接地址打开的目标窗口，默认：mainFrame</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">图标:</label>
				<div class="layui-input-inline">
					<sys:iconselect id="icon" name="icon" value="${menu.icon}" />
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">排序:</label>
				<div class="layui-input-inline">
					<form:input path="sort" htmlEscape="false" maxlength="50" class="required digits layui-input" />
				</div>
				<div class="layui-form-mid layui-word-aux">排列顺序，升序。</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">可见:</label>
				<div class="layui-input-block">
					<c:forEach items="${fns:getDictList('show_hide')}" var="isShow">
						<input name="isShow" title="${isShow.label}" type="radio" value="${isShow.value}" ${menu.isShow eq isShow.value?'checked':''}>
					</c:forEach>
				</div>
				<div class="layui-form-mid layui-word-aux">该菜单或操作是否显示到系统菜单中</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">菜单类型:</label>
				<div class="layui-input-block">
					<input name="sysType" title="PC" type="radio" value="1" ${menu.sysType eq '1'?'checked':''}/>
					<input name="sysType" title="手持机" type="radio" value="2" ${menu.sysType eq '2'?'checked':''}/>
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">权限标识:</label>
				<div class="layui-input-inline" style="width:500px">
					<form:input path="permission" htmlEscape="false" maxlength="100" class="layui-input" />
				</div>
				<div class="layui-form-mid layui-word-aux">控制器中定义的权限标识，如：@RequiresPermissions("权限标识")</div>
			</div>
			<div class="layui-form-item layui-form-text">
				<label class="layui-form-label">备注:</label>
				<div class="layui-input-block" style="margin-left:120px">
					<form:textarea path="remarks" htmlEscape="false" maxlength="200" class="layui-textarea" />
				</div>
			</div>
			<div class="layui-form-item text-center">
				<div class="layui-input-block" id="btn-group">
					<shiro:hasPermission name="sys:menu:edit">
						<button id="btnSubmit" class="layui-btn layui-btn-primary" type="submit">保存</button>
					</shiro:hasPermission>
					<button id="btnCancel" class="layui-btn layui-btn-primary" type="button" onclick="redirect('${ctx}/sys/menu')">返回</button>
				</div>
			</div>
		</form:form>
		</div>
	</div>
</body>
</html>