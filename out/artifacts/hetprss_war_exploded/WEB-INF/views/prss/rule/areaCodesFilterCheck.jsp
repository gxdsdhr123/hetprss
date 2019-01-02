<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title></title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript" src="${ctxStatic}/prss/rule/areaCodesFilterCheck.js"></script>
</head>
<body>
	<input id="selectedId" style="display: none;" value="${selectedId}">
	<input id="selectedText" style="display: none;" value="${selectedText}">
	
	<form id="filterCheck">
		<table style="text-align:center;margin:10px;">
			<tr>
				<!-- 未选列表 -->
				<td>
					<div id="leftDiv" style="width: 200px">
						<div id="leftTitle">
							<label for="keyword" class="control-label">所有项</label>
							<input type="text" class="form-control" id="keyword" placeholder="请输入关键字"
								oninput="search(event)">
						</div>
						<div>
							<ul class="list-group sortable down" id="allul"
								style="height: 200px;">
								<c:forEach items="${unSelectedGisRailInfo}" var="item">
									<li class="list-group-item" id="${item.areaCode}" onclick="liclick(this);" name="${item.areaCodeName}"
										onmousedown="lidown(this);" onmousemove="limove(this);" onmouseup="liup(this);">${item.areaName}</li>
								</c:forEach>
							</ul>
						</div>
					</div>
				</td>
				<!-- 左右移动按钮 -->
				<td>
					<div id="middleDiv" style="margin-top: 60px;margin-left: 35px;width: 80px">
						<button id="pushright" type="button" class="btn btn-default fa fa-angle-double-right"></button>
						<div style="height: 20px;"></div>
						<button id="pushleft" type="button" class="btn btn-default fa fa-angle-double-left"></button>
					</div>
				</td>
				<!-- 已选列表 -->
				<td>
					<div id="rightDiv" style="width: 200px">
						<div id="rightTitle">
							<label for="keyword" class="control-label">已选项</label>
							<input type="text" class="form-control" id="choosed-keyword" placeholder="请输入关键字"
								oninput="search(event)">
						</div>
						<div>
							<ul class="list-group sortable choosedField down" id="selectul"
								style="height: 200px;">
								<c:forEach items="${selectedGisRailInfo}" var="item">
									<li class="list-group-item" id="${item.areaCode}" onclick="liclick(this);" name="${item.areaCodeName}"
										onmousedown="lidown(this);" onmousemove="limove(this);" onmouseup="liup(this);">${item.areaName}</li>
								</c:forEach>
							</ul>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>