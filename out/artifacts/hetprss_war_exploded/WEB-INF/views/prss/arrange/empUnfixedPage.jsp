<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/prss/arrange/css/empPlanAdd.css" rel="stylesheet" />
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<link href="${ctxStatic}/jquery/plugins/jquery-ui/jquery-ui.min.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<title>生成非固定班制计划</title>
</head>
<body>
	<div class="unfixed-div" id="showUnfixedDiv">
		<div class="layui-form-item" id="searchDiv">			
			<div id="toolbar">
				<div class="layui-inline" >
					<label class="layui-form-label unfixed-lable">开始日期</label>
					<div class="layui-input-inline" style="width:120px;">
						<div class="input-group">
							<input type='text' maxlength="20" id="timeStart"
								placeholder='请选择日期' class='layui-input' value=""
								onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'%y-%M-{%d}',maxDate:'#F{$dp.$D(\'timeEnd\')}'});" />
						</div>
					</div>
				</div>
				<div class="layui-inline" >
					<label class="layui-form-label unfixed-lable">结束日期</label>
					<div class="layui-input-inline" style="width:120px;">
						<div class="input-group">
							<input type='text' maxlength="20" id="timeEnd"
								placeholder='请选择日期' class='layui-input' value=""
								onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'timeStart\')}'});" />
						</div>
					</div>
				</div>
				<div class="layui-inline" style="float:right; margin-right: 12px;">
					<button id="addBtn" class="btn btn-default " type="button">新建</button>
					<button id="modifyBtn" class="btn btn-default " type="button">修改</button>
					<button id="deleteBtn" class="btn btn-default " type="button">删除</button>
				</div>
			</div>
		</div>
		<div style="padding-top:5px">
			<table id="baseTable" class="table table-striped"></table>
		</div>
		<div class="layui-form-item" style="float:right;padding-top:5px">
			<button id="makePlanBtn" class="layui-btn layui-btn-normal layui-btn-small" type="button">生成计划</button>
			<button id="closeBtn" class="layui-btn layui-btn-primary layui-btn-small" type="button">关闭</button>
		</div>
	</div>
	<div class="container" id="contentDiv" style="display:none">
		<div id="leftDiv">
			<div class="layui-form-item">
			    <button id="teamBtn" class="btn btn-link" type="button">作业组</button>
			    <button id="workerBtn" class="btn btn-link" type="button">选择人</button>
			</div>
			<ul class="list-group unfixedUL" id="teamInfoUL">
			</ul>
		</div>
		<div id="middleDiv" style="padding:10px;">
		</div>
		<div id="rightDiv">
			<button id="unfixedBtn" class="btn btn-link" type="button">非固定班次类型</button>
			<ul class="list-group unfixedUL shiftsInfoUL" id="shiftsInfoUL">
			</ul>
		</div>
	</div>
	<div class="container" id="teamDiv" style="display:none">
		<div id="leftDiv2">
			<div class="layui-input-inline">
				<label>可选作业组：</label>
				<input class="layui-input" type="text" id="teamSearch" placeholder="输入关键字回车搜索" style="width:200px;display: inline-block;">
			</div>
			<ul class="list-group sortable unfixedUL" id="allTeamUL"></ul>
		</div>
		<div id="middleDiv">
			<button id="pushright" type="button" class="btn btn-default fa fa-angle-double-right"></button>
			<button id="pushleft" type="button" class="btn btn-default fa fa-angle-double-left"></button>
		</div>
		<div id="rightDiv2">
			<div class="layui-input-inline">
				<label>已选作业组</label>
			</div>
			<ul class="list-group sortable unfixedUL chooseTeamFiled" id="selTeamUL">
			</ul>
		</div>
	</div>
	<div class="container" id="workerDiv" style="display:none">
		<form id="filterForm" class="layui-form">
			<div id="leftDiv3" >
				<div class="layui-form-item">
					<div class="layui-input-inline" style="width:120px;">
				      <select name="groupInfo" id="groupInfo" lay-filter="groupSel">
				      	<option value="all">全部</option>
			     	  </select>
			     	</div>
			     	<div class="layui-input-inline">
						<input class="layui-input" type="text" id="personSearch" placeholder="输入关键字回车搜索" style="width:180px;display: inline-block;">
					</div>
				</div>
				<ul class="list-group sortable unfixedUL" id="allWorkerUL">
				</ul>
			</div>
			<div id="middleDiv">
				<button id="pushright2" type="button" class="btn btn-default fa fa-angle-double-right"></button>
				<button id="pushleft2" type="button" class="btn btn-default fa fa-angle-double-left"></button>
			</div>
			<div id="rightDiv3">
				<div class="layui-form-item">
					<label class="layui-form-label">已选员工</label>
				</div>
				<ul class="list-group sortable unfixedUL chooseWorkerFiled" id="selWorkerUL">
				</ul>
			</div>
		</form>
	</div>
	<div class="container" id="unfixedDiv" style="display:none">
		<form id="filterForm" class="layui-form" onkeydown="if(event.keyCode==13){return false;}">
			<div id="leftDiv4" >
				<div class="layui-form-item">
					<label class="layui-form-label" style="width:100px;">可选择组</label>
					<div class="layui-input-inline">
						<input class="layui-input" autocomplete="off" type="text" id="unfixedSearch" placeholder="输入关键字回车搜索" style="width:180px;display: inline-block;">
					</div>
				</div>
				<ul class="list-group sortable unfixedUL" id="allUnfixedUL">
				</ul>
			</div>
			<div id="middleDiv">
				<button id="pushright3" type="button" class="btn btn-default fa fa-angle-double-right"></button>
				<button id="pushleft3" type="button" class="btn btn-default fa fa-angle-double-left"></button>
			</div>
			<div id="rightDiv4">
				<div class="layui-form-item">
					<label class="layui-form-label">已选择组</label>
				</div>
				<ul class="list-group sortable unfixedUL chooseUnfixedFiled" id="selUnfixedUL">
				</ul>
			</div>
		</form>
	</div>
	<script type="text/javascript" src="${ctxStatic}/jquery/plugins/jquery-ui/jquery-ui.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/prss/arrange/empUnfixedPage.js"></script>
</body>
</html>