<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="imax" />
<jsp:include page="common/echarts.jsp"></jsp:include>
<link href="${ctxStatic}/prss/imax/css/resource2.css" rel="stylesheet" />
<script src="${ctxStatic}/prss/imax/resource2.js" type="text/javascript"></script>
</head>
<body>
	<div class="container" >
		<div class="row container-title">
			<div class="col-sm-12 text-center">设备资源情况</div>
		</div>
		<div id="main_container" class="row">
			<!-- 类型选择 -->
			<div class="col-sm-12">
				<form  class="layui-form" >
					<div class="layui-inline">
						<label class="layui-form-label">车辆设备情况</label>
						<div class="layui-input-inline">
							<select id="adType" name="adType" lay-filter="adType">
								<c:forEach items="${depList }" var="obj">
									<option>${obj}</option>
								</c:forEach>
							</select>
						</div>
					</div>
				</form>
			</div>
			<!-- 折线 -->
			<div class="col-sm-12">
				<div class="box">
					<div class="box-body row">
						<div class="col-sm-8">
							<div id="line" class="echarts-container"></div>
							<div class="chart-table" >
								<table style="width:100%;" class="chartTable" id="chartTable">
									
								</table>
							</div>
						</div>
						<div class="col-sm-4 bz-text">
								车辆保障高峰时段：<span class="gf_hour"></span><br/>
								车辆保障紧缺时段：<span class="jq_hour"></span><br/>
								紧缺车辆数：<span class="jq_num"></span><br/>
								已保障车次：<span class="bz_num"></span><br/>
								平均保障航班量：<span class="pj_num"></span>
						</div>
					</div>
				</div>
			</div>
			<!-- 图表 -->
			<div class="col-sm-12">
				<div class="box">
					<div class="box-body row">
						<div class="col-sm-3">
							<div id="pie" class="echarts-container"></div>
						</div>
						<div class="col-sm-2 pie-text">
							车辆人数：<span class="total_num"></span> <br/>
							在用车辆：<span class="zy_num"></span> <br/>
							空闲车辆：<span class="kx_num"></span> <br/>
							维修车辆：<span class="wx_num"></span>
						</div>
						<div class="col-sm-7 pie-table">
							<table class="dataTable" id="occupyTable">
								<thead>
									<tr>
										<td>车号</td>
										<td>状态</td>
										<td>保障航班</td>
										<td>运行时长（分钟）</td>
										<td>报修时间</td>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>