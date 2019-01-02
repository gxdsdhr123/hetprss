<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>员工班组维护</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/head.jsp"%>
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<link href="${ctxStatic}/prss/arrange/css/classGroupForm.css"
	rel="stylesheet" />
<script type="text/javascript">
	var aPerson = '${aPerson}';
	var sPerson = '${sPerson}';
</script>
</head>
<body>
	<div class="content">
		<form:form id="createForm" class="layui-form"
			modelAttribute="cgEntity" action="${ctx}/arrange/classGroup/save"
			enctype="multipart/form-data" method="post">

			<form:input path="id" style="display: none" id="ID" type="text" />
			<form:input path="sPerson" id="sPerson" style="display: none"
				type="text" />
			<div class="row">
				<div class="col-md-6">
					<div class="row">
						<div class="col-md-4">
							<label class="layui-form-label">班组名称：<font
								class="required">*</font></label>
						</div>
						<div class="col-md-5">
							<form:input path="cgname" name="cgname" htmlEscape="false"
								placeholder="请输入" class="layui-input" type="text" 
								/>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4">
							<label class="layui-form-label">领班：<font class="required">*</font></label>
						</div>
						<div class="col-md-6">
							<div class="layui-input-inline">
								<form:select path="pmid" id="PM" lay-verify="required"
									lay-search="true" class="layui-input">
									<form:option value=""></form:option>
									<form:option value="">-</form:option>
									<c:forEach items="${lPerson}" var="item">
										<form:option value="${item.ID}">${item.NAME}</form:option>
									</c:forEach>
								</form:select>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4">
							<label class="layui-form-label">副领班：</label>
						</div>
						<div class="col-md-6">
							<div class="layui-input-inline">
								<form:select path="smid" id="SM" lay-verify="required"
									lay-search="true" value="${SM}" class="layui-input">
									<form:option value=""></form:option>
									<form:option value="">-</form:option>
									<c:forEach items="${lPerson}" var="item">
										<form:option value="${item.ID}">${item.NAME}</form:option>
									</c:forEach>
								</form:select>
							</div>
						</div>
					</div>
				</div>
				<div role="tabpanel" class="tab-pane col-md-6" id="field-choose"
					aria-labelledby="field-choose-tab">
					<div id="leftDiv" class="col-md-5">
						<div id="leftTitle">
							<label for="keyword" class="control-label">待选人员</label> <input
								type="text" class="form-control" id="keyword"
								placeholder="请输入关键字" oninput="search(event)">
						</div>
						<div>
							<ul class="list-group sortable down" id="allul"
								style="height: 330px; position: relative; overflow: auto">
								<c:forEach items="${pPerson}" var="item">
									<li class="list-group-item" id="${item.ID}"
										onclick="liclick(this);" onmousedown="lidown(this);"
										onmousemove="limove(this);" onmouseup="liup(this);">${item.NAME}</li>
								</c:forEach>
							</ul>
						</div>
					</div>
					<div id="middleDiv" class="col-md-2" style="margin-top: 60px;">
						<button id="pushright" type="button"
							class="btn btn-default fa fa-angle-double-right"></button>
						<div style="height: 20px;"></div>
						<button id="pushleft" type="button"
							class="btn btn-default fa fa-angle-double-left"></button>
					</div>
					<div id="rightDiv" class="col-md-5">
						<div id="rightTitle">
							<label for="keyword" class="col-md-12 control-label">已选人员</label>
							<input type="text" class="form-control" id="choosed-keyword"
								placeholder="请输入关键字" oninput="search(event)">
						</div>
						<div>
							<ul class="list-group sortable choosedField down" id="selectul"
								style="height: 330px; position: relative; overflow: auto">
								<c:forEach items="${sPerson}" var="item">
									<li class="list-group-item" id="${item.ID}"
										onclick="liclick(this);" onmousedown="lidown(this);"
										onmousemove="limove(this);" onmouseup="liup(this);">${item.NAME}</li>
								</c:forEach>
							</ul>
						</div>
					</div>
				</div>
			</div>
		</form:form>
	</div>
	<script>
		$('.down').each(function() {
			new PerfectScrollbar(this);
		});
		layui.use([ 'layer', 'form', 'layedit', 'laydate' ], function() {
			var form = layui.form, layer = layui.layer, layedit = layui.layedit, laydate = layui.laydate;
		});
		$(function() {
			$("html,body").css("cssText", "height:100% !important");
		})
	</script>
	<script type="text/javascript"
		src="${ctxStatic}/prss/arrange/classGroupForm.js"></script>

</body>
</html>