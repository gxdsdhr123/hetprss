<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
	<head>
		<title>航班运行状态</title>
		<meta name="decorator" content="default" />
		<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
		<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
		<script src="${ctxStatic}/echarts/echarts.min.js"></script>
		<script src="${ctxStatic}/echarts/theme/dark-prss.js"></script>
		<script src="${ctxStatic}/prss/imax/common/echarts.common.js" type="text/javascript"></script>
		<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script> 
		<script type="text/javascript" src="${ctxStatic}/prss/statisticalanalysis/flightRunState.js"></script>  
		<link href="${ctxStatic}/prss/statisticalanalysis/css/flightRunState.css" rel="stylesheet" />
		<script>
			var PATH = '${ctx}';
		</script>
	</head>
	<body>
		<div id="topDiv">
			查询日期：<input id="beginTime" class="layui-input" onfocus="WdatePicker({dateFmt:'yyyyMMdd',maxDate:'%y-%M-%d',startDate:'%y-%M-{%d-1}'})" readOnly value="${beginTime}"/>
			<button id="searchBut" type="button" class="btn btn-link">查询</button>
		</div>
		<table id="topTable" class="layui-table">
			<tr>
				<td>厦航</td>
				<td>${totalMap.XH_NUM}</td>
				<td>东航波音</td>
				<td>${totalMap.DHBY_NUM}</td>
				<td>BGS外航</td>
				<td>${totalMap.BGS_NUM}</td>
				<td>公务机</td>
				<td>${totalMap.GWJ_NUM}</td>
			</tr>
			<tr>
				<td>国内勤务</td>
				<td>${totalMap.GNQW_NUM}</td>
				<td>国内放行</td>
				<td>${totalMap.GNFX_NUM}</td>
				<td>国际勤务</td>
				<td>${totalMap.GJQW_NUM}</td>
				<td>公务机保障</td>
				<td>${totalMap.GWJBZ_NUM}</td>
			</tr>
		</table>
		<table id="topTable" class="layui-table">
			<tr>
				<td colspan=2>高峰时段</td>
				<td colspan=6>
					<c:if test="${highTime == '0'}">
					   00:00-01:00
					</c:if>
					<c:if test="${highTime == '1'}">
					   01:00-02:00
					</c:if>
					<c:if test="${highTime == '2'}">
					   02:00-03:00
					</c:if>
					<c:if test="${highTime == '3'}">
					   03:00-04:00
					</c:if>
					<c:if test="${highTime == '4'}">
					   04:00-05:00
					</c:if>
					<c:if test="${highTime == '5'}">
					   05:00-06:00
					</c:if>
					<c:if test="${highTime == '6'}">
					   06:00-07:00
					</c:if>
					<c:if test="${highTime == '7'}">
					   07:00-08:00
					</c:if>
					<c:if test="${highTime == '8'}">
					   08:00-09:00
					</c:if>
					<c:if test="${highTime == '9'}">
					   09:00-10:00
					</c:if>
					<c:if test="${highTime == '10'}">
					   10:00-11:00
					</c:if>
					<c:if test="${highTime == '11'}">
					   11:00-12:00
					</c:if>
					<c:if test="${highTime == '12'}">
					   12:00-13:00
					</c:if>
					<c:if test="${highTime == '13'}">
					   13:00-14:00
					</c:if>
					<c:if test="${highTime == '14'}">
					   14:00-15:00
					</c:if>
					<c:if test="${highTime == '15'}">
					   15:00-16:00
					</c:if>
					<c:if test="${highTime == '16'}">
					   16:00-17:00
					</c:if>
					<c:if test="${highTime == '17'}">
					   17:00-18:00
					</c:if>
					<c:if test="${highTime == '18'}">
					   18:00-19:00
					</c:if>
					<c:if test="${highTime == '19'}">
					   19:00-20:00
					</c:if>
					<c:if test="${highTime == '20'}">
					   20:00-21:00
					</c:if>
					<c:if test="${highTime == '21'}">
					   21:00-22:00
					</c:if>
					<c:if test="${highTime == '22'}">
					   22:00-23:00
					</c:if>
					<c:if test="${highTime == '23'}">
					   23:00-24:00
					</c:if>
				</td>	
			</tr>
			<tr>
				<td>厦航</td>
				<td>${totalHighMap.XH_NUM}</td>
				<td>东航波音</td>
				<td>${totalHighMap.DHBY_NUM}</td>
				<td>BGS外航</td>
				<td>${totalHighMap.BGS_NUM}</td>
				<td>公务机</td>
				<td>${totalHighMap.GWJ_NUM}</td>
			</tr>
			<tr>
				<td>国内勤务</td>
				<td>${totalHighMap.GNQW_NUM}</td>
				<td>国内放行</td>
				<td>${totalHighMap.GNFX_NUM}</td>
				<td>国际勤务</td>
				<td>${totalHighMap.GJQW_NUM}</td>
				<td>公务机保障</td>
				<td>${totalHighMap.GWJBZ_NUM}</td>
			</tr>
			<tr>
				<td colspan=2>高峰人数</td>
				<td colspan=6>${totalHighMap.PEOPLE_NUM}</td>
			</tr>
		</table>
		<div class="box run_container">
			<div class="box-body row">
				<div class="col-sm-12">
					<div id="runBar" class="echarts-container"></div>
				</div>
			</div>
		</div>
	</body>
</html>