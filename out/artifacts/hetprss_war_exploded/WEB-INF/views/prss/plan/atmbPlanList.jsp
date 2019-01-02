<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript" src="${ctxStatic}/bootstrap/plugins/bootstrap-table/extensions/bootstrap-table-contextmenu.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/prss/plan/atmbPlanList.js"></script>
<title>空管次日计划</title>
<script type="text/javascript">
var servicePublishDate = ${servicePublishDate};
</script>
</head>
<body>
	<div id="tool-box">
		<!-- TODO 临时隐藏，后续需要此功能 -->
		<button id="importBtn" type="button" class="btn btn-link" style="display: none">报文导入</button>
		<!-- TODO 临时导入excel空管计划 -->
		<button id="refresh" type="button" class="btn btn-link">刷新</button>
		<button id="importExcelBtn" type="button" class="btn btn-link">报文导入</button>
		<button id="addBtn" type="button" class="btn btn-link">新增</button>
		<button id="publishBtn" type="button" class="btn btn-link">发布</button>
		<button id="delBtn" type="button" class="btn btn-link">删除</button>
		<button id="printBtn" type="button" class="btn btn-link">打印</button>
		<!-- 检索功能 -->
		<div class="btn-group">
			<a class="btn btn-default dropdown-toggle" data-toggle="dropdown" id="searchOptBtn" aria-expanded="false">
				不限
				<span class="fa fa-caret-down"></span>
			</a>
			<ul class="dropdown-menu">
					<li>
					<a href="#" onclick="searchOpt('fltNo',this)">航班号</a>
				</li>
				<li>
					<a href="#" onclick="searchOpt('aircraftNumber',this)">机号</a>
				</li>
				<li>
					<a href="#" onclick="searchOpt('actType',this)">机型</a>
				</li>
				<li>
					<a href="#" onclick="searchOpt('airports',this)">起场</a>
				</li>
				<li>
					<a href="#" onclick="searchOpt('airports',this)">落场</a>
				</li>
				<li>
					<a href="#" onclick="searchOpt('',this)">不限</a>
				</li>
			</ul>
		</div>
		<div class="layui-input-inline">
			<input id="searchTab" type="text" class="layui-input" style="border:none;">
		</div>
	</div>
	<div class="row">
		<div class="col-xs-6 col-sm-6 col-md-6">
			<table id="inFltGrid"></table>
		</div>
		<div class="col-xs-6 col-sm-6 col-md-6">
			<table id="outFltGrid"></table>
		</div>
	</div>
	<ul id="context-menu" class="dropdown-menu">
		<li data-item="msgView">
			<a>查看报文</a>
		</li>
	</ul>
	<iframe style="display: none" id="hiddenFrame"></iframe>
	<form id="fileForm" action="${ctx}/plan/atmbPlan/importExcelPlan" method="post" style="display: none;" enctype="multipart/form-data">
        <input id="fileInput" name="file" type="file" multiple="multiple" style="display: none;" onchange="fileOnChange();">
    </form>
    <div id="errMsg" style="display: none;overflow-x: auto; overflow-y: auto; height: 360px; width:600px;" ></div>
</body>
</html>