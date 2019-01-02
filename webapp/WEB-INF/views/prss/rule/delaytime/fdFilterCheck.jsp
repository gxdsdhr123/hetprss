<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title></title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/prss/flightdynamic/css/flightDynList.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/prss/rule/delaytime/fdFilterCheck.js"></script>
<script>
	var items = ${items};
</script>
</head>
<body>
	<input id="type" style="display: none;" value="${type}">
	<input id="selectedId" style="display: none;" value="${selectedId}">
	<input id="selectedText" style="display: none;" value="${selectedText}">
	
	<form id="filterCheck">
		<div class="col-md-12 col-sm-12 content" id="allItems">
			<div id="leftDiv" class="col-md-5 col-sm-5">
				<div id="leftTitle">
					<label for="keyword" class="control-label">所有项</label>
					<input type="text" class="form-control" id="keyword" placeholder="请输入关键字"
						oninput="searchWord(this.value)">
				</div>
				<div>
					<ul class="list-group sortable down" id="allul"
						style="height: 240px; position: relative; overflow: auto">
					</ul>
				</div>
			</div>
			<div id="middleDiv" class="col-md-2 col-sm-2" style="margin-top: 60px;">
				<button id="pushright" type="button" class="btn btn-default fa fa-angle-double-right"></button>
				<div style="height: 20px;"></div>
				<button id="pushleft" type="button" class="btn btn-default fa fa-angle-double-left"></button>
			</div>
			<div id="rightDiv" class="col-md-5 col-sm-5">
				<div id="rightTitle">
					<label for="keyword" class="col-md-12 control-label">已选项</label>
					<input type="text" class="form-control" id="choosed-keyword" placeholder="请输入关键字"
						oninput="search(event)">
				</div>
				<div>
					<ul class="list-group sortable choosedField down" id="selectul"
						style="height: 240px; position: relative; overflow: auto">
						<c:forEach items="${sitems}" var="item">
							<li class="list-group-item" id="${item.id}" onclick="liclick(this);"
								onmousedown="lidown(this);" onmousemove="limove(this);" onmouseup="liup(this);">${item.text}</li>
						</c:forEach>
					</ul>
				</div>
			</div>
		</div>

	</form>
</body>
</html>