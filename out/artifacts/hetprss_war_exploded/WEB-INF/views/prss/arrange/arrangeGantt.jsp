<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>排班管理（甘特图）</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<link href="${ctxStatic}/prss/flightdynamic/css/flightDynList.css" rel="stylesheet" />
<style type="text/css">
#filterBox .row{
	padding:15px;
	margin:0px;
}
#filterBox label{
	line-height: 34px;
	text-align: right;
}
/* .layui-layer-page{
	overflow:hidden;
} */
</style>
<script type="text/javascript" src="${ctxStatic}/jquery/plugins/SJgantt/SJgantt-ar.js"></script>
<body>
	<input id="loginName" type="hidden" value="${fns:getUser().loginName}">
	<input type="hidden" id="schemaId" value="${schemaId}">
	<div id="tool-box">
		<button id="refresh" type="button" class="btn btn-link">刷新</button>
		<button id="filter" type="button" class="btn btn-link">筛选</button>
		<button id="setGroup" type="button" class="btn btn-link">设置分组</button>
	</div>
	<canvas id="SJgantt" class="SJgantt"></canvas>
	<div id="filterBox" style="display:none">
		<div class="row">
			<label for="inputEmail3" class="col-sm-2 control-label">航站楼</label>
		    <div class="col-sm-4">
		      <select id="terminal" class="form-control">
		        <option value=""></option>
				<option value="1">T1</option>
				<option value="2">T2</option>
				<option value="3">T3</option>
			  </select>
		    </div>
		    <label for="inputPassword3" class="col-sm-2 control-label">班期</label>
		    <div class="col-sm-4">
			    <select id="week" class="form-control">
			      <option value=""></option>
				  <option value="1">星期一</option>
				  <option value="2">星期二</option>
				  <option value="3">星期三</option>
				  <option value="4">星期四</option>
				  <option value="5">星期五</option>
				  <option value="6">星期六</option>
				  <option value="7">星期日</option>
				</select>
		    </div>
		</div>
	</div>
	<script>
		$("body").css("overflow","hidden");
	</script>
	<script type="text/javascript" src="${ctxStatic}/prss/arrange/arrangeGantt.js"></script>
</body>
</html>