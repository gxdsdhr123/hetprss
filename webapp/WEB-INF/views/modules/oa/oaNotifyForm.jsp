<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>通知管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/bootstrap.jsp" %>
	<%@include file="/WEB-INF/views/include/jquery-validation.jsp"%>
	<script type="text/javascript">
		layui.use(["form"],function(){
			
		});
		$(document).ready(function() {
			//$("#name").focus();
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
			通知<shiro:hasPermission name="oa:oaNotify:edit">${oaNotify.status eq '1' ? '查看' : not empty oaNotify.id ? '修改' : '添加'}</shiro:hasPermission><shiro:lacksPermission name="oa:oaNotify:edit">查看</shiro:lacksPermission>
		</h5>
	</div>
	<div class="box-body">
		<form:form id="inputForm" modelAttribute="oaNotify" action="${ctx}/oa/oaNotify/save" method="post" class="layui-form">
			<form:hidden path="id"/>
			<sys:message content="${message}"/>	
			<div class="layui-form-item">
				<label class="layui-form-label">类型：</label>
				<div class="layui-input-inline">
					<form:select path="type" class="required">
						<form:option value="" label=""/>
						<form:options items="${fns:getDictList('oa_notify_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
				<span class="layui-form-mid layui-word-aux"><font color="red">*</font> </span>
			</div>	
			<div class="layui-form-item">
				<label class="layui-form-label">标题：</label>
				<div class="layui-input-inline">
					<form:input path="title" htmlEscape="false" maxlength="200" class="layui-input required"/>
				</div>
				<span class="layui-form-mid layui-word-aux"><font color="red">*</font> </span>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label layui-form-text">内容：</label>
				<span class="layui-form-mid layui-word-aux"><font color="red">*</font> </span>
				<div class="layui-input-block">
					<form:textarea path="content" htmlEscape="false" rows="6" maxlength="2000" class="layui-textarea required"/>
				</div>
			</div>
			<c:if test="${oaNotify.status ne '1'}">
				<div class="layui-form-item">
					<label class="layui-form-label">附件：</label>
					<div class="layui-input-block">
						<form:hidden id="files" path="files" htmlEscape="false" maxlength="255" class="layui-input"/>
						<sys:ckfinder input="files" type="files" uploadPath="/oa/notify" selectMultiple="true"/>
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label">状态：</label>
					<div class="layui-input-inline">
						<c:forEach items="${fns:getDictList('oa_notify_status')}" var="status">
							<input name="status" title="${status.label}" type="radio" value="${status.value}" ${oaNotify.status eq status.value?'checked':''} class="required">
						</c:forEach>
					</div>
					<span class="layui-form-mid layui-word-aux"><font color="red">*</font> 发布后不能进行操作。</span>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label">接受人：</label>
					<div class="layui-input-inline">
		                <sys:treeselect id="oaNotifyRecord" name="oaNotifyRecordIds" value="${oaNotify.oaNotifyRecordIds}" labelName="oaNotifyRecordNames" labelValue="${oaNotify.oaNotifyRecordNames}"
							title="用户" url="/sys/office/treeData?type=3" cssClass="form-control required" notAllowSelectParent="true" checked="true"/>
					</div>
					<span class="layui-form-mid layui-word-aux"><font color="red">*</font> </span>
				</div>
			</c:if>
			<c:if test="${oaNotify.status eq '1'}">
				<div class="layui-form-item">
					<label class="layui-form-label">附件：</label>
					<div class="layui-input-inline">
						<form:hidden id="files" path="files" htmlEscape="false" maxlength="255" class="layui-input"/>
						<sys:ckfinder input="files" type="files" uploadPath="/oa/notify" selectMultiple="true" readonly="true" />
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label">接受人：</label>
					<div class="layui-input-block">
						<table id="contentTable" class="layui-table">
							<thead>
								<tr>
									<th>接受人</th>
									<th>接受部门</th>
									<th>阅读状态</th>
									<th>阅读时间</th>
								</tr>
							</thead>
							<tbody>
							<c:forEach items="${oaNotify.oaNotifyRecordList}" var="oaNotifyRecord">
								<tr>
									<td>
										${oaNotifyRecord.user.name}
									</td>
									<td>
										${oaNotifyRecord.user.office.name}
									</td>
									<td>
										${fns:getDictLabel(oaNotifyRecord.readFlag, 'oa_notify_read', '')}
									</td>
									<td>
										<fmt:formatDate value="${oaNotifyRecord.readDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
									</td>
								</tr>
							</c:forEach>
							</tbody>
						</table>
						已查阅：${oaNotify.readNum} &nbsp; 未查阅：${oaNotify.unReadNum} &nbsp; 总共：${oaNotify.readNum + oaNotify.unReadNum}
					</div>
				</div>
			</c:if>
			<div class="layui-form-item text-center">
				<c:if test="${oaNotify.status ne '1'}">
					<shiro:hasPermission name="oa:oaNotify:edit">
						<button type="submit" class="layui-btn layui-btn-primary">保存</button>
					</shiro:hasPermission>
				</c:if>
				<button type="button" class="layui-btn layui-btn-primary" onclick="history.go(-1)">返回</button>
			</div>
		</form:form>
	</div>
</div>
</body>
</html>