<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>人员作业资质</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<style type="text/css">
	.layui-form-checkbox .layui-icon {
		color:#fff !important;
	}
	.layui-disabled:not(.layui-form-checked) {
		opacity:0.3;
	}
</style>
</head>
<body>
	<div style="padding-top: 5px;" >
		<div style="float: left;">
			<button id="refreshBtn" type="button" class="btn btn-link">刷新</button>
		</div>
		<div style="float: left;padding-left:10px">
			 <input name="title" id="searchName" value="" style="width:150px" placeholder="请输入" class="layui-input" type="text">
		</div>
		<button id="searchBtn" type="button" class="btn btn-link">查询</button>
		<button id="modifyBtn" type="button" class="btn btn-link">修改</button>
		<button id="saveBtn" type="button" class="btn btn-link">保存</button>
		<button id="deleteBtn" type="button" class="btn btn-link">删除</button>
	</div>
	<div id="createDetailTableDiv">
		<form class="layui-form" action="" style="padding:0px !important">
			<table id="createDetailTable"></table>
		</form>
	</div>
	
<script type="text/javascript" src="${ctxStatic}/prss/aptitude/aptitudeInfoList.js"></script>
</body>
</html>