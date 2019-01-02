<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>员工工作组维护</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/head.jsp"%>
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/prss/arrange/css/classGroupForm.css" rel="stylesheet" />
<script type="text/javascript">
	var aPerson = '${aPerson}';
	var sPerson = '${sPerson}';
	var pPerson = '${pPerson}';
	var lPerson = '${lPerson}';
</script>
</head>
<body>
	<div class="content">
		<div class="row">
			<div class="col-md-6">
				<form:form id="createForm" class="layui-form" modelAttribute="wgEntity"
					action="${ctx}/arrange/workGroup/save" enctype="multipart/form-data" method="post">
					<form:input path="teamId" style="display: none" id="teamId" type="text" />
					<form:input path="sPerson" id="sPerson" style="display: none" type="text" />
					<div class="col-md-2"></div>
					<div class="col-md-2">
						<label class="layui-form-label">
							组长：
							<font class="required">*</font>
						</label>
					</div>

					<div class="col-md-5">
						<form:select path="leaderId" lay-verify="" lay-search="true">
							<form:option value="">请选择</form:option>
							<form:option value="">请选择</form:option>
							<c:forEach items="${pPerson}" var="item">
								<form:option value="${item.id}">${item.text}</form:option>
							</c:forEach>
						</form:select>
					</div>
					<div class="layui-form-item">
						<div class="col-md-2"></div>
						<div class="col-md-2">
							<label class="layui-form-label">
								组号：
							<!-- 	<font class="required">*</font> -->
							</label>
						</div>

						<div class="col-md-5">
							<form:input class="layui-input" path="teamName" value="${wgEntity.teamName}" id="teamName" type="text" />
						</div>
					</div>
				</form:form>
			</div>
			<div role="tabpanel" class="tab-pane col-md-6" id="field-choose"
				aria-labelledby="field-choose-tab">
				<div id="leftDiv" class="col-md-5">
					<div id="leftTitle">
						<label for="keyword" class="control-label">待选人员</label>
						<input type="text" class="form-control" id="keyword" placeholder="请输入关键字"
							oninput="search(event)">
					</div>
					<div>
						<ul class="list-group sortable down" id="allul"
							style="height: 250px; position: relative; overflow: auto">
							<c:forEach items="${pPerson}" var="item">
								<li class="list-group-item" id="${item.id}" onclick="liclick(this);"
									onmousedown="lidown(this);" onmousemove="limove(this);" onmouseup="liup(this);">${item.text}</li>
							</c:forEach>
						</ul>
					</div>
				</div>
				<div id="middleDiv" class="col-md-2" style="margin-top: 60px;">
					<button id="pushright" type="button" class="btn btn-default fa fa-angle-double-right"></button>
					<div style="height: 20px;"></div>
					<button id="pushleft" type="button" class="btn btn-default fa fa-angle-double-left"></button>
				</div>
				<div id="rightDiv" class="col-md-5">
					<div id="rightTitle">
						<label for="keyword" class="col-md-12 control-label">已选人员</label>
						<input type="text" class="form-control" id="choosed-keyword" placeholder="请输入关键字"
							oninput="search(event)">
					</div>
					<div>
						<ul class="list-group sortable choosedField down" id="selectul"
							style="height: 250px; position: relative; overflow: auto">
							<c:forEach items="${sPerson}" var="item">
								<li class="list-group-item" id="${item.id}" onclick="liclick(this);"
									onmousedown="lidown(this);" onmousemove="limove(this);" onmouseup="liup(this);">${item.text}</li>
							</c:forEach>
						</ul>
					</div>
				</div>
			</div>
		</div>
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
	<script type="text/javascript" src="${ctxStatic}/prss/arrange/workGroupForm.js"></script>

</body>
</html>