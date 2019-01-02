<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>绑定航班</title>
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<style type="text/css">
.list-group-item{
	-moz-user-select: -moz-none;
   	-khtml-user-select: none;
   	-webkit-user-select: none;
   	-ms-user-select: none;
   	user-select: none;
</style>
<script type="text/javascript"
	src="${ctxStatic}/prss/arrange/asFltForm.js"></script>
</head>
<body>
	<div class="content">
		<input type="hidden" name="day" id="day" value="${day}">
		<form:form id="createForm" class="layui-form"
			modelAttribute="cgEntity" enctype="multipart/form-data" method="post">
			<div class="row">
				<div role="tabpanel" class="tab-pane" id="field-choose"
					aria-labelledby="field-choose-tab">
					<div id="leftDiv" class="col-xs-5">
						<div id="leftTitle">
							<label for="keyword" class="control-label">全部航班：</label> <input
								type="text" class="form-control" id="keyword"
								placeholder="请输入关键字" oninput="search(event)">
						</div>
						<div class="sortable" style="height: 180px; overflow: auto">
							<ul class="list-group" id="allul">
								<c:forEach items="${aFltNo}" var="item">
									<li class="list-group-item" id="${item}"
										onclick="liclick(this);" onmousedown="lidown(this);"
										onmousemove="limove(this);" onmouseup="liup(this);">${item}</li>
								</c:forEach>
							</ul>
						</div>
					</div>
					<div id="middleDiv" class="col-xs-2" style="margin-top: 60px;text-align:center">
						<button id="pushright" type="button"
							class="btn btn-default fa fa-angle-double-right"></button>
						<div style="height: 20px;"></div>
						<button id="pushleft" type="button"
							class="btn btn-default fa fa-angle-double-left"></button>
					</div>
					<div id="rightDiv" class="col-xs-5">
						<div id="rightTitle">
							<label for="keyword" class="control-label">已选航班：</label>
							<input type="text" class="form-control" id="choosed-keyword"
								placeholder="请输入关键字" oninput="search(event)">
						</div>
						<div class="sortable" style="height: 180px; overflow: auto">
							<ul class="list-group choosedField" id="selectul">
								<c:forEach items="${sFltNo}" var="item">
									<li class="list-group-item" id="${item}"
										onclick="liclick(this);" onmousedown="lidown(this);"
										onmousemove="limove(this);" onmouseup="liup(this);">${item}</li>
								</c:forEach>
							</ul>
						</div>
					</div>
				</div>
			</div>
		</form:form>
	</div>
</body>
</html>