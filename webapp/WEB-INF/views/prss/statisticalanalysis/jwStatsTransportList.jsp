<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>机务航班运输统计</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" type="text/css">
<body>
	<div id="container" style="padding-top:30px">
		<form  id="listForm"  action="${ctx}/jwys/statistics/exportExcel" method="post">
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">开始日期</label>
					<div class="layui-input-inline" >
						<input id="dateStart" name="dateStart" class="layui-input" type="text" value="${defaultStart}"
						 onfocus="WdatePicker({dateFmt:'yyyyMMdd'})">
					</div>
					<div class="layui-form-mid">结束日期</div>
					<div class="layui-input-inline" >
						<input id="dateEnd" name="dateEnd" class="layui-input" type="text" value="${defaultEnd}"
						 onfocus="WdatePicker({dateFmt:'yyyyMMdd'})">
					</div>
					<div class="layui-input-inline"  >
						<select id="alnCode" name="alnCode" class="form-control" >
					         <option value="">请选择航空公司</option>
					         <c:forEach items="${airlines}" var="item">
					          <option  value="${item.id}" >${item.text}</option>
					         </c:forEach>
						 </select>
					</div>
					
				</div>
				<div class="layui-inline  pull-right" id="toolbar">
					<div class="layui-input-inline"  >
					<button id="btnSubmit" class="layui-btn layui-btn-small layui-btn-primary search" type="button">
					 	<i class="fa fa-search">&nbsp;</i>筛选
					</button>
					<button id="btnChart" class="layui-btn layui-btn-small layui-btn-primary print"  type="button">
					导出
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
	<script type="text/javascript" src="${ctxStatic}/prss/statisticalanalysis/jwStatsTransportList.js"></script>
</body>
</html>