<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>次日计划编辑</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<link href="${ctxStatic}/prss/plan/css/planMain.css" rel="stylesheet" />
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${ctxStatic}/prss/plan/tomorrowPlanForm.js"></script>
<script type="text/javascript" src="${ctxStatic}/prss/plan/tomorrowPlanCommon.js"></script>
<script type="text/javascript">
var airportCodeSource = ${airportCodeSource};
var airlinesCodeSource = ${airlinesCodeSource};
var propertyCodeSource = ${propertyCodeSource};
/* var aircraftNumberSource = ${aircraftNumberSource}; */
var actTypeSource = ${actTypeSource};
var aptAttrCode = ${aptAttrCode};
var currentAirport = "${currentAirport}";
</script>
</head>
<body>
	<input type="hidden" id="isNew" value="${isNew}">
	<input type="hidden" id="fltDate" value="${fltDate}">
	<input type="hidden" id="fltNo" value="${fltNo}">
	<input type="hidden" id="ioType" value="${ioType}">
	<input type="hidden" id="ids" value="${ids}">
	
<!-- 临时隐藏，后续实现功能 TODO -->
	<div id="tool-box" align="left" style="display: none;">
	   <button id="addAirCraft" type="button" class="btn btn-link">
<!--            <i class="fa fa-cloud-upload">&nbsp;</i> -->
                 添加机号
       </button>
       <button id="addActType" type="button" class="btn btn-link">
<!--            <i class="fa fa-cloud-upload">&nbsp;</i> -->
                 添加机型
       </button>
       <button id="addAirport" type="button" class="btn btn-link">
<!--            <i class="fa fa-cloud-upload">&nbsp;</i> -->
                 添加机场
       </button>
       <button id="addAln" type="button" class="btn btn-link">
<!--            <i class="fa fa-cloud-upload">&nbsp;</i> -->
                 添加航空公司
       </button>
	</div>
	<div id="baseTables">
		<table id="baseTable"></table>
	</div>
</body>
</html>