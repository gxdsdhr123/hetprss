<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>停靠机型规则</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>

<body>
	<div>
<!-- 		<div id="leftDiv" class="col-xs-3" -->
<!-- 			style="padding-right: 0px;"> -->
<!-- 			<label class="list-group-item">保障类型</label> -->
<!-- 			<div class="list-group sortable" style="overflow: auto"> -->
<%-- 				<c:forEach items="${offices }" var="offices"> --%>
<%-- 					<button type="button" data-id="${offices.ID }" --%>
<%-- 						class="list-group-item" onclick="clickOffice(this)">${offices.NAME }</button> --%>
<%-- 				</c:forEach> --%>
<!-- 			</div> -->
<!-- 		</div> -->
		<div  class="col-xs-12" style="padding-left: 0px;">
			<div id="toolbar">
				<button id="addBtn" type="button" class="btn btn-link">新增</button>
				<button id="editBtn" type="button" class="btn btn-link">修改</button>
				<button id="delBtn" type="button" class="btn btn-link">删除</button>
			</div>
			<table id="baseTable"></table>
		</div>
	</div>
	<script type="text/javascript" src="${ctxStatic}/prss/stand/parkingMutexList.js"></script>
</body>
</html>