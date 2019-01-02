<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>中航信计划</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript"
	src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>

</head>
<body>
	<div id="tool-box">
		<form action="${ctx}/plan/travelskyPlan/getData" id="searchForm">
			<table>
				<tr>

					<td><label class="layui-form-label"
						style="padding: 10px 10px !important; margin-bottom: 0px;">日期</label></td>
					<td><input id="startDate" name="startDate"
						onClick="dateFilter();" class="layui-input" style="width: 100px;"
						value="" type="text"></td>
					<td>&nbsp;-&nbsp;</td>
					<td><input id="endDate" name="endDate" onClick="dateFilter();"
						class="layui-input" style="width: 100px;" value="" type="text"></td>

					<td>
						<button id="search" type="button" class="btn btn-link">查询</button>
						<button id="import" type="button" class="btn btn-link"
							onclick="importFile()">计划导入</button>
						<button id="create" type="button" class="btn btn-link"
							onclick="arrvialAdd()">新增</button>
						<button id="remove" type="button" class="btn btn-link">删除</button>
						<button id="print" type="button" class="btn btn-link">打印</button>
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div class="row">
		<div class="col-xs-6 col-sm-6 col-md-6">
			<table id="arrivalGrid"></table>
		</div>
		<div class="col-xs-6 col-sm-6 col-md-6">
			<table id="departureGrid"></table>
		</div>
	</div>
	<form id="printForm" action="${ctx}/plan/travelskyPlan/print" hidden="">
		<input id="sDate" type="hidden" value=""> 
		<input id="eDate" type="hidden" value="">
	</form>
	<form id="fileList" action="${ctx}/plan/travelskyPlan/importPlan"
		method="post" enctype="multipart/form-data">
		<input id="fileInput" name="file" style="display: none"
			onchange="fileOnChange()" type="file" multiple="multiple">
		<table class="layui-table" style="display: none;" id="fileTable">
			<thead>
				<tr>
					<th>文件名</th>
					<th>大小</th>
				</tr>
			</thead>
			<tbody id="fileTable"></tbody>
		</table>
		<input id="delFile" name="delFile" style="display: none" />
	</form>
	<script type="text/javascript"
		src="${ctxStatic}/prss/plan/travelskyPlanList.js"></script>
</body>
</html>