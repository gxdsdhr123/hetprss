<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>消息管理</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/treeview.jsp"%>
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<style type="text/css">
.ztree {
	margin: 0;
	_margin-top: 10px;
	padding: 10px 0 0 10px;
}
.box{
	margin: 0px;
}
.btn{
	padding:6px ;
}
#container {
	position: relative;
	margin: 0px auto;
	padding: 0px;
	width: 190px;
	height: 160px;
	overflow-y: auto;
}

#container .content {
	background-image: url('./assets/azusa.jpg');
	width: 190px;
	height: 220px;
}
.ps__rail-x, .ps__rail-y {
	opacity: 0.6;
}
</style>
</head>
<body>
	<div class="row">
	<input type="hidden" name="typeId" id = "typeId" value="0"/>
		<div class="col-md-2 col-xs-2 col-sm-2">
			<div class="box box-widget">
				<div class="box-header with-border" id="header">
					<h6 class="box-title">模板类型</h6>
					<button id="addType" type="button" class="btn btn-link"><i class="fa fa-plus">&nbsp;</i></button>
					<button id="updType" type="button" class="btn btn-link"><i class="fa fa-edit">&nbsp;</i></button>
					<button id="delType" type="button" class="btn btn-link"><i class="fa fa-trash">&nbsp;</i></button>
				</div>
				<div class="box-body" style="height: 470px;padding: 10px 0px 0px 0px;" id="container">
					<div id="ztree" class="ztree content" style="width: 100%;height: 100%;"></div>
				</div>

				<script>
					new PerfectScrollbar('#container');
				</script>
			</div>
		</div>
		<div class="col-md-10 col-xs-10 col-sm-10" style="padding-left: 0px;">
			<div class="box box-widget">
				<div class="box-header with-border">
					<h6 class="box-title">模板列表</h6>
					<button id="addTempl" type="button" class="btn btn-link"><i class="fa fa-plus">&nbsp;</i></button>
<!-- 					<button id="updTempl" type="button" class="btn btn-link"><i class="fa fa-edit">&nbsp;</i></button> -->
					<button id="delTempl" type="button" class="btn btn-link"><i class="fa fa-trash">&nbsp;</i></button>
				</div>
				<div class="box-body">
					<div id="mTables">
						<table id="mTable"></table>
					</div>
				</div>
			</div>
		</div>
	</div>

	<script type="text/javascript" src="${ctxStatic}/prss/message/messageManager.js"></script>
</body>
</html>