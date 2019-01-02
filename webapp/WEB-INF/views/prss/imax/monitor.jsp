<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="imax" />
<jsp:include page="common/echarts.jsp"></jsp:include>
<link href="${ctxStatic}/prss/imax/css/monitor.css" rel="stylesheet" />
<script src="${ctxStatic}/prss/imax/monitor.js" type="text/javascript"></script>
</head>
<body>
	<div class="container" >
		<div class="row container-title">
			<div class="col-sm-12 text-center">航班保障标准</div>
		</div>
		<div id="main_container" class="row">
			<div class="col-sm-6">
				<div class="row">
					<!-- 各机型保障标准情况 -->
					<div class="col-sm-12">
						<div class="box">
							<div class="box-header">
								<h3 class="box-title">各机型保障标准情况</h3>
							</div>
							<div class="box-body row">
								<div class="col-sm-12">
									<div id="bar" class="echarts-container"></div>
									<div class="bar-table" >
										<table  class="chartTable" style="width:95%;" >
											<tr>
												<td width="110">&nbsp;</td>
												<td>60座以下</td>
												<td>61-150座</td>
												<td>151-250座</td>
												<td>251-500座</td>
												<td>500座以上</td>
											</tr>
											<tr>
												<td>&nbsp;</td>
												<td>40</td>
												<td>55</td>
												<td>65</td>
												<td>75</td>
												<td>120</td>
											</tr>
											<tr id="barTable">
												
											</tr>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>
					<!-- 各岗位标准执行情况 -->
					<div class="col-sm-12">
						<div class="box">
							<div class="box-header">
								<h3 class="box-title">各岗位保障情况统计</h3>
							</div>
							<div class="box-body" style="min-height:271px;">
								<table class="dataTable" id="bzTable" style="height:auto;">
									<thead>
										<tr>
											<th>岗位</th>
											<th>符合标准数量</th>
											<th>超出标准数量</th>
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
			<!-- 各节点标准执行情况 -->
			<div class="col-sm-6">
				<div class="box monitor-items">
					<div class="box-header">
						<h3 class="box-title">各岗位到位情况</h3>
					</div>
					<div class="box-body">
						<form  class="layui-form" >
							<div class="layui-inline">
								<label class="layui-form-label">进出港类型</label>
								<div class="layui-input-inline">
									<select id="inOut" name="inOut" lay-filter="inOut">
										<option value="A" >进港</option>
										<option value="D" >出港</option>
										<option value="G" >进出港</option>
									</select>
								</div>
							</div>
						</form>
						<div class="row">
							<div class="col-sm-6">
								<div class="node-item">
									<h4>首辆摆渡车到位</h4>
									<div class="node-content">
										            实际： <span data-id="1"></span><br/>
           		  									标准： <span>-5</span>
									</div>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="node-item ">
									<h4>廊桥/客梯车靠接</h4>
									<div class="node-content">
										            实际： <span data-id="2"></span><br/>
           		  									标准： <span>-5</span>
									</div>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="node-item ">
									<h4>装卸到位</h4>
									<div class="node-content">
										            实际： <span data-id="3"></span><br/>
           		  									标准： <span>-5</span>
									</div>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="node-item ">
									<h4>机务到位</h4>
									<div class="node-content">
										            实际： <span data-id="4"></span><br/>
           		  									标准： <span>-5</span>
									</div>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="node-item ">
									<h4>服务到位</h4>
									<div class="node-content">
										            实际： <span data-id="5"></span><br/>
           		  									标准： <span>-5</span>
									</div>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="node-item ">
									<h4>配餐到位</h4>
									<div class="node-content">
										            实际： <span data-id="6"></span><br/>
           		  									标准： <span>-5</span>
									</div>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="node-item ">
									<h4>油料到位</h4>
									<div class="node-content">
										            实际： <span data-id="7"></span><br/>
           		  									标准： <span>-5</span>
									</div>
								</div>
							</div>
							<div class="col-sm-6">
								<div class="node-item ">
									<h4>清洁到位</h4>
									<div class="node-content">
										            实际： <span data-id="8"></span><br/>
           		  									标准： <span>-5</span>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>