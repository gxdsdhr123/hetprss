<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>正常率统计表</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" type="text/css">
<style type="text/css">
	.layui-layer-page .layui-layer-content{
		overflow: hidden !important;
	}	
</style>
<body>
	<div id="container">
		<form  id="printExcel" action="${ctx}/statisticalanalysis/fltNomalFx/exportExcel" method="post">
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">开始日期</label>
					<div class="layui-input-inline">
						<input type="text" name="startDate" id="startDate" class="layui-input"
							onclick="WdatePicker({dateFmt:'yyyyMMdd'});" value="${defaultDate}" />
					</div>

				</div>
				<div class="layui-inline">
					<label class="layui-form-label">结束日期</label>
					<div class="layui-input-inline"> 
						<input type="text" name="endDate" id="endDate" class="layui-input" 
							onclick="WdatePicker({dateFmt:'yyyyMMdd'});"  value="${defaultDate}"/>
					</div>
				</div>
				<div class="layui-inline  pull-right" id="toolbar">
					<button id="btnSubmit" class="layui-btn layui-btn-small layui-btn-primary search" type="button">
					 	<i class="fa fa-search">&nbsp;</i>查询
					</button>
					<button id="addBtn" class="layui-btn layui-btn-small layui-btn-primary print" type="button">
					 	打印
					</button>
				</div>
			</div>
			<input type="hidden" name="type" id="type" value="${type}">
		</form>
		<table id="baseTable" class="layui-table" style="width: 100%; text-align: center; margin: 10px auto;">
			<thead>
				<tr>
					<th colspan="19">${title}</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td width=5%></td>
					<td width=20% colspan="4">正常率统计</td>
					<td width=60% colspan="12">不正常原因</td>
					<td width=5% rowspan="2">同比</td>
					<td width=5% rowspan="2">环比</td>
				</tr>
				<tr>
					<td width=5%></td>
					<td width=5%>计划</td>
					<td width=5%>正常</td>
					<td width=5%>不正常</td>
					<td width=5%>正常率</td>

					<td width=5%>天气</td>
					<td width=5%>航空公司</td>
					<td width=5%>流量</td>
					<td width=5%>军事活动</td>
					<td width=5%>空管</td>

					<td width=5%>机场</td>
					<td width=5%>联检</td>
					<td width=5%>油料</td>
					<td width=5%>离港系统</td>
					<td width=5%>旅客</td>

					<td width=5%>公共安全</td>
					<td width=5%>航班时刻安排</td>
				</tr>
				<tr>
					<td width=5%>合计</td>
					<td width=5% class="dataArea" id="planFltNum"></td>
					<td width=5% class="dataArea" id="normalFltNum"></td>
					<td width=5% class="dataArea" id="unnormalFltNum"></td>
					<td width=5% class="dataArea" id="normalFx"></td>

					<td width=5% class="dataArea" id="unnormalFltNum1"></td>
					<td width=5% class="dataArea" id="unnormalFltNum2"></td>
					<td width=5% class="dataArea" id="unnormalFltNum3"></td>
					<td width=5% class="dataArea" id="unnormalFltNum4"></td>
					<td width=5% class="dataArea" id="unnormalFltNum5"></td>

					<td width=5% class="dataArea" id="unnormalFltNum6"></td>
					<td width=5% class="dataArea" id="unnormalFltNum7"></td>
					<td width=5% class="dataArea" id="unnormalFltNum8"></td>
					<td width=5% class="dataArea" id="unnormalFltNum9"></td>
					<td width=5% class="dataArea" id="unnormalFltNum10"></td>

					<td width=5% class="dataArea" id="unnormalFltNum11"></td>
					<td width=5% class="dataArea" id="unnormalFltNum12"></td>
					<td width=5% class="dataArea" id="yoy"></td>
					<td width=5% class="dataArea" id="mom"></td>
				</tr>
			</tbody>
		</table>
		</div>
	<script type="text/javascript" src="${ctxStatic}/prss/statisticalanalysis/fltNomalFxList.js"></script>
</body>
</html>