<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>表格元数据管理</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/treeview.jsp"%>
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript"
	src="${ctxStatic}/prss/gridmetadata/gridMetaDataList.js"></script>
<style type="text/css">
.ztree {
	overflow: auto;
	margin: 0;
	_margin-top: 10px;
	padding: 10px 0 0 10px;
	position: relative;
}
</style>	
<body>
	<div class="row">
		<div class="col-md-3 col-xs-3 col-sm-3">
			<div class="box box-widget">
				<div class="box-header with-border">
					<h5 class="box-title">模块信息</h5>
				</div>
				<div class="box-body">
					<div id="ztree" class="ztree"></div>
				</div>
			</div>
		</div>
		<div class="col-md-9 col-xs-9 col-sm-9" style="padding-left: 0px;">
			<div class="box box-widget">
				<div class="box-header with-border">
					<h5 class="box-title">表信息</h5>
				</div>
				<div class="box-body" id="tableList" style="border: 2px #006DC0 solid">
					<table id="dataGrid" class="table table-hover table-striped">
					</table>
				</div>
			</div>
		</div>
	</div>
</body>
</html>