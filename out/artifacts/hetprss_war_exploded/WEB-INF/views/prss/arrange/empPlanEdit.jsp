<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/jquery/plugins/jquery-ui/jquery-ui.min.css" rel="stylesheet" />
<link href="${ctxStatic}/prss/arrange/css/empPlanAdd.css" rel="stylesheet" />
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<title></title>
<script type="text/javascript">
var dateTp = "${selDate}";
var flag="${flag}";
</script>
</head>
<body>
	<form id="filterForm" class="layui-form">
		<div class="editplan" id="showOrderDiv">
			<ul class="list-group-order sortable" id="orderPlanUL" style="height:300px">
				<c:forEach items="${planInfos}" var="infovo">
					<li class="list-group-item hr" data-code="${infovo.ID}">
						${infovo.NUM}&nbsp;&nbsp;
						${infovo.LOGIN_NAME}&nbsp;&nbsp;
						${infovo.SHIFTS_NAME}&nbsp;&nbsp;
						${infovo.LABEL1}&nbsp;&nbsp;
						${infovo.LABEL2}&nbsp;&nbsp;
						${infovo.LABEL3}
					</li>
				</c:forEach>
			</ul>
		</div>
		<div class="editplan" id="showLogDiv">
			<div class="layui-form-item" id="searchDiv">
				<div class="layui-inline" >
					<label class="layui-form-label">开始日期</label>
					<div class="layui-input-inline">
						<div class="input-group">
							<input type='text' maxlength="20" id="timeStart"
								placeholder='请选择日期' class='layui-input' value="${startDate}"
								onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'timeEnd\')}'});" />
						</div>
					</div>
				</div>
				<div class="layui-inline" >
					<label class="layui-form-label">结束日期</label>
					<div class="layui-input-inline">
						<div class="input-group">
							<input type='text' maxlength="20" id="timeEnd"
								placeholder='请选择日期' class='layui-input' value="${endDate}"
								onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'timeStart\')}'});" />
						</div>
					</div>
				</div>
				<div class="layui-inline" >
					<label class="layui-form-label">员工姓名</label>
					<div class="layui-input-inline">
						<div class="input-group">
							<input type='text' id="empName" class='layui-input' value="${empName}" />
						</div>
					</div>
				</div>
				<div class="layui-inline" id="toolbar" style="float:right;">
					<button id="searchBtn" class="btn btn-default btn2" type="button">查询</button>
				</div>
			</div>
			<div style="padding-top:5px">
				<table id="baseTable" class="table table-striped"></table>
			</div>
		</div>
	</form>
	<script type="text/javascript" src="${ctxStatic}/jquery/plugins/jquery-ui/jquery-ui.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/prss/arrange/empPlanEdit.js"></script>
</body>
</html>