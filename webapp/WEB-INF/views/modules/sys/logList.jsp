<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>日志管理</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	layui.use(["form","laypage"],function(){
		var laypage = layui.laypage;
		var form = layui.form;
		laypage.render({
			elem : 'pagination',
			pages : ${page.last},
			curr:${page.pageNo},
			limit:${page.pageSize},
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
	function query(pageNo) {
		$("#pageNo").val(pageNo);
		$("#searchForm").submit();
		return false;
	}
</script>
</head>
<body>
	<div class="box box-widget">
		<div class="box-header with-border">
			<h5 class="box-title">日志管理</h5>
		</div>
		<div class="box-body">
			<form:form id="searchForm" action="${ctx}/sys/log/" method="post" 
				class="layui-form">
				<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
				<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label">操作菜单：</label>
						<div class="layui-input-inline">
							<input id="title" name="title" type="text" maxlength="50" class="layui-input" value="${log.title}" /> 
						</div>
					</div>
					<div class="layui-inline">
						<label class="layui-form-label">用户ID：</label>
						<div class="layui-input-inline">
							<input id="createBy.id" name="createBy.id" type="text" maxlength="50" class="layui-input" value="${log.createBy.id}" /> 
						</div>
					</div>
					<div class="layui-inline">
						<label class="layui-form-label">URI：</label>
						<div class="layui-input-inline">
							<input id="requestUri" name="requestUri" type="text" maxlength="50" class="layui-input" value="${log.requestUri}" />
						</div>
					</div>
				</div>
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label">日期起始：</label>
						<div class="layui-input-inline">
							<div class="input-group">
			                  	<input id="beginDate" name="beginDate"
								type="text" readonly="readonly" maxlength="20" class="layui-input"
								value="<fmt:formatDate value="${log.beginDate}" pattern="yyyy-MM-dd"/>"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
								<div class="input-group-addon"><i class="fa fa-calendar"></i> </div>
			                </div>
						</div>
					</div>
					<div class="layui-inline">
						<label class="layui-form-label">日期结束：</label>
						<div class="layui-input-inline">
							<div class="input-group">
			                  	<input id="endDate" name="endDate" type="text" readonly="readonly"
									maxlength="20" class="layui-input"
									value="<fmt:formatDate value="${log.endDate}" pattern="yyyy-MM-dd"/>"
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
								<div class="input-group-addon"><i class="fa fa-calendar"></i> </div>
			                </div>
						</div>
					</div>
					<div class="layui-inline">
						<label class="layui-form-label">&nbsp;</label>
						<div class="layui-input-inline">
							<input name="exception" title="只查询异常信息" type="checkbox" value="1" ${log.exception eq '1'?' checked':''}>
						</div>
					</div>
					<div class="layui-inline">
						<button id="btnSubmit" class="layui-btn layui-btn-small layui-btn-primary" type="button" onclick="query(1)">
						 <i class="fa fa-search">&nbsp;</i>查询
						</button>
					</div>
				</div>
			</form:form>
			<sys:message content="${message}" />
			<table id="contentTable" class="layui-table">
				<thead>
					<tr>
						<th>操作菜单</th>
						<th>操作用户</th>
						<th>所在公司</th>
						<th>所在部门</th>
						<th>URI</th>
						<th>提交方式</th>
						<th>操作者IP</th>
						<th>操作时间</th>
				</thead>
				<tbody>
					<%
					    request.setAttribute("strEnter", "\n");
					    request.setAttribute("strTab", "\t");
					%>
					<c:forEach items="${page.list}" var="log">
						<tr>
							<td>${log.title}</td>
							<td>${log.createBy.name}</td>
							<td>${log.createBy.company.name}</td>
							<td>${log.createBy.office.name}</td>
							<td>${log.requestUri}</td>
							<td>${log.method}</td>
							<td>${log.remoteAddr}</td>
							<td><fmt:formatDate value="${log.createDate}" type="both" /></td>
						</tr>
						<c:if test="${not empty log.exception}">
							<tr>
								<td colspan="8"
									style="word-wrap: break-word; word-break: break-all;">
									<%-- 					用户代理: ${log.userAgent}<br/> --%> <%-- 					提交参数: ${fns:escapeHtml(log.params)} <br/> --%>
									异常信息: <br />
									${fn:replace(fn:replace(fns:escapeHtml(log.exception), strEnter,
									'<br/>'), strTab, '&nbsp; &nbsp; ')}
								</td>
							</tr>
						</c:if>
					</c:forEach>
				</tbody>
			</table>
			<div id="pagination"></div>
		</div>
	</div>
</body>
</html>