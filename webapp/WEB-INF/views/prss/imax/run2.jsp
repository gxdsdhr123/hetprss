<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="imax" />
<jsp:include page="common/echarts.jsp"></jsp:include>
<link href="${ctxStatic}/prss/imax/css/run2.css" rel="stylesheet" />
<script src="${ctxStatic}/prss/imax/run2.js" type="text/javascript"></script>
</head>
<body>
	<div class="container" >
		<div class="row container-title">
			<div class="col-sm-12 text-center">航班运行情况</div>
		</div>
		<div id="main_container" class="row">
			<div class="col-sm-8">
				<div class="box">
					<div class="box-header">
						<h3 class="box-title">航班运行占比</h3>
					</div>
					<div class="box-body row">
						<div class="col-sm-2 rate-msg" id="runText">
							航班架次：<span class="flight_num"></span>	<br/>
							始发航班架次：<span class="shf_flight_num"></span>	<br/>
							航后航班架次：<span class="hh_flight_num"></span>	<br/>
							货机架次：<span class="hj_flight_num"></span>	<br/>
							公务机架次：<span class="gwj_flight_num"></span>
						</div>
						<div class="col-sm-6">
							<div id="rate" class="echarts-container"></div>
						</div>
						<div class="col-sm-4">
							<div class="panelTable">
								<table id="chartTable">
									<thead>
										<tr>
											<td>机型</td>
											<td>架次</td>
										</tr>
									</thead>
									<tbody>
										
									</tbody>
								</table>
							</div>
						</div>
						<div id="rateTable" class="col-sm-12">
							<table class="dataTable">
								<tbody>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
			<div class="col-sm-4">
				<div class="box">
					<div class="box-header">
						<h3 class="box-title">航班延误情况</h3>
					</div>
					<div class="delay-msg box-body" id="ywText">
						<div id="delay" class="echarts-container"></div>
						<div class=" row">
							<div class="col-sm-6">
								进港延误架次：<span class="jgyw_num"></span>
							</div>
							<div class="col-sm-6">
								出港延误架次：<span class="cgyw_num"></span>
							</div>
							<div class="col-sm-6">
								始发延误架次：<span class="sfyw_num"></span>
							</div>
							<div class="col-sm-6">
								放行延误架次：<span class="fxyw_num"></span>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>