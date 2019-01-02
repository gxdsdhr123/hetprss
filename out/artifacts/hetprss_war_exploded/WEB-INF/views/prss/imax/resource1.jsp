<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta name="decorator" content="imax" />
<jsp:include page="common/echarts.jsp"></jsp:include>
<link href="${ctxStatic}/jquery/plugins/liMarquee/css/liMarquee.css" rel="stylesheet" />
<script src="${ctxStatic}/jquery/plugins/liMarquee/js/jquery.liMarquee.js" type="text/javascript"></script>
<link href="${ctxStatic}/prss/imax/css/resource1.css" rel="stylesheet" />
<script src="${ctxStatic}/prss/imax/resource1.js" type="text/javascript"></script>
</head>
<body>
	<div class="container" >
		<div class="row container-title">
			<div class="col-sm-12 text-center">人员保障情况</div>
		</div>
		<div id="main_container" class="row">
			<!-- 类型选择 -->
			<div class="col-sm-12">
				<form  class="layui-form" >
					<div class="layui-inline">
						<label class="layui-form-label">岗位人员情况</label>
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
						<div class="col-sm-8" style="position: relative;">
							<div id="line" class="echarts-container"></div>
							<div class="chart-table" >
								<table style="width:100%;" class="chartTable" id="chartTable">
									
								</table>
							</div>
						</div>
						<div class="col-sm-4 bz-text" id="monitorText">
								人员保障高峰时段：<span class="gf_hour"></span><br/>
								人员保障紧缺时段：<span class="jq_hour"></span><br/>
								紧缺人数：<span class="jq_num"></span><br/>
								已完成任务数：<span class="bz_num"></span><br/>
								人均保障任务：<span class="pj_num"></span>
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
							在岗人数：<span class="all_num"></span> <br/>
							工作中：<span class="gz_num"></span> <br/>
							空闲：<span class="kx_num"></span> <br/>
							请假：<span class="qj_num"></span>
						</div>
						<div class="col-sm-7 pie-table">
							<div style="height:100%;position: relative;">
								<table class="dataTableHead">
									<thead>
										<tr>
											<td width="200">姓名</td>
											<td width="200">状态</td>
											<td width="400">保障航班</td>
											<td width="700">作业时长（分钟）</td>
											<td width="200">请假</td>
										</tr>
									</thead>
								</table>
								<div id="occupyTableBody">
									<table class="dataTable" id="occupyTable">
										<col width="200">
										<col width="200">
										<col width="400">
										<col width="700">
										<col width="200">
										<tbody></tbody>
									</table>
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