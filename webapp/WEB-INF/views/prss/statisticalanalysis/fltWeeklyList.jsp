<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>周报统计表</title>
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
		<form  id="printExcel" action="${ctx}/statisticalanalysis/fltWeekly/exportExcel" method="post">
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
		</form>
		<table id="baseTable" class="layui-table" style="width: 100%; text-align: center; margin: 10px auto;">
			<thead>
				<tr>
					<th colspan="15">周报统计表</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td width=6% class="leri" style="border-bottom:none;text-align:right;padding:0px">
						<span style="margin-right: 10px;">类别</span>
					</td>
					<td width=30% colspan="5">运输飞行</td>
					<td width=48% colspan="8">通用飞行</td>
					<td width=6% rowspan="3">取消</td>
				</tr>
				<tr>
					<td class="rito" style="border-bottom:none;border-top:none;"> 
                		<span style="margin-left: 10px;">架次</span> 
                	</td> 
					<td width=12% colspan="2">国内</td>
					<td width=12% colspan="2">国际</td>
					<td width=6% rowspan="2">备降</td>
					<td width=6% rowspan="2">公务</td>
					<td width=6% rowspan="2">航拍</td>

					<td width=6% rowspan="2">专机</td>
					<td width=6% rowspan="2">校飞</td>
					<td width=6% rowspan="2">调机</td>
					<td width=6% rowspan="2">训练</td>
					<td width=6% rowspan="2">呼伦通航</td>

					<td width=6% rowspan="2">其他</td>
				</tr>
				<tr >
					<td width=6% style="border-top:none;text-align:left;">日期</td>
					<td width=6% >进港</td>
					<td width=6% >出港</td>
					<td width=6% >进港</td>
					<td width=6% >出港</td>
				</tr>
				<tr id="weeklyDataArea"></tr>
				<tr>
				<tr class="appendData weeklyDataArea"></tr>
				<tr>
					<td colspan="15">延误统计</td>
				</tr>
				<tr>
					<td>航班延误原因</td>
					<td>天气</td>
					<td>航空公司</td>
					<td>流量</td>
					<td>军事活动</td>
					
					<td>空管</td>
					<td>机场</td>
					<td>联检</td>
					<td>油料</td>
					<td>离港系统</td>
					
					<td>旅客</td>
					<td>公共安全</td>
					<td>航班时刻安排</td>
					<td>合计</td>
					<td></td>
				</tr>
				<tr id="sfDataArea">
				</tr>
				<tr>
					<td>放行不正常原因</td>
					<td>天气</td>
					<td>航空公司</td>
					<td>流量</td>
					<td>军事活动</td>
					
					<td>空管</td>
					<td>机场</td>
					<td>联检</td>
					<td>油料</td>
					<td>离港系统</td>
					
					<td>旅客</td>
					<td>公共安全</td>
					<td>航班时刻安排</td>
					<td>合计</td>
					<td></td>
				</tr>
				<tr id="jcDataArea">
				</tr>
			</tbody>
		</table>
		<canvas id ="myCanvas" style="position: absolute;z-index: 10;">Canvas画斜线</canvas>  
		</div>
	<script type="text/javascript" src="${ctxStatic}/prss/statisticalanalysis/fltWeeklyList.js"></script>
</body>
</html>