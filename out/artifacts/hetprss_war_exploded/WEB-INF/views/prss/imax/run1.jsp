<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="imax" />
<jsp:include page="common/echarts.jsp"></jsp:include>
<link href="${ctxStatic}/prss/imax/css/run1.css" rel="stylesheet" />
<script src="${ctxStatic}/prss/imax/run1.js" type="text/javascript"></script>
</head>
<body>
	<div class="container" >
		<div class="row container-title">
			<div class="col-sm-12 text-center">航班正常性分析</div>
		</div>
		<div id="main_container" class="row">
			<!-- 东航运行情况 -->
			<div class="col-sm-3">
				<div class="box chart-box">
					<div class="box-header">
						<h3 class="box-title">东航运行情况</h3>
					</div>
					<div class="box-body row" id="flight1">
						<div class="col-sm-12">
							<div id="chart1" class="echarts-container"></div>
						</div>
						<div class="col-sm-6">
							进港架次：<span class="jg"></span>
						</div>
						<div class="col-sm-6">
							出港架次：<span class="cg"></span>
						</div>
						<div class="col-sm-12">
							进出港架次：<span class="jcg"></span>
						</div>
						<div class="col-sm-12">
							远机位架次：<span class="yjw"></span>
						</div>
						<div class="col-sm-12">
							航班正常率：<span class="hbzc"></span>
						</div>
						<div class="col-sm-12">
							放行正常率：<span class="fxzc"></span>
						</div>
					</div>
				</div>
			</div>
			<!-- 南航运行情况 -->
			<div class="col-sm-3">
				<div class="box chart-box">
					<div class="box-header">
						<h3 class="box-title">南航运行情况</h3>
					</div>
					<div class="box-body row" id="flight2">
						<div class="col-sm-12">
							<div id="chart2" class="echarts-container"></div>
						</div>
						<div class="col-sm-6">
							进港架次：<span class="jg"></span>
						</div>
						<div class="col-sm-6">
							出港架次：<span class="cg"></span>
						</div>
						<div class="col-sm-12">
							进出港架次：<span class="jcg"></span>
						</div>
						<div class="col-sm-12">
							远机位架次：<span class="yjw"></span>
						</div>
						<div class="col-sm-12">
							航班正常率：<span class="hbzc"></span>
						</div>
						<div class="col-sm-12">
							放行正常率：<span class="fxzc"></span>
						</div>
					</div>
				</div>
			</div>
			<!-- 海航运行情况 -->
			<div class="col-sm-3">
				<div class="box chart-box">
					<div class="box-header">
						<h3 class="box-title">海航运行情况</h3>
					</div>
					<div class="box-body row"  id="flight3">
						<div class="col-sm-12">
							<div id="chart3" class="echarts-container"></div>
						</div>
						<div class="col-sm-6">
							进港架次：<span class="jg"></span>
						</div>
						<div class="col-sm-6">
							出港架次：<span class="cg"></span>
						</div>
						<div class="col-sm-12">
							进出港架次：<span class="jcg"></span>
						</div>
						<div class="col-sm-12">
							远机位架次：<span class="yjw"></span>
						</div>
						<div class="col-sm-12">
							航班正常率：<span class="hbzc"></span>
						</div>
						<div class="col-sm-12">
							放行正常率：<span class="fxzc"></span>
						</div>
					</div>
				</div>
			</div>
			<!-- 外航运行情况 -->
			<div class="col-sm-3">
				<div class="box chart-box">
					<div class="box-header">
						<h3 class="box-title">外航运行情况</h3>
					</div>
					<div class="box-body row"  id="flight4">
						<div class="col-sm-12">
							<div id="chart4" class="echarts-container"></div>
						</div>
						<div class="col-sm-6">
							进港架次：<span class="jg"></span>
						</div>
						<div class="col-sm-6">
							出港架次：<span class="cg"></span>
						</div>
						<div class="col-sm-12">
							进出港架次：<span class="jcg"></span>
						</div>
						<div class="col-sm-12">
							远机位架次：<span class="yjw"></span>
						</div>
						<div class="col-sm-12">
							航班正常率：<span class="hbzc"></span>
						</div>
						<div class="col-sm-12">
							放行正常率：<span class="fxzc"></span>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-12">
				<div id="tableData" class="box">
					<div class="box-body">
						<table class="dataTable" id="flight_table">
							<thead>
								<tr>
									<th>&nbsp;</th>
									<th>航班架次</th>
									<th>已保障航班</th>
									<th>取消</th>
									<th>备降</th>
									<th>延误</th>
									<th>延误0-1h</th>
									<th>延误1-2h</th>
									<th>延误>2h</th>
									<th>航班正常率</th>
									<th>放行正常率</th>
								</tr>
							</thead>
							<tbody>
								<!-- <tr>
									<td>进港</td>
									<td>234</td>
									<td>111</td>
									<td>2</td>
									<td>3</td>
									<td>11</td>
									<td>4</td>
									<td>4</td>
									<td>3</td>
									<td>70%</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>出港</td>
									<td>222</td>
									<td>111</td>
									<td>2</td>
									<td>3</td>
									<td>11</td>
									<td>4</td>
									<td>4</td>
									<td>3</td>
									<td>70%</td>
									<td>78%</td>
								</tr> -->
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>