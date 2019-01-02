<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default" />
<title>区域设置</title>
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/treeview.jsp"%>
<style type="text/css">
ul.list-group li{
	padding: 10px 15px;
	margin-bottom: -1px;
}
.search{
	padding: 5px 15px;
}
</style>
</head>
<body>
	<input id="id" type="hidden" value="${id}">
	<input id="type" type="hidden" value="${type}">
	<form id="aptitudeForm" action="" class="layui-form" style="padding-bottom:0px !important">
		<input type="hidden" name="id" id="id">
 		<div class="layui-form-item">
			<div class="layui-inline">
				<label class="layui-form-label">区域名称：</label>
				<div class="layui-input-inline">
					<input type="text" autocomplete="off" class="layui-input"
						name="name" id="name" value="${name}">
				</div>
			</div>
			<div class="layui-inline">
				<label class="layui-form-label">区域属性：</label>
				<div class="layui-input-inline">
					<input type="radio" name="att" value="0" title="机位" lay-filter="area" <c:if test='${type eq "0"}'>checked</c:if>>
					<input type="radio" name="att" value="1" title="机型" lay-filter="area" <c:if test='${type eq "1"}'>checked</c:if>>
				</div>
			</div>
		</div>
		<input type="hidden" name="info" id="info" value="">
	</form>
	<div class="content" style="padding:0px 15px;height:calc(100% - 63px)">
		<div class="row" style="height:100%">
			<div id="leftDiv" class="col-xs-5" style="height:100%">
				<div id="leftTitle">
					<label for="keyword" class="control-label">全部机位</label> 
				</div>
				<div id="aztree" class="ztree" style="height:260px;position:relative"></div>
			</div>
			<div id="middleDiv" class="col-xs-2" style="margin-top: 90px;text-align:center">
				<button id="pushright" type="button"
					class="btn btn-default fa fa-angle-double-right"></button>
				<div style="height: 20px;"></div>
				<button id="pushleft" type="button"
					class="btn btn-default fa fa-angle-double-left"></button>
			</div>
			<div id="rightDiv" class="col-xs-5" style="height:100%">
				<div id="rightTitle">
					<label for="keyword" class="control-label">已选机位</label>
				</div>
				<div id="sztree" class="ztree" style="height:260px;position:relative"></div>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="${ctxStatic}/prss/scheduling/schedulingAreaOperation.js"></script>
</body>
</html>