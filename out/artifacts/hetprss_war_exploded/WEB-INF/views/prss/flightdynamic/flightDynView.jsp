<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/prss/plan/css/longTermPlanForm.css" rel="stylesheet" />
<style>
.form-horizontal label{
	padding-left:0px;
	padding-right:0px;
}
</style>
<script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/flightDynView.js"></script>
</head>
<body>
	<input id="infltid" type="text" style="display: none" value="${infltid}">
	<input id="outfltid" type="text" style="display: none" value="${outfltid}">
	<div class="container">
		<form class="form-horizontal">
		  <div class="form-group" style="margin:0px;">
		    <label class="col-sm-1 control-label">进港航班：</label>
		    <div class="col-sm-2">
		      <p id="infltno" class="form-control-static"></p>
		    </div>
		    <label class="col-sm-1 control-label">出港航班：</label>
		    <div class="col-sm-2">
		      <p id="outfltno" class="form-control-static"></p>
		    </div>
		    <label class="col-sm-1 control-label" style="width:102px;">外航/国内标识：</label>
		    <div class="col-sm-2" style="width:16%">
		      <p id="alnFlag" class="form-control-static"></p>
		    </div>
		    <label class="col-sm-1 control-label">航站楼：</label>
		    <div class="col-sm-2">
		      <p id="terminal" class="form-control-static"></p>
		    </div>
		    <label class="col-sm-1 control-label">进港性质：</label>
		    <div class="col-sm-2">
		      <p id="inproperty" class="form-control-static"></p>
		    </div>
		    <label class="col-sm-1 control-label">出港性质：</label>
		    <div class="col-sm-2">
		      <p id="outproperty" class="form-control-static"></p>
		    </div>
		  </div>
		</form>
	</div>
	<div id="createDetailTables">
		<table id="createDetailTable"></table>
	</div>
</body>
</html>