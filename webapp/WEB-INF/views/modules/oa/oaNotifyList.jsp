<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>通知管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/bootstrap.jsp" %>
	<script type="text/javascript">
		layui.use(["form","laypage"],function(){
			var laypage = layui.laypage;
			var form = layui.form;
			laypage.render({
				cont : 'pagination',
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
		function query(pageNo){
			$("#pageNo").val(pageNo);
			$("#searchForm").submit();
	    }
		function removeNotify(id){
			layer.confirm('确认要删除该通知吗？', {
			  btn: ['是','取消'] //按钮
			}, function(){
				document.location.href = ctx+"/oa/oaNotify/delete?id="+id;
			});
		}
	</script>
</head>
<body>
<sys:message content="${message}"/>
<div class="box box-widget">
		<div class="box-header with-border">
			<h5 class="box-title">
				通知列表
			</h5>
			<div class="box-tools pull-right">
				<div class="layui-inline">
					<c:if test="${!oaNotify.self}">
						<shiro:hasPermission name="oa:oaNotify:edit">
							<button type="button" class="layui-btn layui-btn-small layui-btn-primary" onclick="redirect('${ctx}/oa/oaNotify/form')"><i class="fa fa-plus">&nbsp;</i>通知添加</button>
						</shiro:hasPermission>
					</c:if>
				</div>
			</div>
		</div>
		<div class="box-body">
			<form:form id="searchForm" modelAttribute="oaNotify" action="${ctx}/oa/oaNotify/${oaNotify.self?'self':''}" method="post" class="layui-form">
				<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
				<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
				<div class="layui-form-item">
					<label class="layui-form-label">标题：</label>
					<div class="layui-input-inline">
						<form:input path="title" htmlEscape="false" maxlength="200" class="layui-input"/>
					</div>
					<label class="layui-form-label">类型：</label>
					<div class="layui-input-inline">
						<form:select path="type">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('oa_notify_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
					<c:if test="${!requestScope.oaNotify.self}">
						<label class="layui-form-label">状态：</label>
						<div class="layui-input-inline">
							<form:select path="status">
								<form:option value="" label=""/>
								<form:options items="${fns:getDictList('oa_notify_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
							</form:select>
						</div>
					</c:if>
					<button type="button" class="layui-btn layui-btn-primary layui-btn-small" onclick="query(1)">查询</button>
				</div>
			</form:form>
			<table id="contentTable" class="layui-table">
				<thead>
					<tr>
						<th>标题</th>
						<th>类型</th>
						<th>状态</th>
						<th>查阅状态</th>
						<th>更新时间</th>
						<c:if test="${!oaNotify.self}"><shiro:hasPermission name="oa:oaNotify:edit"><th>操作</th></shiro:hasPermission></c:if>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${page.list}" var="oaNotify">
					<tr>
						<td>
							<a href="${ctx}/oa/oaNotify/${requestScope.oaNotify.self?'view':'form'}?id=${oaNotify.id}">
								${fns:abbr(oaNotify.title,50)}
							</a>
						</td>
						<td>
							${fns:getDictLabel(oaNotify.type, 'oa_notify_type', '')}
						</td>
						<td>
							${fns:getDictLabel(oaNotify.status, 'oa_notify_status', '')}
						</td>
						<td>
							<c:if test="${requestScope.oaNotify.self}">
								${fns:getDictLabel(oaNotify.readFlag, 'oa_notify_read', '')}
							</c:if>
							<c:if test="${!requestScope.oaNotify.self}">
								${oaNotify.readNum} / ${oaNotify.readNum + oaNotify.unReadNum}
							</c:if>
						</td>
						<td>
							<fmt:formatDate value="${oaNotify.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
						</td>
							<c:if test="${!requestScope.oaNotify.self}">
								<shiro:hasPermission name="oa:oaNotify:edit">
									<td nowrap class="btn-cell">
										<div class="layui-btn-group">
											<button type="button" class="layui-btn layui-btn-primary layui-btn-small" onclick="redirect('${ctx}/oa/oaNotify/form?id=${oaNotify.id}')">修改</button>
											<button type="button" class="layui-btn layui-btn-primary layui-btn-small" onclick="removeNotify('${oaNotify.id}')">删除</button>
										</div>
									</td>
								</shiro:hasPermission>
							</c:if>
						</tr>
				</c:forEach>
				</tbody>
			</table>
			<div id="pagination"></div>
		</div>
	</div>
</body>
</html>