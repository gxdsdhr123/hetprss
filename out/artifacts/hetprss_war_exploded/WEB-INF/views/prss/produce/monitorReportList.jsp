<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>航班监控报告</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<link href="${ctxStatic}/prss/produce/css/monitorReportList.css" rel="stylesheet" />
<script type="text/javascript"	src="${ctxStatic}/prss/produce/monitorReportList.js"></script>
<script type="text/javascript">
	var restypes = ${restypes};
</script>
</head>
<body>
	<div id="toolbar" class="form-inline">
		<label class="form-inline">
			航班日期
			<input class="form-control" id="startDate" onfocus="WdatePicker({dateFmt:'yyyyMMdd',maxDate:'#F{$dp.$D(\'endDate\')}' });"/>
			-
			<input class="form-control" id="endDate"  onfocus="WdatePicker({dateFmt:'yyyyMMdd',minDate:'#F{$dp.$D(\'startDate\')}' });"/>
		</label>
		<input class="form-control" id="searchText" placeholder="请输入"/>
		<button id="searchBtn" type="button" class="btn btn-link">查询</button>
		<button id="newBtn" type="button" class="btn btn-link">生成报告单</button>
		<button id="printBtn" type="button" class="btn btn-link">打印报告单</button>
		<button id="editBtn" type="button" class="btn btn-link">修改</button>
		<button id="delBtn" type="button" class="btn btn-link">删除</button>
	</div>
	<table id="billGrid"></table>
	
	<div id="createView" style="display:none;">
		<form action="" class="form-horizontal">
			<div class="row">
				<div  class="col-sm-6 form-group" >
				 	<label for="username" class="col-sm-4 control-label">日期</label>
				    <div class="col-sm-8">
				      	<input type="text" class="form-control"   id="statDay" onclick="WdatePicker({dateFmt:'yyyyMMdd'});"/>
				    </div>
				 </div>
				 <div  class="col-sm-6 form-group" >
				 	<label for="username" class="col-sm-4 control-label">航班号</label>
				    <div class="col-sm-8">
				      	<input type="text" class="form-control" id="flightNumber"/>
				    </div>
				 </div>
			</div>
			<div class="row">
				<div class="col-sm-5" id="leftDiv">
					<div id="leftTitle">
						<label for="keyword" class="control-label">选择保障任务</label>
						<select class="form-control" id="chooseDept">
						</select>
					</div>
					<ul class="list-group sortable">
					</ul>
				</div>
				<div class="col-sm-2" id="middleDiv">
					<button id="pushright" type="button" class="btn btn-default fa fa-angle-double-right"></button>
						<button id="pushleft" type="button" class="btn btn-default fa fa-angle-double-left"></button>
				</div>
				<div class="col-sm-5" id="rightDiv">
					<div id="rightTitle">
						<label for="keyword" class="control-label">已选任务</label>
					</div>
					<div class="sortable">
						<ul class="list-group choosedField">
						</ul>
					</div>
				</div>
			</div>
		</form>
	</div>
</body>
</html>