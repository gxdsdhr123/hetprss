<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="imax" />
<jsp:include page="common/echarts.jsp"></jsp:include>
<link href="${ctxStatic}/jquery/plugins/liMarquee/css/liMarquee.css" rel="stylesheet" />
<script src="${ctxStatic}/jquery/plugins/liMarquee/js/jquery.liMarquee.js" type="text/javascript"></script>
<link href="${ctxStatic}/prss/imax/css/index.css" rel="stylesheet" />
<script src="${ctxStatic}/prss/imax/index.js" type="text/javascript"></script>
</head>
<body>
	<div class="container" >
		<div class="row container-title">
			<div class="col-sm-4" id="nowTime"></div>
			<div class="col-sm-4 text-center" id="safeTime"></div>
			<div class="col-sm-4 text-right">值班领导  <span id="leader"></span></div>
		</div>
		<div id="main_container" class="row">
			<div class="col-sm-3">
				<!-- 车辆资源占用情况 -->
				<div class="box">
					<div class="box-header">
						<h3 class="box-title">车辆资源占用情况</h3>
					</div>
					<div class="box-body row">
						<!-- 摆渡车 -->
						<div class="col-sm-4">
							<div id="car1" class="echarts-container"></div>
							<div class="car_text">
								<h4>牵引车</h4>
								占用车辆：<span class="use"></span><br/> 
								空闲车辆：<span class="unuse"></span>
							</div>
						</div>
						<!-- 客梯车 -->
						<div class="col-sm-4">
							<div id="car2" class="echarts-container"></div>
							<div class="car_text">
								<h4>摆渡车</h4>
								占用车辆：<span class="use"></span><br/> 
								空闲车辆：<span class="unuse"></span>
							</div>
						</div>
						<!-- 牵引车 -->
						<div class="col-sm-4">
							<div id="car3" class="echarts-container"></div>
							<div class="car_text">
								<h4>行李车</h4>
								占用车辆：<span class="use"></span><br/> 
								空闲车辆：<span class="unuse"></span>
							</div>
						</div>
					</div>
				</div>
				<!-- 人员占用情况 -->
				<div class="box">
					<div class="box-header">
						<h3 class="box-title">人员占用情况</h3>
					</div>
					<div id="personOccupy" class="box-body row">
						<!-- 摆渡车司机 -->
						<div class="row" id="po_djkfw">
							<div class="col-sm-4"><h4>登机口服务</h4></div>
							<div class="col-sm-8">
								<div class="progress">
							        <div class="progress-bar" style="width:0%;" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" ></div>
							    </div>
							</div>
							<div class="col-sm-4">在岗人员：<span class="zg">0</span></div>
							<div class="col-sm-4">空闲：<span class="kx">0</span></div>
							<div class="col-sm-4">占用率：<span class="oc">0%</span></div>
						</div>
						<!-- 牵引车司机 -->
						<div class="row" id="po_jzjx">
							<div class="col-sm-4"><h4>监装监卸</h4></div>
							<div class="col-sm-8">
								<div class="progress">
							        <div class="progress-bar" style="width:0%;" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" ></div>
							    </div>
							</div>
							<div class="col-sm-4">在岗人员：<span class="zg">0</span></div>
							<div class="col-sm-4">空闲：<span class="kx">0</span></div>
							<div class="col-sm-4">占用率：<span class="oc">0%</span></div>
						</div>
						<!-- 窄体装卸 -->
						<div class="row" id="po_tc">
							<div class="col-sm-4"><h4>特车</h4></div>
							<div class="col-sm-8">
								<div class="progress">
							        <div class="progress-bar" style="width:0%;" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" ></div>
							    </div>
							</div>
							<div class="col-sm-4">在岗人员：<span class="zg">0</span></div>
							<div class="col-sm-4">空闲：<span class="kx">0</span></div>
							<div class="col-sm-4">占用率：<span class="oc">0%</span></div>
						</div>
						<!-- 宽体装卸 -->
						<div class="row" id="po_jw">
							<div class="col-sm-4"><h4>机务</h4></div>
							<div class="col-sm-8">
								<div class="progress">
							        <div class="progress-bar" style="width:0%;" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" ></div>
							    </div>
							</div>
							<div class="col-sm-4">在岗人员：<span class="zg">0</span></div>
							<div class="col-sm-4">空闲：<span class="kx">0</span></div>
							<div class="col-sm-4">占用率：<span class="oc">0%</span></div>
						</div>
						<!-- 清舱 -->
						<div class="row" id="po_ylgs">
							<div class="col-sm-4"><h4>油料公司</h4></div>
							<div class="col-sm-8">
								<div class="progress">
							        <div class="progress-bar" style="width:0%;" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" ></div>
							    </div>
							</div>
							<div class="col-sm-4">在岗人员：<span class="zg">0</span></div>
							<div class="col-sm-4">空闲：<span class="kx">0</span></div>
							<div class="col-sm-4">占用率：<span class="oc">0%</span></div>
						</div>
						<!-- 航线机务 -->
						<div class="row" id="po_pcgs">
							<div class="col-sm-4"><h4>配餐公司</h4></div>
							<div class="col-sm-8">
								<div class="progress">
							        <div class="progress-bar" style="width:0%;" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" ></div>
							    </div>
							</div>
							<div class="col-sm-4">在岗人员：<span class="zg">0</span></div>
							<div class="col-sm-4">空闲：<span class="kx">0</span></div>
							<div class="col-sm-4">占用率：<span class="oc">0%</span></div>
						</div>
						<!-- 航线机务 -->
						<div class="row" id="po_qjgs">
							<div class="col-sm-4"><h4>清洁公司</h4></div>
							<div class="col-sm-8">
								<div class="progress">
							        <div class="progress-bar" style="width:0%;" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" ></div>
							    </div>
							</div>
							<div class="col-sm-4">在岗人员：<span class="zg">0</span></div>
							<div class="col-sm-4">空闲：<span class="kx">0</span></div>
							<div class="col-sm-4">占用率：<span class="oc">0%</span></div>
						</div>
					</div>
				</div>
			</div>
			<div class="col-sm-6">
				<div class="row">
					<div class="col-sm-12">
						<!-- 航班运行情况 -->
						<div class="box run_container">
							<div class="box-header">
								<h3 class="box-title">航班运行情况</h3>
							</div>
							<div class="box-body row">
								<div class="col-sm-12">
									<div id="runBar" class="echarts-container"></div>
								</div>
								<div class="col-sm-1">
									&nbsp;
								</div>
								<div class="col-sm-7">
									<div class="row">
										<div class="col-sm-4">进港架次：<span id="flight_innum"></span></div>
										<div class="col-sm-4">出港架次：<span id="flight_outnum"></span></div>
									</div>
									<div class="row">
										<div class="col-sm-3">延误：<span id="flight_yw"></span></div>
										<div class="col-sm-3">取消：<span id="flight_qx"></span></div>
										<div class="col-sm-3">备降：<span id="flight_bj"></span></div>
									</div>
								</div>
								<div class="col-sm-4">
									<div class="row">
										<div class="col-sm-12">高峰时段一：<span id="flight_gfsd1"></span></div>
										<div class="col-sm-12">高峰时段二：<span id="flight_gfsd2"></span></div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="col-sm-12">
						<!-- 航班运行占比 -->
						<div class="row">
							<div class="col-sm-4">
								<div id="flightRate" class="box rate-container">
									<div class="box-body">
										<h4>航班正常率：<span id="zc_zb_0"></span></h4>
										<div id="zc_zb" class="echarts-container"></div>
									</div>
								</div>
							</div>
							<div class="col-sm-4">
								<div id="releaseRate" class="box rate-container">
									<div class="box-body">
										<h4>放行正常率：<span id="fx_zb_0"></span></h4>
										<div id="fx_zb" class="echarts-container"></div>
									</div>
								</div>
							</div>
							<div class="col-sm-4">
								<div id="farRate" class="box rate-container">
									<div class="box-body">
										<h4>廊桥位占比：<span id="jjw_zb_0"></span></h4>
										<div id="jjw_zb" class="echarts-container"></div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="col-sm-12">
						<!-- 航班保障进度 -->
						<div class="box">
							<div class="box-header">
								<h3 class="box-title">航班保障进度</h3>
							</div>
							<div id="monitor" class="box-body row">
								<div class="col-sm-12">
									<div class="progress">
								        <div class="progress-bar" style="width:0%;" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" ></div>
								    </div>
								</div>
							    <div class="col-sm-3">
							    	全天架次：<span class="count1"></span>
							    </div>
							    <div class="col-sm-3">
							    	已保障架次：<span class="count2"></span>
							    </div>
							    <div class="col-sm-3">
							    	未保障架次：<span class="count3"></span>
							    </div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="col-sm-3">
				<!-- 部门操作违规情况 -->
				<div class="box">
					<div class="box-header">
						<h3 class="box-title">部门操作违规情况</h3>
					</div>
					<div class="box-body">
						<div id="illegal" class="echarts-container"></div>
					</div>
				</div>
				<!-- 现场违规情况 -->
				<div class="box">
					<div class="box-header">
						<h3 class="box-title">现场违规情况</h3>
					</div>
					<div class="box-body illegal-body">
						<table id="illegal-table" >
						
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>