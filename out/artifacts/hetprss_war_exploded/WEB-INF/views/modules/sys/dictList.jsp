<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>字典管理</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp" %>
	<script type="text/javascript">
		layui.use(["form","laypage"],function(){
			var laypage = layui.laypage;
			var form = layui.form;
			laypage.render({
				elem : 'pagination',
				pages : ${page.last},
				count:${page.count},
				curr:${page.pageNo},
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
		function query(pageNo){
			$("#pageNo").val(pageNo);
			$("#searchForm").submit();
	    }
		function removeDict(id,type){
			layer.confirm('确认要删除该字典吗？', {
			  btn: ['是','取消'] //按钮
			}, function(){
				document.location.href = ctx+"/sys/dict/delete?id="+id+"&type="+type;
			});
		}
	</script>
</head>
<body>
	<div class="box box-widget">
		<div class="box-header with-border">
			<h5 class="box-title">
				字典管理
			</h5>
			<div class="box-tools pull-right">
				<div class="layui-inline">
					<shiro:hasPermission name="sys:dict:edit">
						<button type="button" class="layui-btn layui-btn-small layui-btn-primary" onclick="redirect('${ctx}/sys/dict/form?sort=10')"><i class="fa fa-plus">&nbsp;</i>添加字典</button>
					</shiro:hasPermission>
				</div>
			</div>
		</div>
		<div class="box-body">
			<form:form id="searchForm" modelAttribute="dict" action="${ctx}/sys/dict/" method="post" class="layui-form">
				<input id="pageNo" name="pageNo" id="pageNo" type="hidden" value="${page.pageNo}" />
				<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
				<div class="layui-form-item">
					<label class="layui-form-label">类型：</label>
					<div class="layui-input-inline">
						<form:select id="type" path="type">
							<form:option value="" label="" />
							<form:options items="${typeList}" htmlEscape="false" />
						</form:select>
					</div>
					<label class="layui-form-label">描述 ：</label>
					<div class="layui-input-inline">
						<form:input path="description" htmlEscape="false" maxlength="50" class="layui-input" />
					</div>
					<div class="layui-input-inline">
						<button id="btnSubmit" class="layui-btn layui-btn-primary layui-btn-small" type="button" onclick="query(1)">查询</button>
					</div>
				</div>
			</form:form>
			<sys:message content="${message}" />
			<table id="contentTable" class="layui-table">
				<thead>
					<tr>
						<th>键值</th>
						<th>标签</th>
						<th>类型</th>
						<th>描述</th>
						<th>排序</th>
						<shiro:hasPermission name="sys:dict:edit">
							<th>操作</th>
						</shiro:hasPermission>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${page.list}" var="dict">
						<tr>
							<td>${dict.value}</td>
							<td><a href="${ctx}/sys/dict/form?id=${dict.id}">${dict.label}</a></td>
							<td><a href="javascript:"
								onclick="$('#type').val('${dict.type}');$('#searchForm').submit();return false;">${dict.type}</a></td>
							<td>${dict.description}</td>
							<td>${dict.sort}</td>
							<shiro:hasPermission name="sys:dict:edit">
								<td nowrap class="btn-cell">
									<div class="layui-btn-group">
										<button type="button" class="layui-btn layui-btn-primary layui-btn-small" onclick="redirect('${ctx}/sys/dict/form?id=${dict.id}')">修改</button>
										<button type="button" class="layui-btn layui-btn-primary layui-btn-small" onclick="removeDict('${dict.id}','${dict.type}')">删除</button>
										<button type="button" class="layui-btn layui-btn-primary layui-btn-small" onclick="redirect('<c:url value='${fns:getAdminPath()}/sys/dict/form?type=${dict.type}&sort=${dict.sort+10}'><c:param name='description' value='${dict.description}'/></c:url>')">添加键值</button>
									</div>
								</td>
							</shiro:hasPermission>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div id="pagination"></div>
		</div>
	</div>
</body>
</html>