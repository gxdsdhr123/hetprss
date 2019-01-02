<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>人员资质区域</title>
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<style type="text/css">
.bg-color-grey {
	background-color: #eee;
}
.myrow,.myrow > div {
	height:100%;
}
.list-group{
	height:calc(100% - 42px);
}
</style>
<script type="text/javascript"
	src="${ctxStatic}/prss/aptitude/aptitudeLimitsList.js"></script>
</head>
<body>
	<div class="row myrow">
		<div id="leftDiv" class="col-xs-3"
			style="padding-right: 0px;">
			<label class="list-group-item">保障类型</label>
			<div class="list-group sortable" style="overflow: auto">
				<c:forEach items="${offices }" var="offices">
					<button type="button" data-id="${offices.ID }"
						class="list-group-item" onclick="clickOffice(this)">${offices.NAME }</button>
				</c:forEach>
			</div>
		</div>
		<div id="rightDiv" class="col-xs-9" style="padding-left: 0px;">
			<div id="toolbar">
				<button id="setBtn" type="button" class="btn btn-link">部门属性</button>
				<button id="addBtn" type="button" class="btn btn-link">新增区域</button>
				<button id="editBtn" type="button" class="btn btn-link">修改区域</button>
				<button id="delBtn" type="button" class="btn btn-link">删除区域</button>
			</div>
			<table id="aptitudeGrid"></table>
		</div>
	</div>

</body>
</html>