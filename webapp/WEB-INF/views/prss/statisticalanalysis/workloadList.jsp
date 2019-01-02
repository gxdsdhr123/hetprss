<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>员工工作量统计</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" type="text/css">
<body>
	<div id="container" style="padding-top:30px">
		<form  id="listForm"  action="${ctx}/workload/statistics/exportExcel" method="post">
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">开始日期</label>
					<div class="layui-input-inline" >
						<input id="dateStart" name="dateStart" class="layui-input" type="text" value="${defaultStart}"
						 onfocus="WdatePicker({dateFmt:'yyyyMMdd',maxDate:'#F{$dp.$D(\'dateEnd\')}'})">
					</div>
					<div class="layui-form-mid">结束日期</div>
					<div class="layui-input-inline" >
						<input id="dateEnd" name="dateEnd" class="layui-input" type="text" value="${defaultEnd}"
						 onfocus="WdatePicker({dateFmt:'yyyyMMdd',minDate:'#F{$dp.$D(\'dateStart\')}'})">
					</div>
<!-- 					<div class="layui-form-mid">员工姓名</div> -->
					<div class="layui-input-inline" hidden="" >
						<input type="text" name="name" id="name" class="layui-input"   value=""/>
					</div>
					<shiro:hasPermission name="workload:jobKind">
						<input type="hidden" name="ifJobKind" id="ifJobKind" value="1"/>
						<div class="layui-form-mid">岗位</div>
						<div class="layui-input-inline" >
							<input name="jobKindName" type="hidden"/>
							<select id="jobKind" name="jobKind" class="form-control" onchange="changeJobKind(this)">
						         <option value="">请选择岗位</option>
						         <c:forEach items="${jobKinds}" var="item">
						          <option  value="${item.id}" >${item.text}</option>
						         </c:forEach>
							 </select>
						</div>
					</shiro:hasPermission>
				</div>
				<div class="layui-inline  pull-right" id="toolbar">
					<div class="layui-input-inline"  >
					<button id="btnSubmit" class="layui-btn layui-btn-small layui-btn-primary search" type="button">
					 	<i class="fa fa-search">&nbsp;</i>查询
					</button>
					<button id="btnChart" class="layui-btn layui-btn-small layui-btn-primary print"  type="button">
					打印
					</button>
					</div>
					<div class="layui-input-inline"  >
						<input id="searchText" name="searchText" class="layui-input" type="text" placeholder="搜索">
					</div>
				</div>
			</div>
		</form>
		

		<div id="baseTables" style="width:100%;">
			<table id="baseTable"></table>
		</div>
	</div>
	<script type="text/javascript" src="${ctxStatic}/prss/statisticalanalysis/workloadList.js"></script>
</body>
</html>