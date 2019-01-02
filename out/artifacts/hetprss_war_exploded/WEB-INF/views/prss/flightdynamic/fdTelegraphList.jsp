<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>航班动态报文</title>
<meta name="decorator" content="default" />
<%@ include file="/WEB-INF/views/include/bootstrap.jsp"%>
<%@include file="/WEB-INF/views/include/edittable.jsp"%>

<link href="${ctxStatic}/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<style>
</style>
</head>
<body>
    <input type="hidden" id="isHis" value="${isHis }"/>
	<div class="row">
		<div class="col-md-12 col-xs-12 col-sm-12">
			<div class="box-body">
				<div class="layui-inline">
					<label class="layui-form-label">航班号</label>
					<div class="layui-input-inline">
						<input class="layui-input" type="text" id="fltNo" placeholder="请输入关键字">
					</div>
					<c:if test="${isHis==1 }">
						<div class="layui-inline">
							<label class="layui-form-label">航班日期 从</label>
							<div class="layui-input-inline">
								<div class="layui-input-inline">
									<input class="form-control" id="beginTime" readonly="readonly"
										value="${date }" name="beginTime" placeholder='开始时间'
										onclick="WdatePicker({startDate:'%yyyy-%MM-%dd',dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endTime\')}'})"
										type="text">
								</div>
							</div>
						</div>
						<div class="layui-inline">
							<label class="layui-form-label">到</label>
							<div class="layui-input-inline">
								<input class="form-control" id="endTime" name="endTime" value="${date }" readonly="readonly"
									placeholder='结束时间'
									onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'beginTime\')}'})"
									type="text">
							</div>
						</div>
					</c:if>
                </div>
				<div class="bs-bars pull-right">
					<div id="toolbar">
						<button class="btn btn-link search" type="button">查询</button>
						<button class="btn btn-link " type="button" id="info">查看</button>
					</div>
				</div>
				<table id="baseTable"
					class="table table-striped table-bordered table-hover"></table>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="${ctxStatic}/prss/flightdynamic/fdTelegraphList.js"></script>
</body>
</html>