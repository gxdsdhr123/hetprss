<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
	<head>
		<title>摆渡车航班保障记录</title>
		<meta name="decorator" content="default" />
		<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
		<%@ include file="/WEB-INF/views/include/edittable.jsp"%>
		<script src="${ctxStatic}/echarts/echarts.min.js"></script>
		<script src="${ctxStatic}/echarts/theme/dark-prss.js"></script>
		<script src="${ctxStatic}/prss/imax/common/echarts.common.js" type="text/javascript"></script>
		<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script> 
		<script type="text/javascript" src="${ctxStatic}/prss/statisticalanalysis/flightGuaranteeRecord.js"></script>  
		<link href="${ctxStatic}/prss/statisticalanalysis/css/flightGuaranteeRecord.css" rel="stylesheet" />
		<script>
			var PATH = '${ctx}';
		</script>
	</head>
	<body>
		<div id="topDiv">
			查询日期：<input id="beginTime" class="layui-input" onfocus="WdatePicker({dateFmt:'yyyyMMdd',maxDate:'%y-%M-%d'})" readonly="readonly" value="${beginTime}"/>
			<button id="searchBut" type="button" class="btn btn-link">查询</button>
		</div>
		<table id="topTable" class="layui-table">
			<tr>
				<td colspan=3>航空公司</td>
				<td colspan=3>东航系</td>
				<td colspan=3>南航系</td>
				<td colspan=3>海航系</td>
				<td colspan=3>BGS外航</td>
				<td colspan=3>所有航空公司</td>
			</tr>
			<tr>
				<td colspan=3>进港航班架次</td>
				<td colspan=3>
					<c:if test="${dh_map == null}">
					   0
					</c:if>
					<c:if test="${dh_map != null}">
					   ${dh_map.IN_NUM}
					</c:if>
				</td>
				<td colspan=3>
					<c:if test="${nh_map == null}">
					   0
					</c:if>
					<c:if test="${nh_map != null}">
					   ${nh_map.IN_NUM}
					</c:if>
				</td>
				<td colspan=3>
					<c:if test="${hh_map == null}">
					   0
					</c:if>
					<c:if test="${hh_map != null}">
					   ${hh_map.IN_NUM}
					</c:if>
				</td>
				<td colspan=3>
					<c:if test="${bgs_map == null}">
					   0
					</c:if>
					<c:if test="${bgs_map != null}">
					   ${bgs_map.IN_NUM}
					</c:if>
				</td>
				<td colspan=3>${dh_map.IN_NUM+nh_map.IN_NUM+hh_map.IN_NUM+bgs_map.IN_NUM}</td>
			</tr>
			<tr>
				<td colspan=3>出港航班架次</td>
				<td colspan=3>
					<c:if test="${dh_map == null}">
					   0
					</c:if>
					<c:if test="${dh_map != null}">
					   ${dh_map.OUT_NUM}
					</c:if>
				</td>
				<td colspan=3>
					<c:if test="${nh_map == null}">
					   0
					</c:if>
					<c:if test="${nh_map != null}">
					   ${nh_map.OUT_NUM}
					</c:if>
				</td>
				<td colspan=3>
					<c:if test="${hh_map == null}">
					   0
					</c:if>
					<c:if test="${hh_map != null}">
					   ${hh_map.OUT_NUM}
					</c:if>
				</td>
				<td colspan=3>
					<c:if test="${bgs_map == null}">
					   0
					</c:if>
					<c:if test="${bgs_map != null}">
					   ${bgs_map.OUT_NUM}
					</c:if>
				</td>
				<td colspan=3>${dh_map.OUT_NUM+nh_map.OUT_NUM+hh_map.OUT_NUM+bgs_map.OUT_NUM}</td>
			</tr>
			<tr>
				<td colspan=3>合计</td>
				<td colspan=3>${dh_map.IN_NUM+dh_map.OUT_NUM}</td>
				<td colspan=3>${nh_map.IN_NUM+nh_map.OUT_NUM}</td>
				<td colspan=3>${hh_map.IN_NUM+hh_map.OUT_NUM}</td>
				<td colspan=3>${bgs_map.IN_NUM+bgs_map.OUT_NUM}</td>
				<td colspan=3>${dh_map.IN_NUM+dh_map.OUT_NUM+nh_map.IN_NUM+nh_map.OUT_NUM+hh_map.IN_NUM+hh_map.OUT_NUM+bgs_map.IN_NUM+bgs_map.OUT_NUM}</td>
			</tr>
			<tr>
				<td colspan=3>已保障航班架次</td>
				<td colspan=3>
					<c:if test="${dh_map == null}">
					   0
					</c:if>
					<c:if test="${dh_map != null}">
				    	${dh_map.FLT_NUM}
					</c:if>
				</td>
				<td colspan=3>
					<c:if test="${nh_map == null}">
					   0
					</c:if>
					<c:if test="${nh_map != null}">
					   ${nh_map.FLT_NUM}
					</c:if>
				</td>
				<td colspan=3>
					<c:if test="${hh_map == null}">
					   0
					</c:if>
					<c:if test="${hh_map != null}">
					   ${hh_map.FLT_NUM}
					</c:if>
				</td>
				<td colspan=3>
					<c:if test="${bgs_map == null}">
					   0
					</c:if>
					<c:if test="${bgs_map != null}">
					   ${bgs_map.FLT_NUM}
					</c:if>
				</td>
				<td colspan=3>${dh_map.FLT_NUM+nh_map.FLT_NUM+hh_map.FLT_NUM+bgs_map.FLT_NUM}</td>
			</tr>
			<tr>
				<td colspan=3>车辆类型</td>
				<td>摆渡车</td>
				<td>残升车</td>
				<td>考斯特</td>
				<td>摆渡车</td>
				<td>残升车</td>
				<td>考斯特</td>
				<td>摆渡车</td>
				<td>残升车</td>
				<td>考斯特</td>
				<td>摆渡车</td>
				<td>残升车</td>
				<td>考斯特</td>
				<td>摆渡车</td>
				<td>残升车</td>
				<td>考斯特</td>
			</tr>
			<tr>
				<td colspan=3>保障车次</td>
				<td>
					<c:if test="${dh_map == null}">
					   0
					</c:if>
					<c:if test="${dh_map != null}">
					   ${dh_map.BDC_NUM}
					</c:if>
				</td>
				<td>
					<c:if test="${dh_map == null}">
					   0
					</c:if>
					<c:if test="${dh_map != null}">
					   ${dh_map.CSC_NUM}
					</c:if>
				</td>
				<td>
					<c:if test="${dh_map == null}">
					   0
					</c:if>
					<c:if test="${dh_map != null}">
					   ${dh_map.KST_NUM}
					</c:if>
				</td>
				<td>
					<c:if test="${nh_map == null}">
					   0
					</c:if>
					<c:if test="${nh_map != null}">
					   ${nh_map.BDC_NUM}
					</c:if>
				</td>
				<td>
					<c:if test="${nh_map == null}">
					   0
					</c:if>
					<c:if test="${nh_map != null}">
					   ${nh_map.CSC_NUM}
					</c:if>
				</td>
				<td>
					<c:if test="${nh_map == null}">
					   0
					</c:if>
					<c:if test="${nh_map != null}">
					   ${nh_map.KST_NUM}
					</c:if>
				</td>
				<td>
					<c:if test="${hh_map == null}">
					   0
					</c:if>
					<c:if test="${hh_map != null}">
					   ${hh_map.BDC_NUM}
					</c:if>
				</td>
				<td>
					<c:if test="${hh_map == null}">
					   0
					</c:if>
					<c:if test="${hh_map != null}">
					   ${hh_map.CSC_NUM}
					</c:if>
				</td>
				<td>
					<c:if test="${hh_map == null}">
					   0
					</c:if>
					<c:if test="${hh_map != null}">
					   ${hh_map.KST_NUM}
					</c:if>
				</td>
				<td>
					<c:if test="${bgs_map == null}">
					   0
					</c:if>
					<c:if test="${bgs_map != null}">
					   ${bgs_map.BDC_NUM}
					</c:if>
				</td>
				<td>
					<c:if test="${bgs_map == null}">
					   0
					</c:if>
					<c:if test="${bgs_map != null}">
					   ${bgs_map.CSC_NUM}
					</c:if>
				</td>
				<td>
					<c:if test="${bgs_map == null}">
					   0
					</c:if>
					<c:if test="${bgs_map != null}">
					   ${bgs_map.KST_NUM}
					</c:if>
				</td>
				<td>${dh_map.BDC_NUM+nh_map.BDC_NUM+hh_map.BDC_NUM+bgs_map.BDC_NUM}</td>
				<td>${dh_map.CSC_NUM+nh_map.CSC_NUM+hh_map.CSC_NUM+bgs_map.CSC_NUM}</td>
				<td>${dh_map.KST_NUM+nh_map.KST_NUM+hh_map.KST_NUM+bgs_map.KST_NUM}</td>
			</tr>
			
		</table>
		<table id="topTable" class="layui-table">
			<tr>
				<td colspan=3>高峰时段</td>
				<td colspan=15>
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
				<td colspan=3>航空公司</td>
				<td colspan=3>东航系</td>
				<td colspan=3>南航系</td>
				<td colspan=3>海航系</td>
				<td colspan=3>BGS外航</td>
				<td colspan=3>所有航空公司</td>
			</tr>
			<tr>
				<td colspan=3>进港航班架次</td>
				<td colspan=3>
					<c:if test="${dh_high_map == null}">
					   0
					</c:if>
					<c:if test="${dh_high_map != null}">
					   ${dh_high_map.IN_NUM}
					</c:if>
				</td>
				<td colspan=3>
					<c:if test="${nh_high_map == null}">
					   0
					</c:if>
					<c:if test="${nh_high_map != null}">
					   ${nh_high_map.IN_NUM}
					</c:if>
				</td>
				<td colspan=3>
					<c:if test="${hh_high_map == null}">
					   0
					</c:if>
					<c:if test="${hh_high_map != null}">
					   ${hh_high_map.IN_NUM}
					</c:if>
				</td>
				<td colspan=3>
					<c:if test="${bgs_high_map == null}">
					   0
					</c:if>
					<c:if test="${bgs_high_map != null}">
					   ${bgs_high_map.IN_NUM}
					</c:if>
				</td>
				<td colspan=3>${dh_high_map.IN_NUM+nh_high_map.IN_NUM+hh_high_map.IN_NUM+bgs_high_map.IN_NUM}</td>
			</tr>
			<tr>
				<td colspan=3>出港航班架次</td>
				<td colspan=3>
					<c:if test="${dh_high_map == null}">
					   0
					</c:if>
					<c:if test="${dh_high_map != null}">
					   ${dh_high_map.OUT_NUM}
					</c:if>
				</td>
				<td colspan=3>
					<c:if test="${nh_high_map == null}">
					   0
					</c:if>
					<c:if test="${nh_high_map != null}">
					   ${nh_high_map.OUT_NUM}
					</c:if>
				</td>
				<td colspan=3>
					<c:if test="${hh_high_map == null}">
					   0
					</c:if>
					<c:if test="${hh_high_map != null}">
					   ${hh_high_map.OUT_NUM}
					</c:if>
				</td>
				<td colspan=3>
					<c:if test="${bgs_high_map == null}">
					   0
					</c:if>
					<c:if test="${bgs_high_map != null}">
					   ${bgs_high_map.OUT_NUM}
					</c:if>
				</td>
				<td colspan=3>${dh_high_map.OUT_NUM+nh_high_map.OUT_NUM+hh_high_map.OUT_NUM+bgs_high_map.OUT_NUM}</td>
			</tr>
			<tr>
				<td colspan=3>合计</td>
				<td colspan=3>${dh_high_map.IN_NUM+dh_high_map.OUT_NUM}</td>
				<td colspan=3>${nh_high_map.IN_NUM+nh_high_map.OUT_NUM}</td>
				<td colspan=3>${hh_high_map.IN_NUM+hh_high_map.OUT_NUM}</td>
				<td colspan=3>${bgs_high_map.IN_NUM+bgs_high_map.OUT_NUM}</td>
				<td colspan=3>${dh_high_map.IN_NUM+dh_high_map.OUT_NUM+nh_high_map.IN_NUM+nh_high_map.OUT_NUM+hh_high_map.IN_NUM+hh_high_map.OUT_NUM+bgs_high_map.IN_NUM+bgs_high_map.OUT_NUM}</td>
			</tr>
			<tr>
				<td colspan=3>已保障航班架次</td>
				<td colspan=3>
					<c:if test="${dh_high_map == null}">
					   0
					</c:if>
					<c:if test="${dh_high_map != null}">
					   ${dh_high_map.FLT_NUM}
					</c:if>
				</td>
				<td colspan=3>
					<c:if test="${nh_high_map == null}">
					   0
					</c:if>
					<c:if test="${nh_high_map != null}">
					   ${nh_high_map.FLT_NUM}
					</c:if>
				</td>
				<td colspan=3>
					<c:if test="${hh_high_map == null}">
					   0
					</c:if>
					<c:if test="${hh_high_map != null}">
					   ${hh_high_map.FLT_NUM}
					</c:if>
				</td>
				<td colspan=3>
					<c:if test="${bgs_high_map == null}">
					   0
					</c:if>
					<c:if test="${bgs_high_map != null}">
					   ${bgs_high_map.FLT_NUM}
					</c:if>
				</td>
				<td colspan=3>${dh_high_map.FLT_NUM+nh_high_map.FLT_NUM+hh_high_map.FLT_NUM+bgs_high_map.FLT_NUM}</td>
			</tr>
			<tr>
				<td colspan=3>车辆类型</td>
				<td>摆渡车</td>
				<td>残升车</td>
				<td>考斯特</td>
				<td>摆渡车</td>
				<td>残升车</td>
				<td>考斯特</td>
				<td>摆渡车</td>
				<td>残升车</td>
				<td>考斯特</td>
				<td>摆渡车</td>
				<td>残升车</td>
				<td>考斯特</td>
				<td>摆渡车</td>
				<td>残升车</td>
				<td>考斯特</td>
			</tr>
			<tr>
				<td colspan=3>保障车次</td>
				<td>
					<c:if test="${dh_high_map == null}">
					   0
					</c:if>
					<c:if test="${dh_high_map != null}">
					   ${dh_high_map.BDC_NUM}
					</c:if>
				</td>
				<td>
					<c:if test="${dh_high_map == null}">
					   0
					</c:if>
					<c:if test="${dh_high_map != null}">
					   ${dh_high_map.CSC_NUM}
					</c:if>
				</td>
				<td>
					<c:if test="${dh_high_map == null}">
					   0
					</c:if>
					<c:if test="${dh_high_map != null}">
					   ${dh_high_map.KST_NUM}
					</c:if>
				</td>
				<td>
					<c:if test="${nh_high_map == null}">
					   0
					</c:if>
					<c:if test="${nh_high_map != null}">
					   ${nh_high_map.BDC_NUM}
					</c:if>
				</td>
				<td>
					<c:if test="${nh_high_map == null}">
					   0
					</c:if>
					<c:if test="${nh_high_map != null}">
					   ${nh_high_map.CSC_NUM}
					</c:if>
				</td>
				<td>
					<c:if test="${nh_high_map == null}">
					   0
					</c:if>
					<c:if test="${nh_high_map != null}">
					   ${nh_high_map.KST_NUM}
					</c:if>
				</td>
				<td>
					<c:if test="${hh_high_map == null}">
					   0
					</c:if>
					<c:if test="${hh_high_map != null}">
					   ${hh_high_map.BDC_NUM}
					</c:if>
				</td>
				<td>
					<c:if test="${hh_high_map == null}">
					   0
					</c:if>
					<c:if test="${hh_high_map != null}">
					   ${hh_high_map.CSC_NUM}
					</c:if>
				</td>
				<td>
					<c:if test="${hh_high_map == null}">
					   0
					</c:if>
					<c:if test="${hh_high_map != null}">
					   ${hh_high_map.KST_NUM}
					</c:if>
				</td>
				<td>
					<c:if test="${bgs_high_map == null}">
					   0
					</c:if>
					<c:if test="${bgs_high_map != null}">
					   ${bgs_high_map.BDC_NUM}
					</c:if>
				</td>
				<td>
					<c:if test="${bgs_high_map == null}">
					   0
					</c:if>
					<c:if test="${bgs_high_map != null}">
					   ${bgs_high_map.CSC_NUM}
					</c:if>
				</td>
				<td>
					<c:if test="${bgs_high_map == null}">
					   0
					</c:if>
					<c:if test="${bgs_high_map != null}">
					   ${bgs_high_map.KST_NUM}
					</c:if>
				</td>
				<td>${dh_high_map.BDC_NUM+nh_high_map.BDC_NUM+hh_high_map.BDC_NUM+bgs_high_map.BDC_NUM}</td>
				<td>${dh_high_map.CSC_NUM+nh_high_map.CSC_NUM+hh_high_map.CSC_NUM+bgs_high_map.CSC_NUM}</td>
				<td>${dh_high_map.KST_NUM+nh_high_map.KST_NUM+hh_high_map.KST_NUM+bgs_high_map.KST_NUM}</td>
			</tr>
		</table>
		<!-- 摆渡车日航班运行统计 -->
		<div class="box run_container">
			<div class="box-header">
				<h4 class="">摆渡车日航班运行统计</h4>
			</div>
			<div class="box-body row">
				<div class="col-sm-12">
					<div id="runBar" class="echarts-container"></div>
				</div>
				<div class="col-sm-1">
					&nbsp;
				</div>
				<div class="col-sm-5">
					<div class="row">
						<div class="col-sm-4">进港架次：${dh_map.IN_NUM+nh_map.IN_NUM+hh_map.IN_NUM+bgs_map.IN_NUM}<span id="flight_innum"></span></div>
						<div class="col-sm-4">出港架次：${dh_map.OUT_NUM+nh_map.OUT_NUM+hh_map.OUT_NUM+bgs_map.OUT_NUM}<span id="flight_outnum"></span></div>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>