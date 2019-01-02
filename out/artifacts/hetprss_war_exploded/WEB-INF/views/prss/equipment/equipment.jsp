<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>资源管理</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/treeview.jsp"%>
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<%@include file="equipmentFilter.jsp"%>
<%@include file="equipmentAdd.jsp"%>
<%@include file="equipmentZtreeNewAdd.jsp"%>
<%-- <%@include file="equipmentZtreeRevamp.jsp"%> --%>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/fullcalendar/fullcalendar.min.css"/>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<link href="${ctxStatic}/prss/arrange/css/empPlanList.css" rel="stylesheet" />
<style type="text/css">
.ps__thumb-x{
	bottom:-5px;
}


.ztree li{
	margin:-4px;
}

.layui-form-select {
    display: none; 
}

.ztree li a, .ztree li a span{
	/* color:#999999; */
}

.layui-form-label {
    width: 107px;
}

/* .layui-form-label {
    width: 86px;
} */
.layui-input, .layui-textarea {
	width:99%;
}
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
</style>
<style>
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
</style>
<style>
.ps__rail-x, .ps__rail-y {
	opacity: 0.6;
}
</style>
</head>
<body>
	<div class="row" id="douxf">
		<input type="hidden" name="typeId" id = "typeId" value="0"/>
		<input type="hidden" name="typeName" id = "typeName" value="0"/>
		<div class="col-md-3 col-xs-3 col-sm-3">
			<div class="box box-widget">
				<div class="box-header with-border" id="header">
					<h6 class="box-title">设备类型</h6>
				</div>
				<div class="box-body" style="height: 470px;padding: 10px 0px 0px 0px; width: 100%;" id="container">
					<div id="ztree" class="ztree content" style="width: 100%;height: 100%;"></div>
				</div>
				<script>
					new PerfectScrollbar('#container');
				</script>
			</div>
		</div>
		<div class="col-md-9 col-xs-9 col-sm-9" style="padding-left: 0px;">
			<div class="box box-widget">
				<div class="box-header with-border">
					<h6 class="box-title">设备列表</h6>
				</div>
					<button id="batchAddBtn" type="button" class="btn btn-link">
						<i class="fa fa-filter">&nbsp;</i>筛选
					</button>
	    			<button id="batchAddBtnNew" type="button" class="btn btn-link">
							<i class="fa fa-plus">&nbsp;</i>新增
					</button>
	    			<button id="batchAddBtnRevamp" type="button" class="btn btn-link">
							<i class="fa fa-edit">&nbsp;</i>修改
					</button>
	    			<button id="batchAddBtnDel" type="button" class="btn btn-link">
						<i class="fa fa-trash">&nbsp;</i>删除
					</button>
				<div class="box-body">
					<div id="mTables">
						<table id="mTable"></table>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="${ctxStatic}/prss/equipment/equipment.js"></script>
</body>
</html>
