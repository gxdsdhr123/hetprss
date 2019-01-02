<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>车辆保障设置</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<%@include file="/WEB-INF/views/include/treeview.jsp"%>
</head>
<body>
	<div id="tool-box"></div>
	<table id="settingTable"></table>
	<div id="showArea" style="height:100%;padding:5px 20px;display:none">
		<div class="col-sm-6">
			<label>机位区域</label>
			<div id="showTree" class="ztree" style="height:235px;position:relative"></div>
		</div>
		<div class="col-sm-6">
			<label>默认机位区域</label>
			<div id="showTreeDef" class="ztree" style="height:235px;position:relative"></div>
		</div>
	</div>
	<div id="editArea" style="height:100%;padding:5px 20px;display:none">
		<form class="form-inline">
		  <div class="form-group">
		    <label for="areas">机位区域</label>
		    <select class="form-control" id="areas" style="width:200px;margin-left:5px">
		    	<option value="!">请选择</option>
		    	<c:forEach items="${areas}" var="area">
		    		<option value="${area.id}">${area.name}</option>
		    	</c:forEach>
		    </select>
		  </div>
		  <div class="form-group" style="margin-left:50px" id="actypeSel">
		    <label for="actypes">机型</label>
		    <select class="form-control" id="actypes" style="width:200px;margin-left:5px">
		    	<option value="!">请选择</option>
		    	<c:forEach items="${actypes}" var="actype">
		    		<option value="${actype.id}">${actype.name}</option>
		    	</c:forEach>
		    </select>
		  </div>
		</form>
		<div id="editTree" class="ztree" style="height:260px;position:relative"></div>
	</div>
	<script type="text/javascript" src="${ctxStatic}/prss/scheduling/schedulingResSet.js"></script>
</body>
</html>