<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="imax" />
<jsp:include page="common/echarts.jsp"></jsp:include>
<link href="${ctxStatic}/prss/imax/css/illegal.css" rel="stylesheet" />
<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script src="${ctxStatic}/prss/imax/illegal.js" type="text/javascript"></script>
</head>
<body>
	<div class="container" >
		<div class="row container-title">
			<div class="col-sm-12 text-center">部门违规情况</div>
		</div>
		<div id="main_container" class="row">
			<!-- 类型选择 -->
			<div class="col-sm-12">
				<form  class="layui-form" >
					<div class="layui-inline">
						<label class="layui-form-label">查询日期</label>
						<div class="layui-input-inline">
							<input class='layui-input rangeDate' type='text' placeholder='查询日期' 
		    				onfocus="WdatePicker({dateFmt:'yyyyMM',maxDate:'%y%M'})" id="date" value="${nowMonth }"
		    				onchange="initData();" readonly="readonly">
						</div>
					</div>
					<div class="layui-inline">
						<label class="layui-form-label">岗位</label>
						<div class="layui-input-inline">
							<select id="office" name="office" lay-filter="office">
								<c:forEach items="${depList }" var="obj">
									<option value="${obj.RESKIND }">${obj.KINDNAME }</option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="layui-inline">
						<label class="layui-form-label">对比日期</label>
						<div class="layui-input-inline">
							<input class='layui-input rangeDate' type='text' placeholder='对比日期' 
		    				onfocus="WdatePicker({dateFmt:'yyyyMM',maxDate:'%y%M'})" id="targetDate" value="${nowMonth }"
		    				onchange="initData();" readonly="readonly">
						</div>
					</div>
				</form>
			</div>
			<!-- 折线图 -->
			<div class="col-sm-8">
				<div class="box">
					<div class="box-body row" id="lineBox">
						<div class="col-sm-12" style="position: relative;">
							<div id="line" class="echarts-container"></div>
							<div class="chart-table" >
								<table style="width:100%;" class="chartTable" id="lineTable">
									
								</table>
							</div>
						</div>
						<div class="col-sm-4">
							人员违规次数：<span data-id="person"></span>
						</div>
						<div class="col-sm-4">
							车辆违规次数：<span data-id="car"></span>
						</div>
						<div class="col-sm-4">
							总违规次数：<span data-id="total"></span>
						</div>
						<div class="col-sm-4">
							环比<span data-name="hb">增加</span>：<span data-id="hb"></span>
						</div>
						<div class="col-sm-4">
							同比<span data-name="tb">增加</span>：<span data-id="tb"></span>
						</div>
					</div>
				</div>
			</div>
			<!-- 人员违规情况 -->
			<div class="col-sm-4">
				<div class="box person-illegal">
					<div class="box-header">
						<h3 class="box-title">人员违规情况</h3>
					</div>
					<div class="box-body">
						<table class="dataTable" id="personList">
							<thead>
								<tr>
									<th>序号</th>
									<th>姓名</th>
									<th>晚到</th>
									<th>超时</th>
									<th>合计</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<!-- 柱状图 -->
			<div class="col-sm-8">
				<div class="box">
						<div class="box-body row">
							<div class="col-sm-12">
								<div id="bar" class="echarts-container"></div>
								<div class="bar-table" >
									<table  class="chartTable" style="width:90%;" id="barTable">
										<tr>
											<td width="110">&nbsp;</td>
											<td>人员</td>
											<td>车辆</td>
											<td>总体</td>
										</tr>
										<tr>
											<td>&nbsp;</td>
											<td data-id="db_person">0</td>
											<td data-id="db_car">0</td>
											<td data-id="db_total">0</td>
										</tr>
										<tr>
											<td>&nbsp;</td>
											<td data-id="mb_person">0</td>
											<td data-id="mb_car">0</td>
											<td data-id="mb_total">0</td>
										</tr>
									</table>
								</div>
							</div>
						</div>
				</div>
			</div>
			<!-- 车辆违规情况 -->
			<div class="col-sm-4">
				<div class="box car-illegal">
					<div class="box-header">
						<h3 class="box-title">车辆违规情况</h3>
					</div>
					<div class="box-body">
						<table class="dataTable">
							<thead>
								<tr>
									<th>序号</th>
									<th>驾驶员</th>
									<th>超速</th>
									<th>越界</th>
									<th>合计</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>1</td>
									<td>张某</td>
									<td>0</td>
									<td>0</td>
									<td>0</td>
								</tr>
								<tr>
									<td>2</td>
									<td>张某</td>
									<td>0</td>
									<td>0</td>
									<td>0</td>
								</tr>
								<tr>
									<td>3</td>
									<td>张某</td>
									<td>0</td>
									<td>0</td>
									<td>0</td>
								</tr>
								<tr>
									<td>4</td>
									<td>张某</td>
									<td>0</td>
									<td>0</td>
									<td>0</td>
								</tr>
								<tr>
									<td>5</td>
									<td>张某</td>
									<td>0</td>
									<td>0</td>
									<td>0</td>
								</tr>
								<tr>
									<td>6</td>
									<td>张某</td>
									<td>0</td>
									<td>0</td>
									<td>0</td>
								</tr>
								<tr>
									<td>7</td>
									<td>张某</td>
									<td>0</td>
									<td>0</td>
									<td>0</td>
								</tr>
								<tr>
									<td>8</td>
									<td>张某</td>
									<td>0</td>
									<td>0</td>
									<td>0</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>