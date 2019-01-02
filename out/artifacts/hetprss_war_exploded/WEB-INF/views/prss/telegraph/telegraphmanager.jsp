<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>报文管理</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/treeview.jsp"%>
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>

<style type="text/css">
.ztree {
	margin: 0;
	_margin-top: 10px;
	padding: 0px 0px 0px 0px;
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
	  <input type="hidden" id="typeId" name="typeId">
		<div class="col-md-2 col-xs-3 col-sm-3">
			<div class="box box-widget">
				<div class="box-header with-border" id="header">
					<h6 class="box-title">报文分类</h6>
					<button type="button" class="btn btn-link" >
						<i class="fa fa-trash2">&nbsp;</i>
					</button>
				</div>
			</div>
			<div class="box-body" style="height: 470px;padding: 10px 0px 0px 0px;" id="container">
				<div id="ztree" class="ztree content" style="width: 100%;height: 100%;"></div>
			</div>
			<script>
				new PerfectScrollbar('#container');
			</script>
		</div>
		<div class="col-md-10 col-xs-10 col-sm-10" style="padding-left: 0px;">
			<div class="box box-widget">
				<div class="box-header with-border">
					<h6 class="box-title">模板列表</h6>
					<button id="add" type="button" class="btn btn-link">
						<i class="fa fa-plus">&nbsp;</i>
					</button>
					<button id="remove" type="button" class="btn btn-link">
						<i class="fa fa-trash">&nbsp;</i>
					</button>
				</div>
			</div>
			<div class="box-body">
				<table id="baseTable"
					class="table table-striped table-bordered table-hover"></table>
			</div>
		</div>
	</div>
 <script type="text/javascript" src="${ctxStatic}/prss/telegraph/telegraphmanager.js"></script> 
</body>
</html>